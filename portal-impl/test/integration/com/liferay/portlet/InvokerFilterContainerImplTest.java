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

import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.impl.PortletAppImpl;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.servlet.MainServlet;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.test.rule.callback.MainServletTestCallback;
import com.liferay.portal.util.test.AtomicState;
import com.liferay.portlet.bundle.invokerfiltercontainerimpl.TestPortletActionFilter;
import com.liferay.portlet.bundle.invokerfiltercontainerimpl.TestPortletEventFilter;
import com.liferay.portlet.bundle.invokerfiltercontainerimpl.TestPortletRenderFilter;
import com.liferay.portlet.bundle.invokerfiltercontainerimpl.TestPortletResourceFilter;

import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import javax.servlet.ServletContext;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 * @author Peter Fellwock
 */
public class InvokerFilterContainerImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
				new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE,
				new SyntheticBundleRule("bundle.invokerfiltercontainerimpl"));

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();

		MainServlet mainServlet = MainServletTestCallback.getMainServlet();

		ServletContext servletContext = mainServlet.getServletContext();

		String servletContextName = servletContext.getServletContextName();

		PortletAppImpl portletAppImpl = new PortletAppImpl(servletContextName);
		portletAppImpl.setWARFile(false);

		Portlet portlet = new PortletImpl();
		portlet.setPortletClass("com.liferay.portlet.StrutsPortlet");
		portlet.setPortletId("testPortletFilter");
		portlet.setPortletApp(portletAppImpl);

		PortletContext portletContext = PortletContextFactory.create(
			portlet, servletContext);

		try {
			_invokerFilterContainerImpl = new InvokerFilterContainerImpl(
				portlet, portletContext);
		}
		catch (PortletException e) {
		}
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();
	}

	@Test
	public void testGetActionFilters() throws PortletException {
		String className = TestPortletActionFilter.class.getName();

		List<ActionFilter> actionFilters =
			_invokerFilterContainerImpl.getActionFilters();

		for (ActionFilter actionFilter : actionFilters) {
			Class<?> clazz = actionFilter.getClass();

			if (className.equals(clazz.getName())) {
				return;
			}
		}

		Assert.fail();
	}

	@Test
	public void testGetEventFilters() {
		String className = TestPortletEventFilter.class.getName();

		List<EventFilter> eventFilters =
			_invokerFilterContainerImpl.getEventFilters();

		for (EventFilter eventFilter : eventFilters) {
			Class<?> clazz = eventFilter.getClass();

			if (className.equals(clazz.getName())) {
				return;
			}
		}

		Assert.fail();
	}

	@Test
	public void testGetRenderFilters() {
		String className = TestPortletRenderFilter.class.getName();

		List<RenderFilter> renderFilters =
			_invokerFilterContainerImpl.getRenderFilters();

		for (RenderFilter renderFilter : renderFilters) {
			Class<?> clazz = renderFilter.getClass();

			if (className.equals(clazz.getName())) {
				return;
			}
		}

		Assert.fail();
	}

	@Test
	public void testGetResourceFilters() {
		String className = TestPortletResourceFilter.class.getName();

		List<ResourceFilter> resourceFilters =
			_invokerFilterContainerImpl.getResourceFilters();

		for (ResourceFilter resourceFilter : resourceFilters) {
			Class<?> clazz = resourceFilter.getClass();

			if (className.equals(clazz.getName())) {
				return;
			}
		}

		Assert.fail();
	}

	@Test
	public void testInit() {
		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;
	private static InvokerFilterContainerImpl _invokerFilterContainerImpl;

}