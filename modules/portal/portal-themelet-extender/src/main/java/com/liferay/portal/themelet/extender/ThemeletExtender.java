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

package com.liferay.portal.themelet.extender;

import com.liferay.portal.theme.ThemeletUtil;
import com.liferay.portal.util.Portal;

import java.util.Dictionary;
import java.util.Map;

import org.apache.felix.utils.extender.AbstractExtender;
import org.apache.felix.utils.extender.Extension;
import org.apache.felix.utils.log.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Michael Bradford
 */
@Component(immediate = true)
public class ThemeletExtender extends AbstractExtender {

	@Activate
	protected void activate(
			BundleContext bundleContext, Map<String, Object> properties)
		throws Exception {

		_bundleContext = bundleContext;
		_logger = new Logger(bundleContext);

		start(bundleContext);
	}

	@Deactivate
	protected void deactivate() throws Exception {
		stop(_bundleContext);

		_bundleContext = null;
	}

	@Override
	protected void debug(Bundle bundle, String s) {
		_logger.log(Logger.LOG_DEBUG, "[" + bundle + "] " + s);
	}

	@Override
	protected Extension doCreateExtension(Bundle bundle) throws Exception {
		Dictionary<String, String> headers = bundle.getHeaders();

		if (headers.get("Themelet-Type") == null) {
			return null;
		}

		return new ThemeletExtension(bundle);
	}

	@Override
	protected void error(String s, Throwable t) {
		_logger.log(Logger.LOG_ERROR, s, t);
	}

	@Override
	protected void warn(Bundle bundle, String s, Throwable t) {
		_logger.log(Logger.LOG_WARNING, "[" + bundle + "] " + s, t);
	}

	private BundleContext _bundleContext;
	private Logger _logger;

	private class ThemeletExtension implements Extension {

		public ThemeletExtension(Bundle bundle) {
			_bundle = bundle;
		}

		@Override
		public void destroy() throws Exception {
			ThemeletUtil.unregisterThemeletContextPath(_themeletType);

			_contextPath = null;
			_themeletType = null;
		}

		@Override
		public void start() throws Exception {
			Dictionary<String, String> headers = _bundle.getHeaders();

			_themeletType = headers.get("Themelet-Type");
			_contextPath = Portal.PATH_MODULE + headers.get("Web-ContextPath");

			ThemeletUtil.registerThemeletContextPath(
				_themeletType, _contextPath);
		}

		private final Bundle _bundle;
		private String _contextPath;
		private String _themeletType;

	}

}