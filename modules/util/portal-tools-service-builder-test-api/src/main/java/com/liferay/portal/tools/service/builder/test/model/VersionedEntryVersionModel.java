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
import com.liferay.portal.kernel.model.version.VersionModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the VersionedEntryVersion service. Represents a row in the &quot;VersionedEntryVersion&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.tools.service.builder.test.model.impl.VersionedEntryVersionModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.tools.service.builder.test.model.impl.VersionedEntryVersionImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see VersionedEntryVersion
 * @generated
 */
@ProviderType
public interface VersionedEntryVersionModel
	extends BaseModel<VersionedEntryVersion>, VersionModel<VersionedEntry> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a versioned entry version model instance should use the {@link VersionedEntryVersion} interface instead.
	 */

	/**
	 * Returns the primary key of this versioned entry version.
	 *
	 * @return the primary key of this versioned entry version
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this versioned entry version.
	 *
	 * @param primaryKey the primary key of this versioned entry version
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the versioned entry version ID of this versioned entry version.
	 *
	 * @return the versioned entry version ID of this versioned entry version
	 */
	public long getVersionedEntryVersionId();

	/**
	 * Sets the versioned entry version ID of this versioned entry version.
	 *
	 * @param versionedEntryVersionId the versioned entry version ID of this versioned entry version
	 */
	public void setVersionedEntryVersionId(long versionedEntryVersionId);

	/**
	 * Returns the version of this versioned entry version.
	 *
	 * @return the version of this versioned entry version
	 */
	@Override
	public int getVersion();

	/**
	 * Sets the version of this versioned entry version.
	 *
	 * @param version the version of this versioned entry version
	 */
	@Override
	public void setVersion(int version);

	/**
	 * Returns the versioned entry ID of this versioned entry version.
	 *
	 * @return the versioned entry ID of this versioned entry version
	 */
	public long getVersionedEntryId();

	/**
	 * Sets the versioned entry ID of this versioned entry version.
	 *
	 * @param versionedEntryId the versioned entry ID of this versioned entry version
	 */
	public void setVersionedEntryId(long versionedEntryId);

	/**
	 * Returns the group ID of this versioned entry version.
	 *
	 * @return the group ID of this versioned entry version
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this versioned entry version.
	 *
	 * @param groupId the group ID of this versioned entry version
	 */
	public void setGroupId(long groupId);

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
	public int compareTo(VersionedEntryVersion versionedEntryVersion);

	@Override
	public int hashCode();

	@Override
	public CacheModel<VersionedEntryVersion> toCacheModel();

	@Override
	public VersionedEntryVersion toEscapedModel();

	@Override
	public VersionedEntryVersion toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();

}