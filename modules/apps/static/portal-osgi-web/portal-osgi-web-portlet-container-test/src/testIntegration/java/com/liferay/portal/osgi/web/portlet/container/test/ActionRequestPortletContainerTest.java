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

package com.liferay.portal.osgi.web.portlet.container.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.petra.encryptor.Encryptor;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.test.log.CaptureAppender;
import com.liferay.portal.test.log.Log4JLoggerTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.test.PortletContainerTestUtil;
import com.liferay.portal.util.test.PortletContainerTestUtil.Response;
import com.liferay.portlet.PortletURLImpl;
import com.liferay.portlet.SecurityPortletContainerWrapper;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Raymond Augé
 */
@RunWith(Arquillian.class)
public class ActionRequestPortletContainerTest
	extends BasePortletContainerTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testAuthTokenCheckEnabled() throws Exception {
		Boolean authTokenCheckEnabled = ReflectionTestUtil.getAndSetFieldValue(
			PropsValues.class, "AUTH_TOKEN_CHECK_ENABLED", Boolean.FALSE);
		
		System.out.println("------testAuthTokenCheckEnabled-------------");
		System.out.println("------start-------------");

		try {
			setUpPortlet(
				testPortlet, new HashMapDictionary<String, Object>(),
				TEST_PORTLET_ID);

			HttpServletRequest httpServletRequest =
				PortletContainerTestUtil.getHttpServletRequest(group, layout);

			PortletURL portletURL = new PortletURLImpl(
				httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
				PortletRequest.ACTION_PHASE);

			Response response = PortletContainerTestUtil.request(
				portletURL.toString());

			Assert.assertEquals(200, response.getCode());
			Assert.assertTrue(testPortlet.isCalledAction());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				PropsValues.class, "AUTH_TOKEN_CHECK_ENABLED",
				authTokenCheckEnabled);
		}
		
		System.out.println("------testAuthTokenCheckEnabled-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testAuthTokenIgnoreOrigins() throws Exception {
		Dictionary<String, Object> properties = new HashMapDictionary<>();
		
		System.out.println("------testAuthTokenIgnoreOrigins-------------");
		System.out.println("------start-------------");

		properties.put(
			PropsKeys.AUTH_TOKEN_IGNORE_ORIGINS,
			SecurityPortletContainerWrapper.class.getName());

		registerService(Object.class, new Object(), properties);

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledAction());
		
		System.out.println("------testAuthTokenIgnoreOrigins-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testAuthTokenIgnorePortlets() throws Exception {
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		System.out.println("------testAuthTokenIgnorePortlets-------------");
		System.out.println("------start-------------");
		
		properties.put(PropsKeys.AUTH_TOKEN_IGNORE_PORTLETS, TEST_PORTLET_ID);

		registerService(Object.class, new Object(), properties);

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledAction());
		
		System.out.println("------testAuthTokenIgnorePortlets-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testInitParam() throws Exception {
		Dictionary<String, Object> properties = new HashMapDictionary<>();
		
		System.out.println("------testInitParam-------------");
		System.out.println("------start-------------");

		properties.put(
			"javax.portlet.init-param.check-auth-token",
			Boolean.FALSE.toString());

		setUpPortlet(testPortlet, properties, TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledAction());
		
		System.out.println("------testInitParam-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testNoPortalAuthenticationTokens() throws Exception {
		
		System.out.println("------testNoPortalAuthenticationTokens-------------");
		System.out.println("------start-------------");
		
		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		String url = portletURL.toString();

		try (CaptureAppender captureAppender =
				Log4JLoggerTestUtil.configureLog4JLogger(
					SecurityPortletContainerWrapper.class.getName(),
					Level.WARN)) {

			Response response = PortletContainerTestUtil.request(url);

			List<LoggingEvent> loggingEvents =
				captureAppender.getLoggingEvents();

			Assert.assertEquals(1, loggingEvents.size());

			LoggingEvent loggingEvent = loggingEvents.get(0);

			Assert.assertEquals(
				"User 0 is not allowed to access URL " +
					url.substring(0, url.indexOf('?')) + " and portlet " +
						TEST_PORTLET_ID,
				loggingEvent.getMessage());
			Assert.assertEquals(200, response.getCode());
			Assert.assertFalse(testPortlet.isCalledAction());
		}
		
		System.out.println("------testNoPortalAuthenticationTokens-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testPortalAuthenticationToken() throws Exception {
		testPortlet = new ActionRequestTestPortlet();
		
		System.out.println("------testPortalAuthenticationToken-------------");
		System.out.println("------start-------------");

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);
		
		check(response, "testPortalAuthenticationToken");

		testPortlet.reset();

		// Make an action request using the portal authentication token

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		String url = portletURL.toString();

		url = HttpUtil.setParameter(url, "p_auth", response.getBody());

		response = PortletContainerTestUtil.request(
			url, Collections.singletonMap("Cookie", response.getCookies()));

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledAction());
		
		System.out.println("------testPortalAuthenticationToken-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testPortalAuthenticationTokenSecret() throws Exception {
		
		System.out.println("------testPortalAuthenticationTokenSecret-------------");
		System.out.println("------start-------------");
		
		String authTokenSharedSecret = ReflectionTestUtil.getAndSetFieldValue(
			PropsValues.class, "AUTH_TOKEN_SHARED_SECRET", "test");

		try {
			setUpPortlet(
				testPortlet, new HashMapDictionary<String, Object>(),
				TEST_PORTLET_ID);

			HttpServletRequest httpServletRequest =
				PortletContainerTestUtil.getHttpServletRequest(group, layout);

			PortletURL portletURL = new PortletURLImpl(
				httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
				PortletRequest.ACTION_PHASE);

			portletURL.setParameter("p_auth_secret", Encryptor.digest("test"));

			Response response = PortletContainerTestUtil.request(
				portletURL.toString());

			Assert.assertEquals(200, response.getCode());
			Assert.assertTrue(testPortlet.isCalledAction());
		}
		finally {
			ReflectionTestUtil.setFieldValue(
				PropsValues.class, "AUTH_TOKEN_SHARED_SECRET",
				authTokenSharedSecret);
		}
		
		System.out.println("------testPortalAuthenticationTokenSecret-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testStrutsAction() throws Exception {
		
		System.out.println("------testStrutsAction-------------");
		System.out.println("------start-------------");
		
		Dictionary<String, Object> properties = new HashMapDictionary<>();

		properties.put(PropsKeys.AUTH_TOKEN_IGNORE_ACTIONS, "/test/portlet/1");
		properties.put("com.liferay.portlet.struts-path", "test/portlet");

		setUpPortlet(testPortlet, properties, TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		portletURL.setParameter("struts_action", "/test/portlet/1");

		Response response = PortletContainerTestUtil.request(
			portletURL.toString());

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledAction());
		
		System.out.println("------testStrutsAction-------------");
		System.out.println("------stop-------------");
	}

	@Test
	public void testXCSRFToken() throws Exception {
		
		System.out.println("------testXCSRFToken-------------");
		System.out.println("------start-------------");
		
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);
		check(response, "testXCSRFToken");

		testPortlet.reset();

		// Make an action request using the portal authentication token

		PortletURL portletURL = new PortletURLImpl(
			httpServletRequest, TEST_PORTLET_ID, layout.getPlid(),
			PortletRequest.ACTION_PHASE);

		String url = portletURL.toString();

		url = HttpUtil.removeParameter(url, "p_auth");

		Map<String, List<String>> headers = new HashMap<>();

		headers.put("Cookie", response.getCookies());
		headers.put(
			"X-CSRF-Token", Collections.singletonList(response.getBody()));

		response = PortletContainerTestUtil.request(url, headers);

		Assert.assertEquals(200, response.getCode());
		Assert.assertTrue(testPortlet.isCalledAction());
		
		System.out.println("------testXCSRFToken-------------");
		System.out.println("------stop-------------");
	}
	
	public void check(Response response, String test){
		System.out.println("------->CHECKING TEST:" + test);
		System.out.println("-----------------------------");
		System.out.println("cookie:" + response.getCookies());
		System.out.println("X-CSRF-Token:" + Collections.singletonList(response.getBody()));
		
		List<String> cookies = response.getCookies();
		
		if(cookies == null){
			System.out.println("COOKIES ARE NULL, test will fail");
			return;
		}
		
		if(cookies.size() < 1){
			System.out.println("COOKIES ARE Size 0, test will fail");
			return;
		}
		
		System.out.println("------------- cookies -------------------");
		for(String temp : cookies){
			System.out.println(temp);
		}
		System.out.println("-----------------------------------------");
		
		List<String> csrfs = Collections.singletonList(response.getBody());
		
		if(csrfs == null){
			System.out.println("csrfs ARE NULL, test will fail");
			return;
		}
		
		if(csrfs.size() < 1){
			System.out.println("csrfs ARE Size 0, test will fail");
			return;
		}
		
		System.out.println("------------- csrfs -------------------");
		for(String temp : csrfs){
			System.out.println(temp);
		}
		System.out.println("-----------------------------------------");	
		
	}
	

	private static class ActionRequestTestPortlet extends TestPortlet {

		@Override
		public void serveResource(
				ResourceRequest resourceRequest,
				ResourceResponse resourceResponse)
			throws IOException {

			PrintWriter printWriter = resourceResponse.getWriter();

			PortletURL portletURL = resourceResponse.createActionURL();

			String queryString = HttpUtil.getQueryString(portletURL.toString());

			Map<String, String[]> parameterMap = HttpUtil.getParameterMap(
				queryString);

			String portalAuthenticationToken = MapUtil.getString(
				parameterMap, "p_auth");

			printWriter.write(portalAuthenticationToken);
		}

	}

}