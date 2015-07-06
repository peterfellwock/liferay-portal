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

package com.liferay.portal.deploy.hot;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.SanitizerLogWrapper;
import com.liferay.portal.kernel.plugin.PluginPackage;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.util.CustomJspRegistryUtil;
import com.liferay.portal.util.PortalUtil;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 * @author Peter Fellwock
 */
public class CustomJspBagRegistryUtil {

	public static CustomJspBagRegistryUtil getInstance() {
		return _instance;
	}

	public void destroyCustomJspBag(
			String servletContextName, CustomJspBag customJspBag)
		throws Exception {

		String customJspDir = customJspBag.getCustomJspDir();
		boolean customJspGlobal = customJspBag.isCustomJspGlobal();
		List<String> customJsps = customJspBag.getCustomJsps();

		String portalWebDir = PortalUtil.getPortalWebDir();

		for (String customJsp : customJsps) {
			int pos = customJsp.indexOf(customJspDir);

			String portalJsp = customJsp.substring(pos + customJspDir.length());

			if (customJspGlobal) {
				File portalJspFile = new File(portalWebDir + portalJsp);
				File portalJspBackupFile = getPortalJspBackupFile(
					portalJspFile);

				if (portalJspBackupFile.exists()) {
					FileUtil.copyFile(portalJspBackupFile, portalJspFile);

					portalJspBackupFile.delete();
				}
				else if (portalJspFile.exists()) {
					portalJspFile.delete();
				}
			}
			else {
				portalJsp = CustomJspRegistryUtil.getCustomJspFileName(
					servletContextName, portalJsp);

				File portalJspFile = new File(portalWebDir + portalJsp);

				if (portalJspFile.exists()) {
					portalJspFile.delete();
				}
			}
		}

		if (!customJspGlobal) {
			CustomJspRegistryUtil.unregisterServletContextName(
				servletContextName);
		}
	}

	public void getCustomJsps(
		ServletContext servletContext, String resourcePath,
		List<String> customJsps) {

		Set<String> resourcePaths = servletContext.getResourcePaths(
			resourcePath);

		if ((resourcePaths == null) || resourcePaths.isEmpty()) {
			return;
		}

		for (String curResourcePath : resourcePaths) {
			if (curResourcePath.endsWith(StringPool.SLASH)) {
				getCustomJsps(servletContext, curResourcePath, customJsps);
			}
			else {
				String customJsp = curResourcePath;

				customJsp = StringUtil.replace(
					customJsp, StringPool.DOUBLE_SLASH, StringPool.SLASH);

				customJsps.add(customJsp);
			}
		}
	}

	public String getPortalJsp(String customJsp, String customJspDir) {
		if (Validator.isNull(customJsp) || Validator.isNull(customJspDir)) {
			return null;
		}

		int pos = customJsp.indexOf(customJspDir);

		return customJsp.substring(pos + customJspDir.length());
	}

	public File getPortalJspBackupFile(File portalJspFile) {
		String fileName = portalJspFile.getName();
		String filePath = portalJspFile.toString();

		int fileNameIndex = fileName.lastIndexOf(CharPool.PERIOD);

		if (fileNameIndex > 0) {
			int filePathIndex = filePath.lastIndexOf(fileName);

			fileName =
				fileName.substring(0, fileNameIndex) + ".portal" +
					fileName.substring(fileNameIndex);

			filePath = filePath.substring(0, filePathIndex) + fileName;
		}
		else {
			filePath += ".portal";
		}

		return new File(filePath);
	}

	public void initCustomJspBag(
			String servletContextName, String displayName,
			CustomJspBag customJspBag)
		throws Exception {

		String customJspDir = customJspBag.getCustomJspDir();
		boolean customJspGlobal = customJspBag.isCustomJspGlobal();
		List<String> customJsps = customJspBag.getCustomJsps();

		String portalWebDir = PortalUtil.getPortalWebDir();

		for (String customJsp : customJsps) {
			String portalJsp = getPortalJsp(customJsp, customJspDir);

			if (customJspGlobal) {
				File portalJspFile = new File(portalWebDir + portalJsp);
				File portalJspBackupFile = getPortalJspBackupFile(
					portalJspFile);

				if (portalJspFile.exists() && !portalJspBackupFile.exists()) {
					FileUtil.copyFile(portalJspFile, portalJspBackupFile);
				}
			}
			else {
				portalJsp = CustomJspRegistryUtil.getCustomJspFileName(
					servletContextName, portalJsp);
			}

			FileUtil.write(
				portalWebDir + portalJsp,
				customJspBag.getCustomJspInputStream(customJsp));
		}

		if (!customJspGlobal) {
			CustomJspRegistryUtil.registerServletContextName(
				servletContextName, displayName);
		}
	}

	public void initCustomJspDir(
			ServletContext servletContext, String servletContextName,
			ClassLoader portletClassLoader, PluginPackage pluginPackage,
			Element rootElement)
		throws Exception {

		String customJspDir = rootElement.elementText("custom-jsp-dir");

		if (Validator.isNull(customJspDir)) {
			return;
		}

		if (_log.isDebugEnabled()) {
			_log.debug("Custom JSP directory: " + customJspDir);
		}

		boolean customJspGlobal = GetterUtil.getBoolean(
			rootElement.elementText("custom-jsp-global"), true);

		List<String> customJsps = new ArrayList<>();

		getCustomJsps(servletContext, customJspDir, customJsps);

		if (customJsps.isEmpty()) {
			return;
		}

		CustomJspBag customJspBag = new CustomJspBag(
			servletContext, customJspDir, customJspGlobal, customJsps);

		if (_log.isDebugEnabled()) {
			StringBundler sb = new StringBundler(customJsps.size() * 2);

			sb.append("Custom JSP files:\n");

			for (int i = 0; i < customJsps.size(); i++) {
				String customJsp = customJsps.get(0);

				sb.append(customJsp);

				if ((i + 1) < customJsps.size()) {
					sb.append(StringPool.NEW_LINE);
				}
			}

			Log log = SanitizerLogWrapper.allowCRLF(_log);

			log.debug(sb.toString());
		}

		if (customJspGlobal && !_customJspBagsMap.isEmpty() &&
			!_customJspBagsMap.containsKey(servletContextName)) {

			verifyCustomJsps(servletContextName, customJspBag);
		}

		_customJspBagsMap.put(servletContextName, customJspBag);

		initCustomJspBag(
			servletContextName, pluginPackage.getName(), customJspBag);
	}

	public CustomJspBag remove(String servletContextName) {
		return _customJspBagsMap.remove(servletContextName);
	}

	public void verifyCustomJsps(
			String servletContextName, CustomJspBag customJspBag)
		throws DuplicateCustomJspException {

		Set<String> customJsps = new HashSet<>();

		for (String customJsp : customJspBag.getCustomJsps()) {
			String portalJsp = getPortalJsp(
				customJsp, customJspBag.getCustomJspDir());

			customJsps.add(portalJsp);
		}

		Map<String, String> conflictingCustomJsps = new HashMap<>();

		for (Map.Entry<String, CustomJspBag> entry :
				_customJspBagsMap.entrySet()) {

			CustomJspBag currentCustomJspBag = entry.getValue();

			if (!currentCustomJspBag.isCustomJspGlobal()) {
				continue;
			}

			String currentServletContextName = entry.getKey();

			List<String> currentCustomJsps =
				currentCustomJspBag.getCustomJsps();

			for (String currentCustomJsp : currentCustomJsps) {
				String currentPortalJsp = getPortalJsp(
					currentCustomJsp, currentCustomJspBag.getCustomJspDir());

				if (customJsps.contains(currentPortalJsp)) {
					conflictingCustomJsps.put(
						currentPortalJsp, currentServletContextName);
				}
			}
		}

		if (conflictingCustomJsps.isEmpty()) {
			return;
		}

		_log.error(servletContextName + " conflicts with the installed hooks");

		if (_log.isDebugEnabled()) {
			Log log = SanitizerLogWrapper.allowCRLF(_log);

			StringBundler sb = new StringBundler(
				conflictingCustomJsps.size() * 4 + 2);

			sb.append("Colliding JSP files in ");
			sb.append(servletContextName);
			sb.append(StringPool.NEW_LINE);

			int i = 0;

			for (Map.Entry<String, String> entry :
					conflictingCustomJsps.entrySet()) {

				sb.append(entry.getKey());
				sb.append(" with ");
				sb.append(entry.getValue());

				if ((i + 1) < conflictingCustomJsps.size()) {
					sb.append(StringPool.NEW_LINE);
				}

				i++;
			}

			log.debug(sb.toString());
		}

		throw new DuplicateCustomJspException();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CustomJspBagRegistryUtil.class);

	private static final CustomJspBagRegistryUtil _instance =
		new CustomJspBagRegistryUtil();

	private final Map<String, CustomJspBag> _customJspBagsMap = new HashMap<>();

}