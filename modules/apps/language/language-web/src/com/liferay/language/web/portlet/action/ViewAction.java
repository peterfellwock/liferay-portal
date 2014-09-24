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

package com.liferay.language.web.portlet.action;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroupRole;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.UserService;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.bridges.mvc.ActionCommand;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	immediate = true,
	property = {
		"action.command.name=view",
		"javax.portlet.name=com_liferay_language_web_portlet_LanguagePortlet"
	},
	service = ActionCommand.class
)
public class ViewAction implements ActionCommand {

	public static String getUpdateUserPassword(
		HttpServletRequest request, long userId) {

		String password = PortalUtil.getUserPassword(request);

		if (userId != PortalUtil.getUserId(request)) {
			password = StringPool.BLANK;
		}

		if (password == null) {
			password = StringPool.BLANK;
		}

		return password;
	}

	@Override
	public boolean processCommand(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws PortletException {

		HttpServletResponse httpServletResponse =
			PortalUtil.getHttpServletResponse(portletResponse);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		HttpSession httpSession = httpServletRequest.getSession();

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String languageId = ParamUtil.getString(portletRequest, "languageId");

		Locale locale = LocaleUtil.fromLanguageId(languageId);

		List<Locale> availableLocales = ListUtil.fromArray(
			LanguageUtil.getAvailableLocales(themeDisplay.getSiteGroupId()));

		if (availableLocales.contains(locale)) {
			boolean persistState = ParamUtil.getBoolean(
				portletRequest, "persistState", true);

			if (themeDisplay.isSignedIn() && (persistState)) {
				User user = themeDisplay.getUser();

				try {
					Contact contact = user.getContact();

					updateUser(
						httpServletRequest, user.getUserId(),
						user.getScreenName(), user.getEmailAddress(),
						user.getFacebookId(), user.getOpenId(), languageId,
						user.getTimeZoneId(), user.getGreeting(),
						user.getComments(), contact.getSmsSn(),
						contact.getAimSn(), contact.getFacebookSn(),
						contact.getIcqSn(), contact.getJabberSn(),
						contact.getMsnSn(), contact.getMySpaceSn(),
						contact.getSkypeSn(), contact.getTwitterSn(),
						contact.getYmSn());
				}
				catch (Exception e) {
						if (_log.isWarnEnabled()) {
							_log.warn(e.getMessage());
						}
					}
			}

			httpSession.setAttribute(_STRUTS_LOCALE_KEY, locale);

			LanguageUtil.updateCookie(httpServletRequest, httpServletResponse, locale);
		}

		return true;
	}

	public void updateUser(
			HttpServletRequest request, long userId, String screenName,
			String emailAddress, long facebookId, String openId,
			String languageId, String timeZoneId, String greeting,
			String comments, String smsSn, String aimSn, String facebookSn,
			String icqSn, String jabberSn, String msnSn, String mySpaceSn,
			String skypeSn, String twitterSn, String ymSn)
		throws PortalException {

		String password = getUpdateUserPassword(request, userId);

		User user = UserLocalServiceUtil.getUserById(userId);

		Contact contact = user.getContact();

		Calendar birthdayCal = CalendarFactoryUtil.getCalendar();

		birthdayCal.setTime(contact.getBirthday());

		int birthdayMonth = birthdayCal.get(Calendar.MONTH);
		int birthdayDay = birthdayCal.get(Calendar.DATE);
		int birthdayYear = birthdayCal.get(Calendar.YEAR);

		long[] groupIds = null;
		long[] organizationIds = null;
		long[] roleIds = null;
		List<UserGroupRole> userGroupRoles = null;
		long[] userGroupIds = null;
		ServiceContext serviceContext = new ServiceContext();

		_userService.updateUser(
			userId, password, StringPool.BLANK, StringPool.BLANK,
			user.isPasswordReset(), user.getReminderQueryQuestion(),
			user.getReminderQueryAnswer(), screenName, emailAddress, facebookId,
			openId, languageId, timeZoneId, greeting, comments,
			contact.getFirstName(), contact.getMiddleName(),
			contact.getLastName(), contact.getPrefixId(), contact.getSuffixId(),
			contact.isMale(), birthdayMonth, birthdayDay, birthdayYear, smsSn,
			aimSn, facebookSn, icqSn, jabberSn, msnSn, mySpaceSn, skypeSn,
			twitterSn, ymSn, contact.getJobTitle(), groupIds, organizationIds,
			roleIds, userGroupRoles, userGroupIds, serviceContext);
	}

	@Reference(unbind = "-")
	protected void setUserService(UserService userService) {
		_userService = userService;
	}

	private static final String _STRUTS_LOCALE_KEY =
		"org.apache.struts.action.LOCALE";

	private static Log _log = LogFactoryUtil.getLog(ViewAction.class);

	private UserService _userService;

}