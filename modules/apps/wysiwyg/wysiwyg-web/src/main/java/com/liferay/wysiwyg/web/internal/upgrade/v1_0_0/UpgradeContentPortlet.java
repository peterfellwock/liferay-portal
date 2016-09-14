/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.wysiwyg.web.internal.upgrade.v1_0_0;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderLocalServiceUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.util.PortalInstances;
import com.liferay.wysiwyg.web.internal.constants.WysiwygConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Peter Fellwock
 */
public class UpgradeContentPortlet extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		try {
			long userId = _getUserIdFromDDM();

			if (userId == 0) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Upgrade of WYSIWYG data and portlets not executed " +
							"DDMStructure/Template not in place");
				}
			}
			else {
				_migrate(userId);
			}
		}
		catch (SQLException sqle) {
			_log.error(
				"Unable to get PortletPreferences for WYSIWYG Portlet ", sqle);
		}
	}

	private JournalArticle _convertWysiwygContent(
			long userId, long companyId, long groupId, String content)
		throws Exception {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(groupId);

		long journalFolderId = _getFolderId(userId, groupId, serviceContext);

		String xmlContent = _getContentAsXml(content, companyId);

		String title = _getTitleFromContent(content, 3);

		String defaultLocaleId = UpgradeProcessUtil.getDefaultLanguageId(
			companyId);

		Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLocaleId);

		Map<Locale, String> titleMap = Collections.singletonMap(
			defaultLocale, title);

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.addArticle(
				userId, groupId, journalFolderId, titleMap, titleMap,
				xmlContent, WysiwygConstants.STRUCTURE_KEY,
				WysiwygConstants.TEMPLATE_KEY, serviceContext);

		return journalArticle;
	}

	private long _getClassPK(JournalArticle article) {
		if ((article.isDraft() || article.isPending()) &&
			(article.getVersion() != JournalArticleConstants.VERSION_DEFAULT)) {

			return article.getPrimaryKey();
		}
		else {
			return article.getResourcePrimKey();
		}
	}

	private String _getContentAsXml(String content, long companyId)
		throws Exception {

		String defaultLocale = UpgradeProcessUtil.getDefaultLanguageId(
			companyId);

		Document newDocument = SAXReaderUtil.createDocument();

		Element newRootElement = SAXReaderUtil.createElement("root");

		newRootElement.addAttribute("available-locales", defaultLocale);
		newRootElement.addAttribute("default-locale", defaultLocale);

		newDocument.add(newRootElement);

		Element dynamicElementElement = SAXReaderUtil.createElement(
			"dynamic-element");

		dynamicElementElement.addAttribute("name", "content");
		dynamicElementElement.addAttribute("type", "text_area");
		dynamicElementElement.addAttribute("index-type", "keyword");
		dynamicElementElement.addAttribute("instance-id", "rnev");

		newRootElement.add(dynamicElementElement);

		Element dynamicContentElement = SAXReaderUtil.createElement(
			"dynamic-content");

		dynamicContentElement.addAttribute("language-id", defaultLocale);
		dynamicContentElement.addCDATA(content);

		dynamicElementElement.add(dynamicContentElement);

		return newDocument.asXML();
	}

	private long _getFolderId(
			long userId, long groupId, ServiceContext serviceContext)
		throws PortalException {

		List<JournalFolder> journalFolders =
			JournalFolderLocalServiceUtil.getFolders(groupId, 0);

		for (JournalFolder journalFolder : journalFolders) {
			if (WysiwygConstants.FOLDER_NAME.equals(journalFolder.getName())) {
				return journalFolder.getFolderId();
			}
		}

		JournalFolder existingJournalFolder =
			JournalFolderLocalServiceUtil.addFolder(
				userId, groupId, 0, WysiwygConstants.FOLDER_NAME,
			WysiwygConstants.FOLDER_DESCRIPTION, serviceContext);

		return existingJournalFolder.getFolderId();
	}

	private String _getJournalPortletPreferences(
		JournalArticle journalArticle) {

		long classPK = _getClassPK(journalArticle);

		AssetEntry assetEntry = AssetEntryLocalServiceUtil.fetchEntry(
			JournalArticle.class.getName(), classPK);

		StringBuilder sb = new StringBuilder();

		sb.append("<portlet-preferences><preference>");
		sb.append("<name>ddmTemplateKey</name>");
		sb.append("<value>BASIC-WEB-CONTENT</value>");
		sb.append("</preference><preference>");
		sb.append("<name>assetEntryId</name><value>");
		sb.append(assetEntry.getEntryId());
		sb.append("</value>");
		sb.append("</preference><preference>");
		sb.append("<name>userToolAssetAddonEntryKeys</name><value></value>");
		sb.append("</preference><preference>");
		sb.append("<name>enableViewCountIncrement</name><value>true</value>");
		sb.append("</preference><preference>");
		sb.append("<name>groupId</name><value>");
		sb.append(journalArticle.getGroupId());
		sb.append("</value>");
		sb.append("</preference><preference>");
		sb.append("<name>articleId</name><value>");
		sb.append(journalArticle.getArticleId());
		sb.append("</value>");
		sb.append("</preference><preference>");
		sb.append("<name>contentMetadataAssetAddonEntryKeys</name>");
		sb.append("<value></value>");
		sb.append("</preference></portlet-preferences>");

		return sb.toString();
	}

	private String _getQuery() {
		StringBuilder sb = new StringBuilder();

		sb.append("select PortletPreferences.portletPreferencesId, ");
		sb.append("PortletPreferences.companyId, PortletPreferences.plid, ");
		sb.append("Layout.groupId, PortletPreferences.portletId, ");
		sb.append("PortletPreferences.preferences from PortletPreferences ");
		sb.append("inner join Layout on Layout.plid = ");
		sb.append("PortletPreferences.plid where ");
		sb.append("PortletPreferences.portletId like '");
		sb.append(WysiwygConstants.WYSIWYG_PORTLET_KEY);
		sb.append("%'");

		return sb.toString();
	}

	private String _getTitleFromContent(String text, int wordCount) {
		StringBuilder sb = new StringBuilder();

		int counter = 1;
		int spaceIndex = 0;

		while (counter <= wordCount) {
			if (text.indexOf(' ') > -1) {
				spaceIndex = text.indexOf(' ');
				sb.append(text.substring(0, spaceIndex + 1));
				text = text.substring(spaceIndex + 1, text.length());
			}
			else {
				sb.append(text.substring(0, text.length()));
				counter = wordCount + 1;
			}

			counter++;
		}

		return HtmlUtil.extractText(sb.toString());
	}

	private long _getUserIdFromDDM() {
		long companyId = PortalInstances.getDefaultCompanyId();

		try {
			DDMStructure ddmStructure =
				DDMStructureLocalServiceUtil.getStructure(
					companyId, PortalUtil.getClassNameId(JournalArticle.class),
					WysiwygConstants.STRUCTURE_KEY);

			return ddmStructure.getUserId();
		}
		catch (PortalException pe) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Unable to get User ID from DDMStructure when migrating" +
						"WYSIWYG Data and Portlet");
			}
		}

		try {
			DDMTemplate ddmTemplate = DDMTemplateLocalServiceUtil.getTemplate(
				companyId, PortalUtil.getClassNameId(DDMStructure.class),
				WysiwygConstants.TEMPLATE_KEY);

			return ddmTemplate.getUserId();
		}
		catch (PortalException pe) {
			if (_log.isInfoEnabled()) {
				_log.info(
					"Unable to get User ID from DDMTemplate when migrating" +
						"WYSIWYG Data and Portlet");
			}
		}

		return 0;
	}

	private void _migrate(long userId) throws SQLException {
		String query = _getQuery();

		try (Connection con = DataAccess.getUpgradeOptimizedConnection();

			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				long portletPreferencesId = rs.getLong("portletPreferencesId");
				long companyId = rs.getLong("companyId");
				long groupId = rs.getLong("groupId");
				long plid = rs.getLong("plid");
				String portletId = rs.getString("portletId");
				String preferences = rs.getString("preferences");

				javax.portlet.PortletPreferences preferenceMap =
					PortletPreferencesFactoryUtil.fromXML(
						companyId, 0, 0, plid, portletId, preferences);

				String content = preferenceMap.getValue(
					"message", StringPool.BLANK);

				if (Validator.isNotNull(content)) {
					_migrateDataAndPortlet(
						userId, portletPreferencesId, companyId, groupId, plid,
						portletId, content);
				}
			}
		}
	}

	private void _migrateDataAndPortlet(
		long userId, long portletPreferencesId, long companyId, long groupId,
		long plid, String portletId, String content) {

		try {
			JournalArticle journalArticle = _convertWysiwygContent(
				userId, companyId, groupId, content);

			if (journalArticle != null) {
				_updatePortletPreferences(
					portletPreferencesId, journalArticle, portletId);
				_updatePortletReferenceInLayout(plid);
			}
		}
		catch (Exception e) {
			_log.error("Unable to migrate WYSIWYG Data and Portlet", e);
		}
	}

	private void _updatePortletPreferences(
			long portletPreferencesId, JournalArticle journalArticle,
			String portletId)
		throws PortalException {

		String journalPortletId = StringUtil.replace(
			portletId, WysiwygConstants.WYSIWYG_PORTLET_KEY,
			WysiwygConstants.JOURNAL_PORTLET_KEY);

		String journalPreference = _getJournalPortletPreferences(
			journalArticle);

		PortletPreferences modifiedPortletPreferences =
			PortletPreferencesLocalServiceUtil.getPortletPreferences(
				portletPreferencesId);

		modifiedPortletPreferences.setPortletId(journalPortletId);
		modifiedPortletPreferences.setPreferences(journalPreference);

		PortletPreferencesLocalServiceUtil.updatePortletPreferences(
			modifiedPortletPreferences);
	}

	private void _updatePortletReferenceInLayout(long plid)
		throws PortalException {

		Layout layout = LayoutLocalServiceUtil.getLayout(plid);

		String typedSettings = layout.getTypeSettings();

		String updatedTypedSettings = StringUtil.replace(
			typedSettings, WysiwygConstants.WYSIWYG_PORTLET_KEY,
			WysiwygConstants.JOURNAL_PORTLET_KEY);

		layout.setTypeSettings(updatedTypedSettings);

		LayoutLocalServiceUtil.updateLayout(layout);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpgradeContentPortlet.class);

}