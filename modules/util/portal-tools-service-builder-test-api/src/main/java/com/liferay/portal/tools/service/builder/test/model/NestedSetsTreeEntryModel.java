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
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the NestedSetsTreeEntry service. Represents a row in the &quot;NestedSetsTreeEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.tools.service.builder.test.model.impl.NestedSetsTreeEntryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.tools.service.builder.test.model.impl.NestedSetsTreeEntryImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see NestedSetsTreeEntry
 * @generated
 */
@ProviderType
public interface NestedSetsTreeEntryModel
	extends BaseModel<NestedSetsTreeEntry> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a nested sets tree entry model instance should use the {@link NestedSetsTreeEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this nested sets tree entry.
	 *
	 * @return the primary key of this nested sets tree entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this nested sets tree entry.
	 *
	 * @param primaryKey the primary key of this nested sets tree entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @return the nested sets tree entry ID of this nested sets tree entry
	 */
	public long getNestedSetsTreeEntryId();

	/**
	 * Sets the nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @param nestedSetsTreeEntryId the nested sets tree entry ID of this nested sets tree entry
	 */
	public void setNestedSetsTreeEntryId(long nestedSetsTreeEntryId);

	/**
	 * Returns the group ID of this nested sets tree entry.
	 *
	 * @return the group ID of this nested sets tree entry
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this nested sets tree entry.
	 *
	 * @param groupId the group ID of this nested sets tree entry
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the parent nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @return the parent nested sets tree entry ID of this nested sets tree entry
	 */
	public long getParentNestedSetsTreeEntryId();

	/**
	 * Sets the parent nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @param parentNestedSetsTreeEntryId the parent nested sets tree entry ID of this nested sets tree entry
	 */
	public void setParentNestedSetsTreeEntryId(
		long parentNestedSetsTreeEntryId);

	/**
	 * Returns the left nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @return the left nested sets tree entry ID of this nested sets tree entry
	 */
	public long getLeftNestedSetsTreeEntryId();

	/**
	 * Sets the left nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @param leftNestedSetsTreeEntryId the left nested sets tree entry ID of this nested sets tree entry
	 */
	public void setLeftNestedSetsTreeEntryId(long leftNestedSetsTreeEntryId);

	/**
	 * Returns the right nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @return the right nested sets tree entry ID of this nested sets tree entry
	 */
	public long getRightNestedSetsTreeEntryId();

	/**
	 * Sets the right nested sets tree entry ID of this nested sets tree entry.
	 *
	 * @param rightNestedSetsTreeEntryId the right nested sets tree entry ID of this nested sets tree entry
	 */
	public void setRightNestedSetsTreeEntryId(long rightNestedSetsTreeEntryId);

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
	public int compareTo(NestedSetsTreeEntry nestedSetsTreeEntry);

	@Override
	public int hashCode();

	@Override
	public CacheModel<NestedSetsTreeEntry> toCacheModel();

	@Override
	public NestedSetsTreeEntry toEscapedModel();

	@Override
	public NestedSetsTreeEntry toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();

}