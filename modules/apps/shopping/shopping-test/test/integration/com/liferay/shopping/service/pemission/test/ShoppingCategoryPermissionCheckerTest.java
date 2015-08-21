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

package com.liferay.shopping.service.pemission.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.permission.test.BasePermissionTestCase;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.shopping.model.ShoppingCategory;
import com.liferay.shopping.service.permission.ShoppingCategoryPermission;
import com.liferay.shopping.service.permission.ShoppingPermission;
import com.liferay.shopping.util.test.ShoppingTestUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eric Chin
 * @author Shinn Lok
 */
@RunWith(Arquillian.class)
@Sync
public class ShoppingCategoryPermissionCheckerTest
	extends BasePermissionTestCase {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testContains() throws Exception {
		Assert.assertTrue(
			ShoppingCategoryPermission.contains(
				permissionChecker, _category, ActionKeys.VIEW));
		Assert.assertTrue(
			ShoppingCategoryPermission.contains(
				permissionChecker, _subcategory, ActionKeys.VIEW));

		removePortletModelViewPermission();

		Assert.assertFalse(
			ShoppingCategoryPermission.contains(
				permissionChecker, _category, ActionKeys.VIEW));
		Assert.assertFalse(
			ShoppingCategoryPermission.contains(
				permissionChecker, _subcategory, ActionKeys.VIEW));
	}

	@Before
	@Override
	protected void doSetUp() throws Exception {
		_category = ShoppingTestUtil.addCategory(group.getGroupId());

		_subcategory = ShoppingTestUtil.addCategory(
			group.getGroupId(), _category.getCategoryId());

		super.setUp();
	}

	@Override
	protected String getResourceName() {
		return ShoppingPermission.RESOURCE_NAME;
	}

	private ShoppingCategory _category;
	private ShoppingCategory _subcategory;

}