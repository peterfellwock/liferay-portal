package com.liferay.wysiwyg.web.internal.upgrade.v1_0_0;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.util.PortalInstances;
import com.liferay.wysiwyg.web.internal.constants.WysiwygConstants;

public class WysiwygContentHelperUtil {
	
	
	public static void contentUpdate(){
		
		try{
			
			System.out.println("I am here!!!!!");
			
			Company portalCompany =
					CompanyLocalServiceUtil.getCompanyById(
						PortalInstances.getDefaultCompanyId());
			
			DDMStructure ddmStructure = DDMStructureLocalServiceUtil.getStructure(
					portalCompany.getGroupId(), 
					PortalUtil.getClassNameId(JournalArticle.class),
					WysiwygConstants.STRUCTURE_KEY);
				
				DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.getTemplate(
					portalCompany.getGroupId(),
					PortalUtil.getClassNameId(DDMStructure.class),
					WysiwygConstants.TEMPLATE_KEY);
			
			try(
					Connection con = DataAccess.getUpgradeOptimizedConnection(); 
					PreparedStatement ps = con.prepareStatement("select PortletPreferences.portletPreferencesId, PortletPreferences.companyId, PortletPreferences.plid, Layout.groupId, PortletPreferences.portletId, PortletPreferences.preferences from PortletPreferences inner join Layout on Layout.plid = PortletPreferences.plid where PortletPreferences.portletId like 'com_liferay_wysiwyg_web_portlet_WysiwygPortlet%'");
					ResultSet rs = ps.executeQuery()){
				
				while(rs.next()){
					long portletPreferencesId = rs.getLong("portletPreferencesId");
					long companyId = rs.getLong("companyId");
					long groupId = rs.getLong("groupId");
					long plid = rs.getLong("plid");
					String portletId = rs.getString("portletId");
					String preferences = rs.getString("preferences");
					
					javax.portlet.PortletPreferences preferenceMap = 
						PortletPreferencesFactoryUtil.fromXML(
								companyId,
							0, 
							0, 
							plid, portletId,
							preferences);
					
					String content = preferenceMap.getValue("message", StringPool.BLANK);
				
					JournalArticle journalArticle = convertWysiwygContent(companyId, groupId, preferences, content, ddmTemplate);
					
					cleanupPortletPreference(portletPreferencesId, plid, journalArticle, portletId);
					
				}
			}
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
	private static void cleanupPortletPreference(long portletPreferencesId, long plid, JournalArticle journalArticle, String portletId) throws PortalException {
		
		String journalPortletId = StringUtil.replace(
			portletId, "com_liferay_wysiwyg_web_portlet_WysiwygPortlet",
			"com_liferay_journal_content_web_portlet_JournalContentPortlet");
		
		PortletPreferences modifiedPortletPreferences = PortletPreferencesLocalServiceUtil.getPortletPreferences(portletPreferencesId);
		
		modifiedPortletPreferences.setPortletId(journalPortletId);
		
		String journalPreference = createXmlJournalPreferences(journalArticle);
		modifiedPortletPreferences.setPreferences(journalPreference);
		
		PortletPreferencesLocalServiceUtil.updatePortletPreferences(modifiedPortletPreferences);
		
	}

	private static String createXmlJournalPreferences(JournalArticle journalArticle) {
		
		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(JournalArticle.class.getName(), journalArticle.getId());
		
		StringBuilder sb = new StringBuilder();
		sb.append("<portlet-preferences><preference>");
		sb.append("<name>ddmTemplateKey</name><value>BASIC-WEB-CONTENT</value>");
		sb.append("</preference><preference>");
		sb.append("<name>assetEntryId</name><value>" + assetEntry.toString() + "</value>");
		sb.append("</preference><preference>");
		sb.append("<name>userToolAssetAddonEntryKeys</name><value></value>");
		sb.append("</preference><preference>");
		sb.append("<name>enableViewCountIncrement</name><value>true</value>");
		sb.append("</preference><preference>");
		sb.append("<name>groupId</name><value>" + journalArticle.getGroupId() + "</value>");
		sb.append("</preference><preference>");
		sb.append("<name>articleId</name><value>" + journalArticle.getArticleId() + "</value>");
		sb.append("</preference><preference>");
		sb.append("<name>contentMetadataAssetAddonEntryKeys</name><value></value>");
		sb.append("</preference></portlet-preferences>");
		
		return sb.toString();
	}

		public static JournalArticle convertWysiwygContent(
			long companyId, long groupId, String preferences, String content, DDMTemplate ddmTemplate) throws PortalException{
			
			long userId = 20120;
			
			
			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			

			long journalFolderId = getFolderId(userId, groupId, serviceContext);
			
				
			String title = getTitleFromContent(content, 3);	
					
			Map<Locale, String> titleMap = Collections.singletonMap(LocaleUtil.getDefault(), title);
					
			serviceContext.setScopeGroupId(groupId);
			
			JournalArticle journalArticle = 
				JournalArticleLocalServiceUtil.addArticle(
					userId, groupId,
					journalFolderId, titleMap, titleMap, 
					convertToXml(content), WysiwygConstants.STRUCTURE_KEY,
					WysiwygConstants.TEMPLATE_KEY, serviceContext);
			
			return journalArticle;
			
		}
		
		private static String getTitleFromContent(String text, int wordCount) {
			
			StringBuilder sb = new StringBuilder();
			

			int counter = 1;
			int spaceIndex = 0;
			
			while(counter <= wordCount){
				if (text.indexOf(' ') > -1) {
					spaceIndex = text.indexOf(' ');
					sb.append(text.substring(0, spaceIndex + 1));
					text = text.substring(spaceIndex + 1, text.length());
				}
				else {
					sb.append(text.substring(0, text.length()));
					counter = wordCount+1;
				}
				counter++;
			}
			
			return sb.toString();
		}

		public static String convertToXml(String content) {
			
			Locale defaultLocale = LocaleUtil.getDefault();
			
			Document newDocument = SAXReaderUtil.createDocument();

			Element newRootElement = SAXReaderUtil.createElement("root");
			newRootElement.addAttribute("available-locales", defaultLocale.toString());
			newRootElement.addAttribute("default-locale", defaultLocale.toString());

			newDocument.add(newRootElement);
			
			Element dynamicElementElement = SAXReaderUtil.createElement("dynamic-element");

			dynamicElementElement.addAttribute("name", "content");
			dynamicElementElement.addAttribute("type", "text_area");
			dynamicElementElement.addAttribute("index-type", "keyword");
			dynamicElementElement.addAttribute("instance-id", "rnev");

			newRootElement.add(dynamicElementElement);
				
			Element dynamicContentElement = SAXReaderUtil.createElement("dynamic-content");

			dynamicContentElement.addAttribute("language-id", defaultLocale.toString());
			dynamicContentElement.addCDATA(content);

			dynamicElementElement.add(dynamicContentElement);

			return newDocument.asXML();
		}

		public static long getFolderId(
			long userId, long groupId, ServiceContext serviceContext)
			throws PortalException{
			
			List<JournalFolder> journalFolders = JournalFolderLocalServiceUtil.getFolders(groupId, 0);		
			
			for(JournalFolder journalFolder: journalFolders){
				if(WysiwygConstants.FOLDER_NAME.equals(journalFolder.getName())){
					return journalFolder.getFolderId();
				}
			}
			
			JournalFolder existingJournalFolder = JournalFolderLocalServiceUtil.addFolder(
				userId, groupId, 0, WysiwygConstants.FOLDER_NAME, 
				WysiwygConstants.FOLDER_DESCRIPTION, serviceContext);
			
			return existingJournalFolder.getFolderId();
		}
		

}
