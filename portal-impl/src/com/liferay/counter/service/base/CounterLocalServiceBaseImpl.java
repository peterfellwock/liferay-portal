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

package com.liferay.counter.service.base;

import com.liferay.counter.kernel.model.Counter;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.counter.kernel.service.persistence.CounterFinder;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Provides the base implementation for the counter local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.counter.service.impl.CounterLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.counter.service.impl.CounterLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class CounterLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements CounterLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CounterLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.counter.kernel.service.CounterLocalServiceUtil</code>.
	 */

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		CounterLocalService counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the counter finder.
	 *
	 * @return the counter finder
	 */
	public CounterFinder getCounterFinder() {
		return counterFinder;
	}

	/**
	 * Sets the counter finder.
	 *
	 * @param counterFinder the counter finder
	 */
	public void setCounterFinder(CounterFinder counterFinder) {
		this.counterFinder = counterFinder;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CounterLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return Counter.class;
	}

	protected String getModelClassName() {
		return Counter.class.getName();
	}

	@BeanReference(type = CounterLocalService.class)
	protected CounterLocalService counterLocalService;

	@BeanReference(type = CounterFinder.class)
	protected CounterFinder counterFinder;

}