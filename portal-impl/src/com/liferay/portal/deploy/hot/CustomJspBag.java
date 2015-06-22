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

import java.io.InputStream;

import java.util.List;

import javax.servlet.ServletContext;
public class CustomJspBag {

	public CustomJspBag(
		ServletContext servletContext, String customJspDir,
		boolean customJspGlobal, List<String> customJsps) {

		_servletContext = servletContext;
		_customJspDir = customJspDir;
		_customJspGlobal = customJspGlobal;
		_customJsps = customJsps;
	}

	public String getCustomJspDir() {
		return _customJspDir;
	}

	public InputStream getCustomJspInputStream(String customJsp) {
		return _servletContext.getResourceAsStream(customJsp);
	}

	public List<String> getCustomJsps() {
		return _customJsps;
	}

	public boolean isCustomJspGlobal() {
		return _customJspGlobal;
	}

	private final String _customJspDir;
	private final boolean _customJspGlobal;
	private final List<String> _customJsps;
	private final ServletContext _servletContext;

}