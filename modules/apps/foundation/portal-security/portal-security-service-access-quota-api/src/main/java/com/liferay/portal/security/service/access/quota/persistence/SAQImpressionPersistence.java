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

package com.liferay.portal.security.service.access.quota.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.security.service.access.quota.metric.SAQContextMatcher;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Stian Sigvartsen
 */
@ProviderType
public interface SAQImpressionPersistence {

	public void createImpression(
		long companyId, Map<String, String> metrics, long expiryIntervalMillis);

	/* I find it unsettling to have a persistence class that returns an iterator.
	*  Could this mean that the impressions are lazily retrieved. What would happen
	*  if the backend is a DB? Could the user have problems if invoking the Iterator
	*  when the connection has closed?
	*
	*  Couldn't we just return a materialized collection?
	* */
	public Iterator<SAQImpression> findAllImpressions(long companyId);

	public Iterator<SAQImpression> findImpressions(
		long companyId, SAQContextMatcher contextMetricsMatcher);

	public int getImpressionsCount(long companyId, long expiryIntervalMillis);

}