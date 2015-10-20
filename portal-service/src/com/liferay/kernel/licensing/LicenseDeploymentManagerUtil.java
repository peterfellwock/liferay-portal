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

package com.liferay.kernel.licensing;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;
import com.liferay.portal.license.LicenseInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Amos Fong
 */
public class LicenseDeploymentManagerUtil {

	/**
	 * @deprecated As of 6.2.0, replaced by {@link LicenseDeploymentManager#STATE_ABSENT}
	 */
	@Deprecated
	public static final int STATE_ABSENT = LicenseDeploymentManager.STATE_ABSENT;

	/**
	 * @deprecated As of 6.2.0, replaced by {@link LicenseDeploymentManager#STATE_EXPIRED}
	 */
	@Deprecated
	public static final int STATE_EXPIRED = LicenseDeploymentManager.STATE_EXPIRED;

	/**
	 * @deprecated As of 6.2.0, replaced by {@link LicenseDeploymentManager#STATE_GOOD}
	 */
	@Deprecated
	public static final int STATE_GOOD = LicenseDeploymentManager.STATE_GOOD;

	/**
	 * @deprecated As of 6.2.0, replaced by {@link
	 *             LicenseDeploymentManager#STATE_INACTIVE}
	 */
	@Deprecated
	public static final int STATE_INACTIVE = LicenseDeploymentManager.STATE_INACTIVE;

	/**
	 * @deprecated As of 6.2.0, replaced by {@link LicenseDeploymentManager#STATE_INVALID}
	 */
	@Deprecated
	public static final int STATE_INVALID = LicenseDeploymentManager.STATE_INVALID;

	/**
	 * @deprecated As of 6.2.0, replaced by {@link
	 *             LicenseDeploymentManager#STATE_OVERLOAD}
	 */
	@Deprecated
	public static final int STATE_OVERLOAD = LicenseDeploymentManager.STATE_OVERLOAD;

	public static void checkLicense(String productId) {
		getLicenseDeploymentManager().checkLicense(productId);
	}

	public static List<Map<String, String>> getClusterLicenseProperties(
		String clusterNodeId) {

		return getLicenseDeploymentManager().getClusterLicenseProperties(clusterNodeId);
	}

	public static String getHostName() {
		return getLicenseDeploymentManager().getHostName();
	}

	public static Set<String> getIpAddresses() {
		return getLicenseDeploymentManager().getIpAddresses();
	}

	public static LicenseInfo getLicenseInfo(String productId) {
		return getLicenseDeploymentManager().getLicenseInfo(productId);
	}

	public static LicenseDeploymentManager getLicenseDeploymentManager() {
		PortalRuntimePermission.checkGetBeanProperty(LicenseDeploymentManagerUtil.class);

		return _LicenseDeploymentManager;
	}

	public static List<Map<String, String>> getLicenseProperties() {
		return getLicenseDeploymentManager().getLicenseProperties();
	}

	public static Map<String, String> getLicenseProperties(String productId) {
		return getLicenseDeploymentManager().getLicenseProperties(productId);
	}

	public static int getLicenseState(Map<String, String> licenseProperties) {
		return getLicenseDeploymentManager().getLicenseState(licenseProperties);
	}

	public static int getLicenseState(String productId) {
		return getLicenseDeploymentManager().getLicenseState(productId);
	}

	public static Set<String> getMacAddresses() {
		return getLicenseDeploymentManager().getMacAddresses();
	}

	public static void registerLicense(JSONObject jsonObject) throws Exception {
		getLicenseDeploymentManager().registerLicense(jsonObject);
	}

	public void setLicenseDeploymentManager(LicenseDeploymentManager LicenseDeploymentManager) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_LicenseDeploymentManager = LicenseDeploymentManager;
	}

	private static LicenseDeploymentManager _LicenseDeploymentManager;

}