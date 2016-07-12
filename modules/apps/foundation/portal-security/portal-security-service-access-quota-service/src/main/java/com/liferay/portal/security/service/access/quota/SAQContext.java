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

package com.liferay.portal.security.service.access.quota;

import com.liferay.portal.security.service.access.quota.metric.SAQContextMatcher;
import com.liferay.portal.security.service.access.quota.persistence.SAQImpression;
import com.liferay.portal.security.service.access.quota.persistence.SAQImpressionPersistence;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Stian Sigvartsen
 */
public interface SAQContext extends SAQContextMatcher {

	public Map<String, String> getMetricsMap();

	public String getMetricValue(String metricName);

	public List<ServiceAccessQuota> getQuotas();

	public Set<ServiceAccessQuota> matches(SAQImpression impression);

	public void process(
		long companyId, SAQImpressionPersistence impressionsPersistence,
		SAQContextListener listener);

	public void process(SAQImpression impression, SAQContextListener listener);

}