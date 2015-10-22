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

package com.liferay.portlet.exportimport.staging;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portlet.exportimport.lar.MissingReference;
import com.liferay.portlet.exportimport.model.ExportImportConfiguration;

import java.util.Locale;
import java.util.Map;

/**
 * @author Andrew Betts
 */
@ProviderType
public class StagingJSONHelperUtil {

	public static JSONArray getErrorMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		return _stagingJSONHelper.getErrorMessagesJSONArray(
			locale, missingReferences);
	}

	public static JSONObject getExceptionMessagesJSONObject(
		Locale locale, Exception e,
		ExportImportConfiguration exportImportConfiguration) {

		return _stagingJSONHelper.getExceptionMessagesJSONObject(
			locale, e, exportImportConfiguration);
	}

	public static JSONArray getWarningMessagesJSONArray(
		Locale locale, Map<String, MissingReference> missingReferences) {

		return _stagingJSONHelper.getWarningMessagesJSONArray(
			locale, missingReferences);
	}

	private static final StagingJSONHelper
		_stagingJSONHelper = ProxyFactory.newServiceTrackedInstance(
			StagingJSONHelper.class);

}