/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.shop.by.diagram.internal.type.util.comparator;

import com.liferay.commerce.shop.by.diagram.type.CPDefinitionDiagramType;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerCustomizerFactory.ServiceWrapper;
import com.liferay.portal.kernel.util.MapUtil;

import java.io.Serializable;

import java.util.Comparator;

/**
 * @author Alessio Antonio Rendina
 */
public class CPDefinitionDiagramTypeOrderComparator
	implements Comparator<ServiceWrapper<CPDefinitionDiagramType>>,
			   Serializable {

	public CPDefinitionDiagramTypeOrderComparator() {
		this(true);
	}

	public CPDefinitionDiagramTypeOrderComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(
		ServiceWrapper<CPDefinitionDiagramType> serviceWrapper1,
		ServiceWrapper<CPDefinitionDiagramType> serviceWrapper2) {

		int value = Integer.compare(
			MapUtil.getInteger(
				serviceWrapper1.getProperties(),
				"commerce.product.definition.diagram.type.order",
				Integer.MAX_VALUE),
			MapUtil.getInteger(
				serviceWrapper2.getProperties(),
				"commerce.product.definition.diagram.type.order",
				Integer.MAX_VALUE));

		if (_ascending) {
			return value;
		}

		return Math.negateExact(value);
	}

	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;

}