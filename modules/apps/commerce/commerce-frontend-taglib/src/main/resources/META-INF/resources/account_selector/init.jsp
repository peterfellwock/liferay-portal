<%--
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
--%>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@ page import="com.liferay.commerce.frontend.taglib.internal.model.CurrentCommerceAccountModel" %><%@
page import="com.liferay.commerce.frontend.taglib.internal.model.CurrentCommerceOrderModel" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.json.JSONFactoryUtil" %><%@
page import="com.liferay.portal.kernel.json.JSONSerializer" %><%@
page import="com.liferay.portal.kernel.util.PortalUtil" %><%@
page import="com.liferay.portal.kernel.util.Validator" %>

<liferay-theme:defineObjects />

<%
long channelId = (long)request.getAttribute("liferay-commerce:account-selector:channelId");
String createNewOrderURL = (String)request.getAttribute("liferay-commerce:account-selector:createNewOrderURL");
CurrentCommerceAccountModel currentAccount = (CurrentCommerceAccountModel)request.getAttribute("liferay-commerce:account-selector:currentAccount");
CurrentCommerceOrderModel currentOrder = (CurrentCommerceOrderModel)request.getAttribute("liferay-commerce:account-selector:currentOrder");
JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();
String selectOrderURL = (String)request.getAttribute("liferay-commerce:account-selector:selectOrderURL");
String setCurrentAccountURL = (String)request.getAttribute("liferay-commerce:account-selector:setCurrentAccountURL");
String spritemap = (String)request.getAttribute("liferay-commerce:account-selector:spritemap");

String randomNamespace = PortalUtil.generateRandomKey(request, "taglib_account_selector") + StringPool.UNDERLINE;

String accountSelectorId = randomNamespace + "account-selector";
%>