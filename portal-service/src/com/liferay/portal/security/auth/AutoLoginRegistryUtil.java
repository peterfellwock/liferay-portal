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
 * @autoor Peter Fellwock
 */
public class AutoLoginRegistryUtil {

	public static AutoLogin getAutoLogin(String classname) {
		return _instance._getAutoLogin(classname);
	}
	
	public static AutoLogin getAutoLogin() {
		return _instance._serviceTracker.getService();
	}

	public static Map<String, AutoLogin> getAutoLogins() {
		return _instance._getAutoLogins();
	}

	public static void register(String path, AutoLogin autoLogin) {
		_instance._register(path, autoLogin);
	}

	public static void unregister(String path) {
		_instance._unregister(path);
	}

	private AutoLoginRegistryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		Filter filter = registry.getFilter(
			"(objectClass=" + AutoLogin.class.getName() + ")");

		_serviceTracker = registry.trackServices(
			filter, new AutoLoginServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private AutoLogin _getAutoLogin(String classname) {

		
		System.out.println("-----------------------------------------------------> LOOKING FOR:" + classname);
		AutoLogin autoLogin = _tokens.get(classname);

		if (autoLogin != null) {
			return autoLogin;
		}
		System.out.println("-----------------------------------------------------> NOT TOKEN FOUND: " + classname);
		for (Map.Entry<String, AutoLogin> entry : _tokens.entrySet()) {
			if (classname.startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!> OH SNAP, could not find:" + classname);
		return null;
	}

	private Map<String, AutoLogin> _getAutoLogins() {
		return _tokens;
	}

	private void _register(String path, AutoLogin autoLogin) {
		Registry registry = RegistryUtil.getRegistry();

		Map<String, Object> properties = new HashMap<String, Object>();

		properties.put("path", path);

		ServiceRegistration<AutoLogin> serviceRegistration =
			registry.registerService(
				AutoLogin.class, autoLogin, properties);

		_autoLoginServiceRegistrations.put(path, serviceRegistration);
	}

	private void _unregister(String path) {
		ServiceRegistration<?> serviceRegistration =
				_autoLoginServiceRegistrations.remove(path);

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}

		serviceRegistration = _autoLoginServiceRegistrations.remove(
			path);

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	private static AutoLoginRegistryUtil _instance =
		new AutoLoginRegistryUtil();

	private Map<String, AutoLogin> _tokens =
		new ConcurrentHashMap<String, AutoLogin>();
	private ServiceTracker<?, AutoLogin> _serviceTracker;
	private StringServiceRegistrationMap<AutoLogin>
		_autoLoginServiceRegistrations =
			new StringServiceRegistrationMap<AutoLogin>();

	private class AutoLoginServiceTrackerCustomizer
		implements ServiceTrackerCustomizer<Object, AutoLogin> {

		@Override
		public AutoLogin addingService(ServiceReference<Object> serviceReference) {
			Registry registry = RegistryUtil.getRegistry();

			Object service = registry.getService(serviceReference);
			
			AutoLogin autoLogin = (AutoLogin) service;
			
			String path = autoLogin.getClass().getName();
			
			_tokens.put(path, autoLogin);

			return autoLogin;
		}

		@Override
		public void modifiedService(
			ServiceReference<Object> serviceReference, AutoLogin service) {
		}

		@Override
		public void removedService(
			ServiceReference<Object> serviceReference, AutoLogin service) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			String path = (String)serviceReference.getProperty("path");

			_tokens.remove(path);
		}

	}

}