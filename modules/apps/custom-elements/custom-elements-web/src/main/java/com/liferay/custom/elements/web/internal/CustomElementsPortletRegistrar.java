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

package com.liferay.custom.elements.web.internal;

import com.liferay.custom.elements.model.CustomElementsPortletDescriptor;
import com.liferay.custom.elements.service.CustomElementsPortletDescriptorLocalService;
import com.liferay.custom.elements.web.internal.portlet.CustomElementsPortlet;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Iván Zaera Avellón
 */
@Component(immediate = true, service = CustomElementsPortletRegistrar.class)
public class CustomElementsPortletRegistrar {

	public void registerPortlet(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		_registerPortlet(customElementsPortletDescriptor);
	}

	public void unregisterPortlet(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		_unregisterPortlet(
			customElementsPortletDescriptor.
				getCustomElementsPortletDescriptorId());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		if (_log.isInfoEnabled()) {
			_log.info("Registering custom elements portlets");
		}

		for (CustomElementsPortletDescriptor customElementsPortletDescriptor :
				_customElementsPortletDescriptorLocalService.
					getCustomElementsPortletDescriptors(
						QueryUtil.ALL_POS, QueryUtil.ALL_POS)) {

			registerPortlet(customElementsPortletDescriptor);
		}
	}

	@Deactivate
	protected void deactivate() {
		if (_log.isInfoEnabled()) {
			_log.info("Unregistering custom elements portlets");
		}

		for (long customElementsPortletDescriptorId :
				_customElementsPortlets.keySet()) {

			_unregisterPortlet(customElementsPortletDescriptorId);
		}
	}

	private void _registerPortlet(
		CustomElementsPortletDescriptor customElementsPortletDescriptor) {

		CustomElementsPortlet customElementsPortlet = new CustomElementsPortlet(
			customElementsPortletDescriptor);

		long customElementsPortletDescriptorId =
			customElementsPortletDescriptor.
				getCustomElementsPortletDescriptorId();

		CustomElementsPortlet existingCustomElementsPortlet =
			_customElementsPortlets.putIfAbsent(
				customElementsPortletDescriptorId, customElementsPortlet);

		if (existingCustomElementsPortlet != null) {
			throw new IllegalStateException(
				"Custom elements portlet " + customElementsPortletDescriptorId +
					" is already registered");
		}

		customElementsPortlet.register(_bundleContext);

		if (_log.isInfoEnabled()) {
			_log.info(
				"Registered custom elements portlet " +
					customElementsPortlet.getName());
		}
	}

	private void _unregisterPortlet(long customElementsPortletDescriptorId) {
		CustomElementsPortlet customElementsPortlet =
			_customElementsPortlets.remove(customElementsPortletDescriptorId);

		if (customElementsPortlet != null) {
			customElementsPortlet.unregister();

			if (_log.isInfoEnabled()) {
				_log.info(
					"Unregistered custom elements portlet " +
						customElementsPortlet.getName());
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CustomElementsPortletRegistrar.class);

	private BundleContext _bundleContext;

	@Reference
	private CustomElementsPortletDescriptorLocalService
		_customElementsPortletDescriptorLocalService;

	private final ConcurrentMap<Long, CustomElementsPortlet>
		_customElementsPortlets = new ConcurrentHashMap<>();

}