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

package com.liferay.sync.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.sync.model.SyncDevice;
import com.liferay.sync.model.SyncDeviceModel;
import com.liferay.sync.model.SyncDeviceSoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model implementation for the SyncDevice service. Represents a row in the &quot;SyncDevice&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>SyncDeviceModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SyncDeviceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SyncDeviceImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class SyncDeviceModelImpl
	extends BaseModelImpl<SyncDevice> implements SyncDeviceModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a sync device model instance should use the <code>SyncDevice</code> interface instead.
	 */
	public static final String TABLE_NAME = "SyncDevice";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"syncDeviceId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"createDate", Types.TIMESTAMP},
		{"modifiedDate", Types.TIMESTAMP}, {"type_", Types.VARCHAR},
		{"buildNumber", Types.BIGINT}, {"featureSet", Types.INTEGER},
		{"hostname", Types.VARCHAR}, {"status", Types.INTEGER}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("syncDeviceId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("type_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("buildNumber", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("featureSet", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("hostname", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("status", Types.INTEGER);
	}

	public static final String TABLE_SQL_CREATE =
		"create table SyncDevice (uuid_ VARCHAR(75) null,syncDeviceId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,type_ VARCHAR(75) null,buildNumber LONG,featureSet INTEGER,hostname VARCHAR(75) null,status INTEGER)";

	public static final String TABLE_SQL_DROP = "drop table SyncDevice";

	public static final String ORDER_BY_JPQL =
		" ORDER BY syncDevice.syncDeviceId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY SyncDevice.syncDeviceId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.sync.service.util.ServiceProps.get(
			"value.object.entity.cache.enabled.com.liferay.sync.model.SyncDevice"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.sync.service.util.ServiceProps.get(
			"value.object.finder.cache.enabled.com.liferay.sync.model.SyncDevice"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.sync.service.util.ServiceProps.get(
			"value.object.column.bitmask.enabled.com.liferay.sync.model.SyncDevice"),
		true);

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long USERID_COLUMN_BITMASK = 2L;

	public static final long USERNAME_COLUMN_BITMASK = 4L;

	public static final long UUID_COLUMN_BITMASK = 8L;

	public static final long SYNCDEVICEID_COLUMN_BITMASK = 16L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static SyncDevice toModel(SyncDeviceSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		SyncDevice model = new SyncDeviceImpl();

		model.setUuid(soapModel.getUuid());
		model.setSyncDeviceId(soapModel.getSyncDeviceId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setType(soapModel.getType());
		model.setBuildNumber(soapModel.getBuildNumber());
		model.setFeatureSet(soapModel.getFeatureSet());
		model.setHostname(soapModel.getHostname());
		model.setStatus(soapModel.getStatus());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<SyncDevice> toModels(SyncDeviceSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<SyncDevice> models = new ArrayList<SyncDevice>(soapModels.length);

		for (SyncDeviceSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.sync.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.sync.model.SyncDevice"));

	public SyncDeviceModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _syncDeviceId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setSyncDeviceId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _syncDeviceId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SyncDevice.class;
	}

	@Override
	public String getModelClassName() {
		return SyncDevice.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SyncDevice, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<SyncDevice, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SyncDevice, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((SyncDevice)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SyncDevice, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<SyncDevice, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(SyncDevice)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<SyncDevice, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<SyncDevice, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<SyncDevice, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<SyncDevice, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<SyncDevice, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<SyncDevice, Object>>();
		Map<String, BiConsumer<SyncDevice, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<SyncDevice, ?>>();

		attributeGetterFunctions.put("uuid", SyncDevice::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<SyncDevice, String>)SyncDevice::setUuid);
		attributeGetterFunctions.put(
			"syncDeviceId", SyncDevice::getSyncDeviceId);
		attributeSetterBiConsumers.put(
			"syncDeviceId",
			(BiConsumer<SyncDevice, Long>)SyncDevice::setSyncDeviceId);
		attributeGetterFunctions.put("companyId", SyncDevice::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<SyncDevice, Long>)SyncDevice::setCompanyId);
		attributeGetterFunctions.put("userId", SyncDevice::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<SyncDevice, Long>)SyncDevice::setUserId);
		attributeGetterFunctions.put("userName", SyncDevice::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<SyncDevice, String>)SyncDevice::setUserName);
		attributeGetterFunctions.put("createDate", SyncDevice::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<SyncDevice, Date>)SyncDevice::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", SyncDevice::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<SyncDevice, Date>)SyncDevice::setModifiedDate);
		attributeGetterFunctions.put("type", SyncDevice::getType);
		attributeSetterBiConsumers.put(
			"type", (BiConsumer<SyncDevice, String>)SyncDevice::setType);
		attributeGetterFunctions.put("buildNumber", SyncDevice::getBuildNumber);
		attributeSetterBiConsumers.put(
			"buildNumber",
			(BiConsumer<SyncDevice, Long>)SyncDevice::setBuildNumber);
		attributeGetterFunctions.put("featureSet", SyncDevice::getFeatureSet);
		attributeSetterBiConsumers.put(
			"featureSet",
			(BiConsumer<SyncDevice, Integer>)SyncDevice::setFeatureSet);
		attributeGetterFunctions.put("hostname", SyncDevice::getHostname);
		attributeSetterBiConsumers.put(
			"hostname",
			(BiConsumer<SyncDevice, String>)SyncDevice::setHostname);
		attributeGetterFunctions.put("status", SyncDevice::getStatus);
		attributeSetterBiConsumers.put(
			"status", (BiConsumer<SyncDevice, Integer>)SyncDevice::setStatus);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
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

	@JSON
	@Override
	public long getSyncDeviceId() {
		return _syncDeviceId;
	}

	@Override
	public void setSyncDeviceId(long syncDeviceId) {
		_syncDeviceId = syncDeviceId;
	}

	@JSON
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

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

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

	public long getOriginalUserId() {
		return _originalUserId;
	}

	@JSON
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
		_columnBitmask |= USERNAME_COLUMN_BITMASK;

		if (_originalUserName == null) {
			_originalUserName = _userName;
		}

		_userName = userName;
	}

	public String getOriginalUserName() {
		return GetterUtil.getString(_originalUserName);
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
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

	@JSON
	@Override
	public String getType() {
		if (_type == null) {
			return "";
		}
		else {
			return _type;
		}
	}

	@Override
	public void setType(String type) {
		_type = type;
	}

	@JSON
	@Override
	public long getBuildNumber() {
		return _buildNumber;
	}

	@Override
	public void setBuildNumber(long buildNumber) {
		_buildNumber = buildNumber;
	}

	@JSON
	@Override
	public int getFeatureSet() {
		return _featureSet;
	}

	@Override
	public void setFeatureSet(int featureSet) {
		_featureSet = featureSet;
	}

	@JSON
	@Override
	public String getHostname() {
		if (_hostname == null) {
			return "";
		}
		else {
			return _hostname;
		}
	}

	@Override
	public void setHostname(String hostname) {
		_hostname = hostname;
	}

	@JSON
	@Override
	public int getStatus() {
		return _status;
	}

	@Override
	public void setStatus(int status) {
		_status = status;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(SyncDevice.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), SyncDevice.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SyncDevice toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SyncDevice)ProxyUtil.newProxyInstance(
				_classLoader, _escapedModelInterfaces,
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SyncDeviceImpl syncDeviceImpl = new SyncDeviceImpl();

		syncDeviceImpl.setUuid(getUuid());
		syncDeviceImpl.setSyncDeviceId(getSyncDeviceId());
		syncDeviceImpl.setCompanyId(getCompanyId());
		syncDeviceImpl.setUserId(getUserId());
		syncDeviceImpl.setUserName(getUserName());
		syncDeviceImpl.setCreateDate(getCreateDate());
		syncDeviceImpl.setModifiedDate(getModifiedDate());
		syncDeviceImpl.setType(getType());
		syncDeviceImpl.setBuildNumber(getBuildNumber());
		syncDeviceImpl.setFeatureSet(getFeatureSet());
		syncDeviceImpl.setHostname(getHostname());
		syncDeviceImpl.setStatus(getStatus());

		syncDeviceImpl.resetOriginalValues();

		return syncDeviceImpl;
	}

	@Override
	public int compareTo(SyncDevice syncDevice) {
		long primaryKey = syncDevice.getPrimaryKey();

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

		if (!(obj instanceof SyncDevice)) {
			return false;
		}

		SyncDevice syncDevice = (SyncDevice)obj;

		long primaryKey = syncDevice.getPrimaryKey();

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
		SyncDeviceModelImpl syncDeviceModelImpl = this;

		syncDeviceModelImpl._originalUuid = syncDeviceModelImpl._uuid;

		syncDeviceModelImpl._originalCompanyId = syncDeviceModelImpl._companyId;

		syncDeviceModelImpl._setOriginalCompanyId = false;

		syncDeviceModelImpl._originalUserId = syncDeviceModelImpl._userId;

		syncDeviceModelImpl._setOriginalUserId = false;

		syncDeviceModelImpl._originalUserName = syncDeviceModelImpl._userName;

		syncDeviceModelImpl._setModifiedDate = false;

		syncDeviceModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SyncDevice> toCacheModel() {
		SyncDeviceCacheModel syncDeviceCacheModel = new SyncDeviceCacheModel();

		syncDeviceCacheModel.uuid = getUuid();

		String uuid = syncDeviceCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			syncDeviceCacheModel.uuid = null;
		}

		syncDeviceCacheModel.syncDeviceId = getSyncDeviceId();

		syncDeviceCacheModel.companyId = getCompanyId();

		syncDeviceCacheModel.userId = getUserId();

		syncDeviceCacheModel.userName = getUserName();

		String userName = syncDeviceCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			syncDeviceCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			syncDeviceCacheModel.createDate = createDate.getTime();
		}
		else {
			syncDeviceCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			syncDeviceCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			syncDeviceCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		syncDeviceCacheModel.type = getType();

		String type = syncDeviceCacheModel.type;

		if ((type != null) && (type.length() == 0)) {
			syncDeviceCacheModel.type = null;
		}

		syncDeviceCacheModel.buildNumber = getBuildNumber();

		syncDeviceCacheModel.featureSet = getFeatureSet();

		syncDeviceCacheModel.hostname = getHostname();

		String hostname = syncDeviceCacheModel.hostname;

		if ((hostname != null) && (hostname.length() == 0)) {
			syncDeviceCacheModel.hostname = null;
		}

		syncDeviceCacheModel.status = getStatus();

		return syncDeviceCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<SyncDevice, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<SyncDevice, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SyncDevice, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((SyncDevice)this));
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
		Map<String, Function<SyncDevice, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<SyncDevice, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SyncDevice, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((SyncDevice)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader =
		SyncDevice.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
		SyncDevice.class, ModelWrapper.class
	};

	private String _uuid;
	private String _originalUuid;
	private long _syncDeviceId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private String _userName;
	private String _originalUserName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _type;
	private long _buildNumber;
	private int _featureSet;
	private String _hostname;
	private int _status;
	private long _columnBitmask;
	private SyncDevice _escapedModel;

}