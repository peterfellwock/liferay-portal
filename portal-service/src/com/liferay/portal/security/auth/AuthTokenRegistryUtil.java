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

package com.liferay.portal.security.auth;

import com.liferay.registry.Filter;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.collections.StringServiceRegistrationMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Peter Fellwock
 */
public class AuthTokenRegistryUtil {

	public static AuthToken getAuthToken(String path) {
		return _instance._getAuthToken(path);
	}
	
	public static AuthToken getAuthToken() {
		return _instance._serviceTracker.getService();
	}

	public static Map<String, AuthToken> getAuthTokens() {
		return _instance._getAuthTokens();
	}

	public static void register(String path, AuthToken authToken) {
		_instance._register(path, authToken);
	}

	public static void unregister(String path) {
		_instance._unregister(path);
	}

	private AuthTokenRegistryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		Filter filter = registry.getFilter(
			"(objectClass=" + AuthToken.class.getName() + ")");

		_serviceTracker = registry.trackServices(
			filter, new AuthTokenServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private AuthToken _getAuthToken(String path) {
		AuthToken authToken = _tokens.get(path);

		if (authToken != null) {
			return authToken;
		}

		for (Map.Entry<String, AuthToken> entry : _tokens.entrySet()) {
			if (path.startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}

		return null;
	}

	private Map<String, AuthToken> _getAuthTokens() {
		return _tokens;
	}

	private void _register(String path, AuthToken authToken) {
		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<String, Object>();

		properties.put("path", path);

		ServiceRegistration<AuthToken> serviceRegistration =
			registry.registerService(
				AuthToken.class, authToken, properties);

		_authTokenServiceRegistrations.put(path, serviceRegistration);
	}

	private void _unregister(String path) {
		ServiceRegistration<?> serviceRegistration =
				_authTokenServiceRegistrations.remove(path);

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}

		serviceRegistration = _authTokenServiceRegistrations.remove(
			path);

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	private static AuthTokenRegistryUtil _instance =
		new AuthTokenRegistryUtil();

	private Map<String, AuthToken> _tokens =
		new ConcurrentHashMap<String, AuthToken>();
	private ServiceTracker<?, AuthToken> _serviceTracker;
	private StringServiceRegistrationMap<AuthToken>
		_authTokenServiceRegistrations =
			new StringServiceRegistrationMap<AuthToken>();

	private class AuthTokenServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Object, AuthToken> {

		@Override
		public AuthToken addingService(ServiceReference<Object> serviceReference) {
			Registry registry = RegistryUtil.getRegistry();

			Object service = registry.getService(serviceReference);

			AuthToken authToken = (AuthToken) service;


			String path = (String)serviceReference.getProperty("path");

			_tokens.put(path, authToken);

			return authToken;
		}

		@Override
		public void modifiedService(
			ServiceReference<Object> serviceReference, AuthToken service) {
		}

		@Override
		public void removedService(
			ServiceReference<Object> serviceReference, AuthToken service) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			String path = (String)serviceReference.getProperty("path");

			_tokens.remove(path);
		}

	}

}