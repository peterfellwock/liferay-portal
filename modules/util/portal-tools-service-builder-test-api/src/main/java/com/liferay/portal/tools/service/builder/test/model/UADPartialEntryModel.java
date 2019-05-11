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

package com.liferay.portal.tools.service.builder.test.model;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the UADPartialEntry service. Represents a row in the &quot;UADPartialEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.tools.service.builder.test.model.impl.UADPartialEntryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.tools.service.builder.test.model.impl.UADPartialEntryImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see UADPartialEntry
 * @generated
 */
@ProviderType
public interface UADPartialEntryModel extends BaseModel<UADPartialEntry> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a uad partial entry model instance should use the {@link UADPartialEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this uad partial entry.
	 *
	 * @return the primary key of this uad partial entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this uad partial entry.
	 *
	 * @param primaryKey the primary key of this uad partial entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uad partial entry ID of this uad partial entry.
	 *
	 * @return the uad partial entry ID of this uad partial entry
	 */
	public long getUadPartialEntryId();

	/**
	 * Sets the uad partial entry ID of this uad partial entry.
	 *
	 * @param uadPartialEntryId the uad partial entry ID of this uad partial entry
	 */
	public void setUadPartialEntryId(long uadPartialEntryId);

	/**
	 * Returns the user ID of this uad partial entry.
	 *
	 * @return the user ID of this uad partial entry
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this uad partial entry.
	 *
	 * @param userId the user ID of this uad partial entry
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this uad partial entry.
	 *
	 * @return the user uuid of this uad partial entry
	 */
	public String getUserUuid();

	/**
	 * Sets the user uuid of this uad partial entry.
	 *
	 * @param userUuid the user uuid of this uad partial entry
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this uad partial entry.
	 *
	 * @return the user name of this uad partial entry
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this uad partial entry.
	 *
	 * @param userName the user name of this uad partial entry
	 */
	public void setUserName(String userName);

	/**
	 * Returns the message of this uad partial entry.
	 *
	 * @return the message of this uad partial entry
	 */
	@AutoEscape
	public String getMessage();

	/**
	 * Sets the message of this uad partial entry.
	 *
	 * @param message the message of this uad partial entry
	 */
	public void setMessage(String message);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(UADPartialEntry uadPartialEntry);

	@Override
	public int hashCode();

	@Override
	public CacheModel<UADPartialEntry> toCacheModel();

	@Override
	public UADPartialEntry toEscapedModel();

	@Override
	public UADPartialEntry toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();

}