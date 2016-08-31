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
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.PortletContainerTestUtil;
import com.liferay.portal.util.test.PortletContainerTestUtil.Response;
import com.liferay.portlet.PortletURLImpl;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import javax.servlet.http.HttpServletRequest;

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
	public void testPortalAuthenticationToken() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

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
	}

	@Test
	public void testXCSRFToken() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken1() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken2() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken4() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken5() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken6() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken7() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken8() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken9() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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
	}

	@Test
	public void testXCSRFToken10() throws Exception {
		testPortlet = new ActionRequestTestPortlet();

		setUpPortlet(
			testPortlet, new HashMapDictionary<String, Object>(),
			TEST_PORTLET_ID);

		HttpServletRequest httpServletRequest =
			PortletContainerTestUtil.getHttpServletRequest(group, layout);

		Response response = PortletContainerTestUtil.getPortalAuthentication(
			httpServletRequest, layout, TEST_PORTLET_ID);

		if (Validator.isNull(response.getBody())) {
			System.out.println("::testXCSRFToken  Body Fail-----");
			System.out.println("response.getCode()" + response.getCode());
			Assert.fail();
		}

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