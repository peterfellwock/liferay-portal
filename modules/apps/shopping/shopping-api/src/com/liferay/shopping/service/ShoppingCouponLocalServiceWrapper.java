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

package com.liferay.shopping.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link ShoppingCouponLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see ShoppingCouponLocalService
 * @generated
 */
@ProviderType
public class ShoppingCouponLocalServiceWrapper
	implements ShoppingCouponLocalService,
		ServiceWrapper<ShoppingCouponLocalService> {
	public ShoppingCouponLocalServiceWrapper(
		ShoppingCouponLocalService shoppingCouponLocalService) {
		_shoppingCouponLocalService = shoppingCouponLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public ShoppingCouponLocalService getWrappedShoppingCouponLocalService() {
		return _shoppingCouponLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedShoppingCouponLocalService(
		ShoppingCouponLocalService shoppingCouponLocalService) {
		_shoppingCouponLocalService = shoppingCouponLocalService;
	}

	@Override
	public ShoppingCouponLocalService getWrappedService() {
		return _shoppingCouponLocalService;
	}

	@Override
	public void setWrappedService(
		ShoppingCouponLocalService shoppingCouponLocalService) {
		_shoppingCouponLocalService = shoppingCouponLocalService;
	}

	private ShoppingCouponLocalService _shoppingCouponLocalService;
}