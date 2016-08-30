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

package com.liferay.portlet;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutType;
import com.liferay.portal.kernel.model.LayoutTypeAccessPolicy;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.ActionResult;
import com.liferay.portal.kernel.portlet.PortletContainer;
import com.liferay.portal.kernel.portlet.PortletContainerException;
import com.liferay.portal.kernel.portlet.PortletContainerUtil;
import com.liferay.portal.kernel.resiliency.spi.SPIUtil;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.security.auth.AuthTokenWhitelistUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.pacl.DoPrivileged;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.TempAttributesServletRequest;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.util.PropsValues;

import java.util.List;
import java.util.Map;

import javax.portlet.Event;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tomas Polesovsky
 * @author Raymond Augé
 */
@DoPrivileged
public class SecurityPortletContainerWrapper implements PortletContainer {

	public static PortletContainer createSecurityPortletContainerWrapper(
		PortletContainer portletContainer) {

		if (!SPIUtil.isSPI()) {
			portletContainer = new SecurityPortletContainerWrapper(
				portletContainer);
		}

		return portletContainer;
	}

	public SecurityPortletContainerWrapper(PortletContainer portletContainer) {
		_portletContainer = portletContainer;
	}

	@Override
	public void preparePortlet(HttpServletRequest request, Portlet portlet)
		throws PortletContainerException {

		_portletContainer.preparePortlet(request, portlet);
	}

	@Override
	public ActionResult processAction(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		System.out.println(
			":::SecurityPortletContainerWrapper.processAction#" + 1);

		try {
			HttpServletRequest ownerLayoutRequest =
				getOwnerLayoutRequestWrapper(request, portlet);
			System.out.println(
				":::SecurityPortletContainerWrapper.processAction#" + 2);

			checkAction(ownerLayoutRequest, portlet);
			System.out.println(
				":::SecurityPortletContainerWrapper.processAction#" + 3);

			ActionResult ar = _portletContainer.processAction(
				request, response, portlet);

			System.out.println(
				":::SecurityPortletContainerWrapper.processAction#" + 4);

			return ar;
		}
		catch (PrincipalException pe) {
			return processActionException(request, response, portlet, pe);
		}
		catch (PortletContainerException pce) {
			throw pce;
		}
		catch (Exception e) {
			throw new PortletContainerException(e);
		}
	}

	@Override
	public List<Event> processEvent(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet, Layout layout, Event event)
		throws PortletContainerException {

		return _portletContainer.processEvent(
			request, response, portlet, layout, event);
	}

	@Override
	public void render(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		try {
			checkRender(request, portlet);

			_portletContainer.render(request, response, portlet);
		}
		catch (PrincipalException pe) {
			processRenderException(request, response, portlet);
		}
		catch (PortletContainerException pce) {
			throw pce;
		}
		catch (Exception e) {
			throw new PortletContainerException(e);
		}
	}

	@Override
	public void serveResource(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		try {
			HttpServletRequest ownerLayoutRequest =
				getOwnerLayoutRequestWrapper(request, portlet);

			checkResource(ownerLayoutRequest, portlet);

			_portletContainer.serveResource(request, response, portlet);
		}
		catch (PrincipalException pe) {
			processServeResourceException(request, response, portlet, pe);
		}
		catch (PortletContainerException pce) {
			throw pce;
		}
		catch (Exception e) {
			throw new PortletContainerException(e);
		}
	}

	protected void check(HttpServletRequest request, Portlet portlet)
		throws Exception {

		System.out.println(
			":::SecurityPortletContainerWrapper.check#1");

		if (portlet == null) {
			return;
		}

		System.out.println(
			":::SecurityPortletContainerWrapper.check#2");

		boolean isValid = isValidPortletId(portlet.getPortletId());

		System.out.println(
			":::SecurityPortletContainerWrapper.check#isValidPortletId(portlet.getPortletId()::" +
			isValid);

		if (!isValidPortletId(portlet.getPortletId())) {
			if (_log.isWarnEnabled()) {
				_log.warn("Invalid portlet ID " + portlet.getPortletId());
			}

			System.out.println(
				":::SecurityPortletContainerWrapper.check#NotisValidPortletId -> Invalid portlet ID");

			throw new PrincipalException(
				"Invalid portlet ID " + portlet.getPortletId());
		}

		if (portlet.isUndeployedPortlet()) {
			return;
		}

		System.out.println(":::SecurityPortletContainerWrapper.check#3");

		Layout layout = (Layout)request.getAttribute(WebKeys.LAYOUT);

		System.out.println(":::SecurityPortletContainerWrapper.check#4");

		LayoutType layoutType = layout.getLayoutType();

		System.out.println(":::SecurityPortletContainerWrapper.check#5");

		LayoutTypeAccessPolicy layoutTypeAccessPolicy =
			layoutType.getLayoutTypeAccessPolicy();

		System.out.println(":::SecurityPortletContainerWrapper.check#6");

		layoutTypeAccessPolicy.checkAccessAllowedToPortlet(
			request, layout, portlet);

		System.out.println(":::SecurityPortletContainerWrapper.check#7");

	}

	protected void checkAction(HttpServletRequest request, Portlet portlet)
		throws Exception {

		System.out.println(
			":::SecurityPortletContainerWrapper.checkAction#" + 1);

		checkCSRFProtection(request, portlet);

		System.out.println(
			":::SecurityPortletContainerWrapper.checkAction#" + 2);

		check(request, portlet);

		System.out.println(
			":::SecurityPortletContainerWrapper.checkAction#" + 3);
	}

	protected void checkCSRFProtection(
			HttpServletRequest request, Portlet portlet)
		throws PortalException {

		System.out.println(
			":::SecurityPortletContainerWrapper.checkCSRFProtection#" + 1);

		Map<String, String> initParams = portlet.getInitParams();

		System.out.println(
			":::SecurityPortletContainerWrapper.checkCSRFProtection#" + 2);

		boolean checkAuthToken = GetterUtil.getBoolean(
			initParams.get("check-auth-token"), true);

		System.out.println(
			":::SecurityPortletContainerWrapper.checkCSRFProtection#checkAuthToken" +
				checkAuthToken);

		System.out.println(
			":::SecurityPortletContainerWrapper.checkCSRFProtection#" + 3);

		boolean isP = AuthTokenWhitelistUtil.isPortletCSRFWhitelisted(
			request, portlet);

		System.out.println(
			":::SecurityPortletContainerWrapper.checkCSRFProtection#isPortletCSRFWhitelisted" +
				isP);

		if (isP) {
			checkAuthToken = false;
		}

		System.out.println(
			":::SecurityPortletContainerWrapper.checkCSRFProtection#checkAuthToken2" +
				checkAuthToken);

		if (checkAuthToken) {
			System.out.println(
				":::SecurityPortletContainerWrapper.checkCSRFProtection#inner#" +
					1);

			AuthTokenUtil.checkCSRFToken(
				request, SecurityPortletContainerWrapper.class.getName());

			System.out.println(
				":::SecurityPortletContainerWrapper.checkCSRFProtection#inner#" +
					1);
		}
	}

	protected void checkRender(HttpServletRequest request, Portlet portlet)
		throws Exception {

		check(request, portlet);
	}

	protected void checkResource(HttpServletRequest request, Portlet portlet)
		throws Exception {

		check(request, portlet);
	}

	protected String getOriginalURL(HttpServletRequest request) {
		LastPath lastPath = (LastPath)request.getAttribute(WebKeys.LAST_PATH);

		if (lastPath == null) {
			return String.valueOf(request.getRequestURI());
		}

		String portalURL = PortalUtil.getPortalURL(request);

		return portalURL.concat(
			lastPath.getContextPath()).concat(lastPath.getPath());
	}

	protected HttpServletRequest getOwnerLayoutRequestWrapper(
			HttpServletRequest request, Portlet portlet)
		throws Exception {

		if (!PropsValues.PORTLET_EVENT_DISTRIBUTION_LAYOUT_SET ||
			PropsValues.PORTLET_CROSS_LAYOUT_INVOCATION_MODE.equals("render")) {

			return request;
		}

		Layout ownerLayout = null;
		LayoutTypePortlet ownerLayoutTypePortlet = null;

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout requestLayout = (Layout)request.getAttribute(WebKeys.LAYOUT);

		List<LayoutTypePortlet> layoutTypePortlets =
			PortletContainerUtil.getLayoutTypePortlets(requestLayout);

		for (LayoutTypePortlet layoutTypePortlet : layoutTypePortlets) {
			if (layoutTypePortlet.hasPortletId(portlet.getPortletId())) {
				ownerLayoutTypePortlet = layoutTypePortlet;

				ownerLayout = layoutTypePortlet.getLayout();

				break;
			}
		}

		if (ownerLayout == null) {
			return request;
		}

		Layout currentLayout = themeDisplay.getLayout();

		if (currentLayout.equals(ownerLayout)) {
			return request;
		}

		ThemeDisplay themeDisplayClone = (ThemeDisplay)themeDisplay.clone();

		themeDisplayClone.setLayout(ownerLayout);
		themeDisplayClone.setLayoutTypePortlet(ownerLayoutTypePortlet);

		TempAttributesServletRequest tempAttributesServletRequest =
			new TempAttributesServletRequest(request);

		tempAttributesServletRequest.setTempAttribute(
			WebKeys.LAYOUT, ownerLayout);
		tempAttributesServletRequest.setTempAttribute(
			WebKeys.THEME_DISPLAY, themeDisplayClone);

		return tempAttributesServletRequest;
	}

	protected boolean isValidPortletId(String portletId) {
		for (int i = 0; i < portletId.length(); i++) {
			char c = portletId.charAt(i);

			if ((c >= CharPool.LOWER_CASE_A) && (c <= CharPool.LOWER_CASE_Z)) {
				continue;
			}

			if ((c >= CharPool.UPPER_CASE_A) && (c <= CharPool.UPPER_CASE_Z)) {
				continue;
			}

			if ((c >= CharPool.NUMBER_0) && (c <= CharPool.NUMBER_9)) {
				continue;
			}

			if ((c == CharPool.POUND) || (c == CharPool.UNDERLINE)) {
				continue;
			}

			return false;
		}

		return true;
	}

	protected ActionResult processActionException(
		HttpServletRequest request, HttpServletResponse response,
		Portlet portlet, PrincipalException pe) {

		System.out.println("----------------------------------------------");
		System.out.println(pe.getMessage());
		pe.printStackTrace(System.out);
		System.out.println("----------------------------------------------");

		if (_log.isDebugEnabled()) {
			_log.debug(pe);
		}

		String url = getOriginalURL(request);

		if (_log.isWarnEnabled()) {
			_log.warn(
				String.format(
					"User %s is not allowed to access URL %s and portlet %s",
					PortalUtil.getUserId(request), url,
					portlet.getPortletId()));
		}

		return ActionResult.EMPTY_ACTION_RESULT;
	}

	protected void processRenderException(
			HttpServletRequest request, HttpServletResponse response,
			Portlet portlet)
		throws PortletContainerException {

		String portletContent = null;

		if (portlet.isShowPortletAccessDenied()) {
			portletContent = "/html/portal/portlet_access_denied.jsp";
		}

		try {
			if (portletContent != null) {
				RequestDispatcher requestDispatcher =
					request.getRequestDispatcher(portletContent);

				requestDispatcher.include(request, response);
			}
		}
		catch (Exception e) {
			throw new PortletContainerException(e);
		}
	}

	protected void processServeResourceException(
		HttpServletRequest request, HttpServletResponse response,
		Portlet portlet, PrincipalException pe) {

		if (_log.isDebugEnabled()) {
			_log.debug(pe);
		}

		String url = getOriginalURL(request);

		response.setHeader(
			HttpHeaders.CACHE_CONTROL,
			HttpHeaders.CACHE_CONTROL_NO_CACHE_VALUE);

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

		if (_log.isWarnEnabled()) {
			_log.warn(
				"Reject serveResource for " + url + " on " +
					portlet.getPortletId());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SecurityPortletContainerWrapper.class);

	private final PortletContainer _portletContainer;

}