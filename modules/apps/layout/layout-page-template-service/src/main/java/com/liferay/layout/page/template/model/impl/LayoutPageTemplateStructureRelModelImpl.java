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

package com.liferay.layout.page.template.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel;
import com.liferay.layout.page.template.model.LayoutPageTemplateStructureRelModel;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model implementation for the LayoutPageTemplateStructureRel service. Represents a row in the &quot;LayoutPageTemplateStructureRel&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>LayoutPageTemplateStructureRelModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link LayoutPageTemplateStructureRelImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LayoutPageTemplateStructureRelImpl
 * @generated
 */
@ProviderType
public class LayoutPageTemplateStructureRelModelImpl
	extends BaseModelImpl<LayoutPageTemplateStructureRel>
	implements LayoutPageTemplateStructureRelModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a layout page template structure rel model instance should use the <code>LayoutPageTemplateStructureRel</code> interface instead.
	 */
	public static final String TABLE_NAME = "LayoutPageTemplateStructureRel";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"lPageTemplateStructureRelId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"layoutPageTemplateStructureId", Types.BIGINT},
		{"segmentsExperienceId", Types.BIGINT}, {"data_", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("lPageTemplateStructureRelId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("layoutPageTemplateStructureId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("segmentsExperienceId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("data_", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table LayoutPageTemplateStructureRel (uuid_ VARCHAR(75) null,lPageTemplateStructureRelId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,layoutPageTemplateStructureId LONG,segmentsExperienceId LONG,data_ STRING null)";

	public static final String TABLE_SQL_DROP =
		"drop table LayoutPageTemplateStructureRel";

	public static final String ORDER_BY_JPQL =
		" ORDER BY layoutPageTemplateStructureRel.layoutPageTemplateStructureRelId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY LayoutPageTemplateStructureRel.lPageTemplateStructureRelId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.layout.page.template.service.util.ServiceProps.get(
			"value.object.entity.cache.enabled.com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.layout.page.template.service.util.ServiceProps.get(
			"value.object.finder.cache.enabled.com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.layout.page.template.service.util.ServiceProps.get(
			"value.object.column.bitmask.enabled.com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel"),
		true);

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long GROUPID_COLUMN_BITMASK = 2L;

	public static final long LAYOUTPAGETEMPLATESTRUCTUREID_COLUMN_BITMASK = 4L;

	public static final long SEGMENTSEXPERIENCEID_COLUMN_BITMASK = 8L;

	public static final long UUID_COLUMN_BITMASK = 16L;

	public static final long LAYOUTPAGETEMPLATESTRUCTURERELID_COLUMN_BITMASK =
		32L;

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.layout.page.template.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.layout.page.template.model.LayoutPageTemplateStructureRel"));

	public LayoutPageTemplateStructureRelModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _layoutPageTemplateStructureRelId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setLayoutPageTemplateStructureRelId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _layoutPageTemplateStructureRelId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return LayoutPageTemplateStructureRel.class;
	}

	@Override
	public String getModelClassName() {
		return LayoutPageTemplateStructureRel.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<LayoutPageTemplateStructureRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<LayoutPageTemplateStructureRel, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<LayoutPageTemplateStructureRel, Object>
				attributeGetterFunction = entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply(
					(LayoutPageTemplateStructureRel)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<LayoutPageTemplateStructureRel, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<LayoutPageTemplateStructureRel, Object>
				attributeSetterBiConsumer = attributeSetterBiConsumers.get(
					attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(LayoutPageTemplateStructureRel)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<LayoutPageTemplateStructureRel, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<LayoutPageTemplateStructureRel, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map
		<String, Function<LayoutPageTemplateStructureRel, Object>>
			_attributeGetterFunctions;
	private static final Map
		<String, BiConsumer<LayoutPageTemplateStructureRel, Object>>
			_attributeSetterBiConsumers;

	static {
		Map<String, Function<LayoutPageTemplateStructureRel, Object>>
			attributeGetterFunctions =
				new LinkedHashMap
					<String,
					 Function<LayoutPageTemplateStructureRel, Object>>();
		Map<String, BiConsumer<LayoutPageTemplateStructureRel, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap
					<String, BiConsumer<LayoutPageTemplateStructureRel, ?>>();

		attributeGetterFunctions.put(
			"uuid", LayoutPageTemplateStructureRel::getUuid);
		attributeSetterBiConsumers.put(
			"uuid",
			(BiConsumer<LayoutPageTemplateStructureRel, String>)
				LayoutPageTemplateStructureRel::setUuid);
		attributeGetterFunctions.put(
			"layoutPageTemplateStructureRelId",
			LayoutPageTemplateStructureRel::
				getLayoutPageTemplateStructureRelId);
		attributeSetterBiConsumers.put(
			"layoutPageTemplateStructureRelId",
			(BiConsumer<LayoutPageTemplateStructureRel, Long>)
				LayoutPageTemplateStructureRel::
					setLayoutPageTemplateStructureRelId);
		attributeGetterFunctions.put(
			"groupId", LayoutPageTemplateStructureRel::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<LayoutPageTemplateStructureRel, Long>)
				LayoutPageTemplateStructureRel::setGroupId);
		attributeGetterFunctions.put(
			"companyId", LayoutPageTemplateStructureRel::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<LayoutPageTemplateStructureRel, Long>)
				LayoutPageTemplateStructureRel::setCompanyId);
		attributeGetterFunctions.put(
			"userId", LayoutPageTemplateStructureRel::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<LayoutPageTemplateStructureRel, Long>)
				LayoutPageTemplateStructureRel::setUserId);
		attributeGetterFunctions.put(
			"userName", LayoutPageTemplateStructureRel::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<LayoutPageTemplateStructureRel, String>)
				LayoutPageTemplateStructureRel::setUserName);
		attributeGetterFunctions.put(
			"createDate", LayoutPageTemplateStructureRel::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<LayoutPageTemplateStructureRel, Date>)
				LayoutPageTemplateStructureRel::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", LayoutPageTemplateStructureRel::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<LayoutPageTemplateStructureRel, Date>)
				LayoutPageTemplateStructureRel::setModifiedDate);
		attributeGetterFunctions.put(
			"layoutPageTemplateStructureId",
			LayoutPageTemplateStructureRel::getLayoutPageTemplateStructureId);
		attributeSetterBiConsumers.put(
			"layoutPageTemplateStructureId",
			(BiConsumer<LayoutPageTemplateStructureRel, Long>)
				LayoutPageTemplateStructureRel::
					setLayoutPageTemplateStructureId);
		attributeGetterFunctions.put(
			"segmentsExperienceId",
			LayoutPageTemplateStructureRel::getSegmentsExperienceId);
		attributeSetterBiConsumers.put(
			"segmentsExperienceId",
			(BiConsumer<LayoutPageTemplateStructureRel, Long>)
				LayoutPageTemplateStructureRel::setSegmentsExperienceId);
		attributeGetterFunctions.put(
			"data", LayoutPageTemplateStructureRel::getData);
		attributeSetterBiConsumers.put(
			"data",
			(BiConsumer<LayoutPageTemplateStructureRel, String>)
				LayoutPageTemplateStructureRel::setData);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@Override
	public long getLayoutPageTemplateStructureRelId() {
		return _layoutPageTemplateStructureRelId;
	}

	@Override
	public void setLayoutPageTemplateStructureRelId(
		long layoutPageTemplateStructureRelId) {

		_layoutPageTemplateStructureRelId = layoutPageTemplateStructureRelId;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_modifiedDate = modifiedDate;
	}

	@Override
	public long getLayoutPageTemplateStructureId() {
		return _layoutPageTemplateStructureId;
	}

	@Override
	public void setLayoutPageTemplateStructureId(
		long layoutPageTemplateStructureId) {

		_columnBitmask |= LAYOUTPAGETEMPLATESTRUCTUREID_COLUMN_BITMASK;

		if (!_setOriginalLayoutPageTemplateStructureId) {
			_setOriginalLayoutPageTemplateStructureId = true;

			_originalLayoutPageTemplateStructureId =
				_layoutPageTemplateStructureId;
		}

		_layoutPageTemplateStructureId = layoutPageTemplateStructureId;
	}

	public long getOriginalLayoutPageTemplateStructureId() {
		return _originalLayoutPageTemplateStructureId;
	}

	@Override
	public long getSegmentsExperienceId() {
		return _segmentsExperienceId;
	}

	@Override
	public void setSegmentsExperienceId(long segmentsExperienceId) {
		_columnBitmask |= SEGMENTSEXPERIENCEID_COLUMN_BITMASK;

		if (!_setOriginalSegmentsExperienceId) {
			_setOriginalSegmentsExperienceId = true;

			_originalSegmentsExperienceId = _segmentsExperienceId;
		}

		_segmentsExperienceId = segmentsExperienceId;
	}

	public long getOriginalSegmentsExperienceId() {
		return _originalSegmentsExperienceId;
	}

	@Override
	public String getData() {
		if (_data == null) {
			return "";
		}
		else {
			return _data;
		}
	}

	@Override
	public void setData(String data) {
		_data = data;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(
				LayoutPageTemplateStructureRel.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), LayoutPageTemplateStructureRel.class.getName(),
			getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public LayoutPageTemplateStructureRel toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel =
				(LayoutPageTemplateStructureRel)ProxyUtil.newProxyInstance(
					_classLoader, _escapedModelInterfaces,
					new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		LayoutPageTemplateStructureRelImpl layoutPageTemplateStructureRelImpl =
			new LayoutPageTemplateStructureRelImpl();

		layoutPageTemplateStructureRelImpl.setUuid(getUuid());
		layoutPageTemplateStructureRelImpl.setLayoutPageTemplateStructureRelId(
			getLayoutPageTemplateStructureRelId());
		layoutPageTemplateStructureRelImpl.setGroupId(getGroupId());
		layoutPageTemplateStructureRelImpl.setCompanyId(getCompanyId());
		layoutPageTemplateStructureRelImpl.setUserId(getUserId());
		layoutPageTemplateStructureRelImpl.setUserName(getUserName());
		layoutPageTemplateStructureRelImpl.setCreateDate(getCreateDate());
		layoutPageTemplateStructureRelImpl.setModifiedDate(getModifiedDate());
		layoutPageTemplateStructureRelImpl.setLayoutPageTemplateStructureId(
			getLayoutPageTemplateStructureId());
		layoutPageTemplateStructureRelImpl.setSegmentsExperienceId(
			getSegmentsExperienceId());
		layoutPageTemplateStructureRelImpl.setData(getData());

		layoutPageTemplateStructureRelImpl.resetOriginalValues();

		return layoutPageTemplateStructureRelImpl;
	}

	@Override
	public int compareTo(
		LayoutPageTemplateStructureRel layoutPageTemplateStructureRel) {

		long primaryKey = layoutPageTemplateStructureRel.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof LayoutPageTemplateStructureRel)) {
			return false;
		}

		LayoutPageTemplateStructureRel layoutPageTemplateStructureRel =
			(LayoutPageTemplateStructureRel)obj;

		long primaryKey = layoutPageTemplateStructureRel.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		LayoutPageTemplateStructureRelModelImpl
			layoutPageTemplateStructureRelModelImpl = this;

		layoutPageTemplateStructureRelModelImpl._originalUuid =
			layoutPageTemplateStructureRelModelImpl._uuid;

		layoutPageTemplateStructureRelModelImpl._originalGroupId =
			layoutPageTemplateStructureRelModelImpl._groupId;

		layoutPageTemplateStructureRelModelImpl._setOriginalGroupId = false;

		layoutPageTemplateStructureRelModelImpl._originalCompanyId =
			layoutPageTemplateStructureRelModelImpl._companyId;

		layoutPageTemplateStructureRelModelImpl._setOriginalCompanyId = false;

		layoutPageTemplateStructureRelModelImpl._setModifiedDate = false;

		layoutPageTemplateStructureRelModelImpl.
			_originalLayoutPageTemplateStructureId =
				layoutPageTemplateStructureRelModelImpl.
					_layoutPageTemplateStructureId;

		layoutPageTemplateStructureRelModelImpl.
			_setOriginalLayoutPageTemplateStructureId = false;

		layoutPageTemplateStructureRelModelImpl._originalSegmentsExperienceId =
			layoutPageTemplateStructureRelModelImpl._segmentsExperienceId;

		layoutPageTemplateStructureRelModelImpl.
			_setOriginalSegmentsExperienceId = false;

		layoutPageTemplateStructureRelModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<LayoutPageTemplateStructureRel> toCacheModel() {
		LayoutPageTemplateStructureRelCacheModel
			layoutPageTemplateStructureRelCacheModel =
				new LayoutPageTemplateStructureRelCacheModel();

		layoutPageTemplateStructureRelCacheModel.uuid = getUuid();

		String uuid = layoutPageTemplateStructureRelCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			layoutPageTemplateStructureRelCacheModel.uuid = null;
		}

		layoutPageTemplateStructureRelCacheModel.
			layoutPageTemplateStructureRelId =
				getLayoutPageTemplateStructureRelId();

		layoutPageTemplateStructureRelCacheModel.groupId = getGroupId();

		layoutPageTemplateStructureRelCacheModel.companyId = getCompanyId();

		layoutPageTemplateStructureRelCacheModel.userId = getUserId();

		layoutPageTemplateStructureRelCacheModel.userName = getUserName();

		String userName = layoutPageTemplateStructureRelCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			layoutPageTemplateStructureRelCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			layoutPageTemplateStructureRelCacheModel.createDate =
				createDate.getTime();
		}
		else {
			layoutPageTemplateStructureRelCacheModel.createDate =
				Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			layoutPageTemplateStructureRelCacheModel.modifiedDate =
				modifiedDate.getTime();
		}
		else {
			layoutPageTemplateStructureRelCacheModel.modifiedDate =
				Long.MIN_VALUE;
		}

		layoutPageTemplateStructureRelCacheModel.layoutPageTemplateStructureId =
			getLayoutPageTemplateStructureId();

		layoutPageTemplateStructureRelCacheModel.segmentsExperienceId =
			getSegmentsExperienceId();

		layoutPageTemplateStructureRelCacheModel.data = getData();

		String data = layoutPageTemplateStructureRelCacheModel.data;

		if ((data != null) && (data.length() == 0)) {
			layoutPageTemplateStructureRelCacheModel.data = null;
		}

		return layoutPageTemplateStructureRelCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<LayoutPageTemplateStructureRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<LayoutPageTemplateStructureRel, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<LayoutPageTemplateStructureRel, Object>
				attributeGetterFunction = entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(
				attributeGetterFunction.apply(
					(LayoutPageTemplateStructureRel)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<LayoutPageTemplateStructureRel, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<LayoutPageTemplateStructureRel, Object>>
				entry : attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<LayoutPageTemplateStructureRel, Object>
				attributeGetterFunction = entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(
				attributeGetterFunction.apply(
					(LayoutPageTemplateStructureRel)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader =
		LayoutPageTemplateStructureRel.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
		LayoutPageTemplateStructureRel.class, ModelWrapper.class
	};

	private String _uuid;
	private String _originalUuid;
	private long _layoutPageTemplateStructureRelId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private long _layoutPageTemplateStructureId;
	private long _originalLayoutPageTemplateStructureId;
	private boolean _setOriginalLayoutPageTemplateStructureId;
	private long _segmentsExperienceId;
	private long _originalSegmentsExperienceId;
	private boolean _setOriginalSegmentsExperienceId;
	private String _data;
	private long _columnBitmask;
	private LayoutPageTemplateStructureRel _escapedModel;

}