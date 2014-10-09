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

package com.liferay.page.comments.web.portlet;

import com.liferay.page.comments.web.upgrade.PageCommentsUpgrade;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
* @author Raymond Augé
* @author Peter Fellwock
*/
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.css-class-wrapper=page-comments",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.icon=/icons/page_comments.png",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.struts-path=page_comments",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Comments",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=guest,power-user,user"
	},
	service = Portlet.class
)
public class PageCommentsPortlet extends MVCPortlet {

	@Reference(unbind = "-")
	protected void setPageCommentsUpgrade(
		PageCommentsUpgrade pageCommentsUpgrade) {
	}

}

/**
<user-notification-definitions>com/liferay/portlet/comments/comments-user-notification-definitions.xml</user-notification-definitions>
<user-notification-handler-class>com.liferay.portlet.comments.notifications.CommentsUserNotificationHandler</user-notification-handler-class>
 **/