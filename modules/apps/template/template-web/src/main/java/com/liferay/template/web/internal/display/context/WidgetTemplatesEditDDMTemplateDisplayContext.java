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

package com.liferay.template.web.internal.display.context;

import com.liferay.dynamic.data.mapping.configuration.DDMWebConfiguration;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil;

/**
 * @author Eudaldo Alonso
 * @author Lourdes Fernández Besada
 */
public class WidgetTemplatesEditDDMTemplateDisplayContext
	extends EditDDMTemplateDisplayContext {

	public WidgetTemplatesEditDDMTemplateDisplayContext(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		super(liferayPortletRequest, liferayPortletResponse);

		_ddmWebConfiguration =
			(DDMWebConfiguration)liferayPortletRequest.getAttribute(
				DDMWebConfiguration.class.getName());
	}

	@Override
	public boolean autogenerateTemplateKey() {
		return _ddmWebConfiguration.autogenerateTemplateKey();
	}

	@Override
	protected String getDefaultScript() {
		TemplateHandler templateHandler =
			TemplateHandlerRegistryUtil.getTemplateHandler(getClassNameId());

		if (templateHandler != null) {
			return templateHandler.getTemplatesHelpContent(getLanguageType());
		}

		return "<#-- Empty script -->";
	}

	private final DDMWebConfiguration _ddmWebConfiguration;

}