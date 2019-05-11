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

package com.liferay.message.boards.model.impl;

import com.liferay.message.boards.model.MBCategory;
import com.liferay.message.boards.service.MBCategoryLocalServiceUtil;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model base implementation for the MBCategory service. Represents a row in the &quot;MBCategory&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link MBCategoryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBCategoryImpl
 * @see MBCategory
 * @generated
 */
@ProviderType
public abstract class MBCategoryBaseImpl
	extends MBCategoryModelImpl implements MBCategory {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a message boards category model instance should use the <code>MBCategory</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			MBCategoryLocalServiceUtil.addMBCategory(this);
		}
		else {
			MBCategoryLocalServiceUtil.updateMBCategory(this);
		}
	}

}