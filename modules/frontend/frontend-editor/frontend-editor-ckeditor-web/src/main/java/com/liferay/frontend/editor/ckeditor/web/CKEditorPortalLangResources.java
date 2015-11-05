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

package com.liferay.frontend.editor.ckeditor.web;

import com.liferay.frontend.editor.lang.FrontendEditorPortalLangResources;
import com.liferay.portal.kernel.servlet.PortalLangResources;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Michael Bradford
 */
@Component(immediate = true, service = PortalLangResources.class)
public class CKEditorPortalLangResources implements PortalLangResources {

	public Class getLangResourceBundleClass() {
		return FrontendEditorPortalLangResources.class;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundle = bundleContext.getBundle();
	}

	private Bundle _bundle;

}