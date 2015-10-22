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

package com.liferay.exportimport.staging;

import com.liferay.portal.LayoutPrototypeException;
import com.liferay.portal.LocaleException;
import com.liferay.portal.PortletIdException;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskDetailsItemJSONObject;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskJSONTransformer;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.servlet.ServletResponseConstants;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Tuple;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.StagedModel;
import com.liferay.portal.model.adapter.StagedTheme;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.DuplicateFileEntryException;
import com.liferay.portlet.documentlibrary.FileExtensionException;
import com.liferay.portlet.documentlibrary.FileNameException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.exportimport.LARFileException;
import com.liferay.portlet.exportimport.LARFileSizeException;
import com.liferay.portlet.exportimport.LARTypeException;
import com.liferay.portlet.exportimport.MissingReferenceException;
import com.liferay.portlet.exportimport.configuration.ExportImportConfigurationConstants;
import com.liferay.portlet.exportimport.lar.MissingReference;
import com.liferay.portlet.exportimport.lar.MissingReferences;
import com.liferay.portlet.exportimport.lar.PortletDataException;
import com.liferay.portlet.exportimport.lar.StagedModelDataHandlerUtil;
import com.liferay.portlet.exportimport.lar.StagedModelType;
import com.liferay.portlet.exportimport.model.ExportImportConfiguration;
import com.liferay.portlet.exportimport.staging.StagingJSONHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Andrew Betts
 */
@Component(immediate = true)
@DoPrivileged
public class StagingJSONHelperImpl implements StagingJSONHelper {

	@Override
	public JSONArray getErrorMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		List<BackgroundTaskDetailsItemJSONObject>
			backgroundTaskDetailsItemJSONObjects = new ArrayList<>();

		for (String missingReferenceDisplayName : missingReferences.keySet()) {
			MissingReference missingReference = missingReferences.get(
				missingReferenceDisplayName);

			String className = missingReference.getClassName();
			Map<String, String> referrers = missingReference.getReferrers();

			String info = StringPool.BLANK;

			if (className.equals(StagedTheme.class.getName())) {
				info = LanguageUtil.format(
					locale,
					"the-referenced-theme-x-is-not-deployed-in-the-" +
						"current-environment",
					missingReference.getClassPK(), false);
			}
			else if (referrers.size() == 1) {
				Set<Map.Entry<String, String>> referrerDisplayNames =
					referrers.entrySet();

				Iterator<Map.Entry<String, String>> iterator =
					referrerDisplayNames.iterator();

				Map.Entry<String, String> entry = iterator.next();

				String referrerDisplayName = entry.getKey();
				String referrerClassName = entry.getValue();

				if (referrerClassName.equals(Portlet.class.getName())) {
					referrerDisplayName = PortalUtil.getPortletTitle(
						referrerDisplayName, locale);
				}

				info = LanguageUtil.format(
					locale, "referenced-by-a-x-x",
					new String[] {
						ResourceActionsUtil.getModelResource(
							locale, referrerClassName),
						referrerDisplayName
					}, false);
			}
			else {
				info = LanguageUtil.format(
					locale, "referenced-by-x-elements", referrers.size(), true);
			}

			Group group = GroupLocalServiceUtil.fetchGroup(
				missingReference.getGroupId());

			String errorMessage = ResourceActionsUtil.getModelResource(
				locale, missingReference.getClassName());

			if (group != null) {
				errorMessage += StringPool.SPACE +
					LanguageUtil.format(
						locale, "in-site-x", missingReference.getGroupId(),
						false);
			}

			BackgroundTaskDetailsItemJSONObject
				backgroundTaskDetailsItemJSONObject =
					new BackgroundTaskDetailsItemJSONObject(
						errorMessage, missingReferenceDisplayName, info);

			backgroundTaskDetailsItemJSONObjects.add(
				backgroundTaskDetailsItemJSONObject);
		}

		return BackgroundTaskJSONTransformer.toJSONArray(
			backgroundTaskDetailsItemJSONObjects);
	}

	@Override
	public JSONObject getExceptionMessagesJSONObject(
		Locale locale, Exception e,
		ExportImportConfiguration exportImportConfiguration) {

		String errorMessage = StringPool.BLANK;
		JSONArray errorMessagesJSONArray = null;
		int errorType = 0;
		JSONArray warningMessagesJSONArray = null;

		if (e instanceof DuplicateFileEntryException) {
			errorMessage = LanguageUtil.get(
				locale, "please-enter-a-unique-document-name");
			errorType = ServletResponseConstants.SC_DUPLICATE_FILE_EXCEPTION;
		}
		else if (e instanceof FileExtensionException) {
			errorMessage = LanguageUtil.format(
				locale,
				"document-names-must-end-with-one-of-the-following-extensions",
				".lar", false);
			errorType = ServletResponseConstants.SC_FILE_EXTENSION_EXCEPTION;
		}
		else if (e instanceof FileNameException) {
			errorMessage = LanguageUtil.get(
				locale, "please-enter-a-file-with-a-valid-file-name");
			errorType = ServletResponseConstants.SC_FILE_NAME_EXCEPTION;
		}
		else if (e instanceof FileSizeException ||
				 e instanceof LARFileSizeException) {

			long fileMaxSize = PropsValues.DL_FILE_MAX_SIZE;

			try {
				fileMaxSize = PrefsPropsUtil.getLong(
					PropsKeys.DL_FILE_MAX_SIZE);

				if (fileMaxSize == 0) {
					fileMaxSize = PrefsPropsUtil.getLong(
						PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE);
				}
			}
			catch (Exception e1) {
			}

			if ((exportImportConfiguration != null) &&
				((exportImportConfiguration.getType() ==
					ExportImportConfigurationConstants.
						TYPE_PUBLISH_LAYOUT_LOCAL) ||
					(exportImportConfiguration.getType() ==
						ExportImportConfigurationConstants.
							TYPE_PUBLISH_LAYOUT_REMOTE) ||
					(exportImportConfiguration.getType() ==
						ExportImportConfigurationConstants.
							TYPE_PUBLISH_PORTLET))) {

				errorMessage = LanguageUtil.get(
					locale,
					"file-size-limit-exceeded.-please-ensure-that-the-file-" +
						"does-not-exceed-the-file-size-limit-in-both-the-" +
						"live-environment-and-the-staging-environment");
			}
			else {
				errorMessage = LanguageUtil.format(
					locale,
					"please-enter-a-file-with-a-valid-file-size-no-larger-" +
						"than-x",
					TextFormatter.formatStorageSize(fileMaxSize, locale),
					false);
			}

			errorType = ServletResponseConstants.SC_FILE_SIZE_EXCEPTION;
		}
		else if (e instanceof LARTypeException) {
			LARTypeException lte = (LARTypeException)e;

			errorMessage = LanguageUtil.format(
				locale,
				"please-import-a-lar-file-of-the-correct-type-x-is-not-valid",
				lte.getMessage());
			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}
		else if (e instanceof LARFileException) {
			errorMessage = LanguageUtil.get(
				locale, "please-specify-a-lar-file-to-import");
			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}
		else if (e instanceof LayoutPrototypeException) {
			LayoutPrototypeException lpe = (LayoutPrototypeException)e;

			StringBundler sb = new StringBundler(4);

			sb.append("the-lar-file-could-not-be-imported-because-it-");
			sb.append("requires-page-templates-or-site-templates-that-could-");
			sb.append("not-be-found.-please-import-the-following-templates-");
			sb.append("manually");

			errorMessage = LanguageUtil.get(locale, sb.toString());

			errorMessagesJSONArray = JSONFactoryUtil.createJSONArray();

			List<Tuple> missingLayoutPrototypes =
				lpe.getMissingLayoutPrototypes();

			for (Tuple missingLayoutPrototype : missingLayoutPrototypes) {
				String layoutPrototypeUuid =
					(String)missingLayoutPrototype.getObject(1);

				String layoutPrototypeName =
					(String)missingLayoutPrototype.getObject(2);

				String layoutPrototypeClassName =
					(String)missingLayoutPrototype.getObject(0);

				String modelResource = ResourceActionsUtil.getModelResource(
					locale, layoutPrototypeClassName);

				BackgroundTaskDetailsItemJSONObject
					backgroundTaskDetailsItemJSONObject =
						new BackgroundTaskDetailsItemJSONObject(
							modelResource, layoutPrototypeName,
							layoutPrototypeUuid);

				errorMessagesJSONArray.put(
					backgroundTaskDetailsItemJSONObject.toJSONObject());
			}

			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}
		else if (e instanceof LocaleException) {
			LocaleException le = (LocaleException)e;

			errorMessage = LanguageUtil.format(
				locale,
				"the-available-languages-in-the-lar-file-x-do-not-match-the-" +
					"site's-available-languages-x",
				new String[] {
					StringUtil.merge(
						le.getSourceAvailableLocales(),
						StringPool.COMMA_AND_SPACE),
					StringUtil.merge(
						le.getTargetAvailableLocales(),
						StringPool.COMMA_AND_SPACE)
				}, false);

			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}
		else if (e instanceof MissingReferenceException) {
			MissingReferenceException mre = (MissingReferenceException)e;

			if ((exportImportConfiguration != null) &&
				((exportImportConfiguration.getType() ==
					ExportImportConfigurationConstants.
						TYPE_PUBLISH_LAYOUT_LOCAL) ||
					(exportImportConfiguration.getType() ==
						ExportImportConfigurationConstants.
							TYPE_PUBLISH_LAYOUT_REMOTE) ||
					(exportImportConfiguration.getType() ==
						ExportImportConfigurationConstants.
							TYPE_PUBLISH_PORTLET))) {

				errorMessage = LanguageUtil.get(
					locale,
					"there-are-missing-references-that-could-not-be-found-in-" +
						"the-live-environment");
			}
			else {
				errorMessage = LanguageUtil.get(
					locale,
					"there-are-missing-references-that-could-not-be-found-in-" +
						"the-current-site");
			}

			MissingReferences missingReferences = mre.getMissingReferences();

			errorMessagesJSONArray = getErrorMessagesJSONArray(
				locale, missingReferences.getDependencyMissingReferences());
			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
			warningMessagesJSONArray = getWarningMessagesJSONArray(
				locale, missingReferences.getWeakMissingReferences());
		}
		else if (e instanceof PortletDataException) {
			PortletDataException pde = (PortletDataException)e;

			StagedModel stagedModel = pde.getStagedModel();

			String referrerClassName = StringPool.BLANK;
			String referrerDisplayName = StringPool.BLANK;

			if (stagedModel != null) {
				StagedModelType stagedModelType =
					stagedModel.getStagedModelType();

				referrerClassName = stagedModelType.getClassName();
				referrerDisplayName = StagedModelDataHandlerUtil.getDisplayName(
					stagedModel);
			}

			if (pde.getType() == PortletDataException.INVALID_GROUP) {
				errorMessage = LanguageUtil.format(
					locale,
					"the-x-x-could-not-be-exported-because-it-is-not-in-the-" +
						"currently-exported-group",
					new String[] {
						ResourceActionsUtil.getModelResource(
							locale, referrerClassName),
						referrerDisplayName
					}, false);
			}
			else if (pde.getType() == PortletDataException.MISSING_DEPENDENCY) {
				errorMessage = LanguageUtil.format(
					locale,
					"the-x-x-has-missing-references-that-could-not-be-found-" +
						"during-the-export",
					new String[] {
						ResourceActionsUtil.getModelResource(
							locale, referrerClassName),
						referrerDisplayName
					}, false);
			}
			else if (pde.getType() == PortletDataException.STATUS_IN_TRASH) {
				errorMessage = LanguageUtil.format(
					locale,
					"the-x-x-could-not-be-exported-because-it-is-in-the-" +
						"recycle-bin",
					new String[] {
						ResourceActionsUtil.getModelResource(
							locale, referrerClassName),
						referrerDisplayName
					}, false);
			}
			else if (pde.getType() == PortletDataException.STATUS_UNAVAILABLE) {
				errorMessage = LanguageUtil.format(
					locale,
					"the-x-x-could-not-be-exported-because-its-workflow-" +
						"status-is-not-exportable",
					new String[] {
						ResourceActionsUtil.getModelResource(
							locale, referrerClassName),
						referrerDisplayName
					}, false);
			}
			else {
				errorMessage = e.getLocalizedMessage();
			}

			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}
		else if (e instanceof PortletIdException) {
			errorMessage = LanguageUtil.get(
				locale, "please-import-a-lar-file-for-the-current-portlet");
			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}
		else {
			errorMessage = e.getLocalizedMessage();
			errorType = ServletResponseConstants.SC_FILE_CUSTOM_EXCEPTION;
		}

		JSONObject exceptionMessagesJSONObject =
			JSONFactoryUtil.createJSONObject();

		exceptionMessagesJSONObject.put("message", errorMessage);

		if ((errorMessagesJSONArray != null) &&
			(errorMessagesJSONArray.length() > 0)) {

			exceptionMessagesJSONObject.put(
				"messageListItems", errorMessagesJSONArray);
		}

		exceptionMessagesJSONObject.put("status", errorType);

		if ((warningMessagesJSONArray != null) &&
			(warningMessagesJSONArray.length() > 0)) {

			exceptionMessagesJSONObject.put(
				"warningMessages", warningMessagesJSONArray);
		}

		return exceptionMessagesJSONObject;
	}

	@Override
	public JSONArray getWarningMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		List<BackgroundTaskDetailsItemJSONObject>
			backgroundTaskDetailsItemJSONObjects = new ArrayList<>();

		for (String missingReferenceReferrerClassName :
				missingReferences.keySet()) {

			MissingReference missingReference = missingReferences.get(
				missingReferenceReferrerClassName);

			Map<String, String> referrers = missingReference.getReferrers();

			String info = StringPool.BLANK;

			if (Validator.isNotNull(missingReference.getClassName())) {
				info = LanguageUtil.format(
					locale,
					"the-original-x-does-not-exist-in-the-current-" +
						"environment",
					ResourceActionsUtil.getModelResource(
						locale, missingReference.getClassName()),
					false);
			}

			String errorMessage = ResourceActionsUtil.getModelResource(
				locale, missingReferenceReferrerClassName);

			BackgroundTaskDetailsItemJSONObject
				backgroundTaskDisplayDetailsItem =
					new BackgroundTaskDetailsItemJSONObject(
						errorMessage, String.valueOf(referrers.size()), info);

			backgroundTaskDetailsItemJSONObjects.add(
				backgroundTaskDisplayDetailsItem);
		}

		return BackgroundTaskJSONTransformer.toJSONArray(
			backgroundTaskDetailsItemJSONObjects);
	}

}