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

package com.liferay.object.admin.rest.internal.graphql.mutation.v1_0;

import com.liferay.object.admin.rest.dto.v1_0.ObjectDefinition;
import com.liferay.object.admin.rest.dto.v1_0.ObjectField;
import com.liferay.object.admin.rest.resource.v1_0.ObjectDefinitionResource;
import com.liferay.object.admin.rest.resource.v1_0.ObjectFieldResource;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLField;
import com.liferay.portal.vulcan.graphql.annotation.GraphQLName;

import java.util.function.BiFunction;

import javax.annotation.Generated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.osgi.service.component.ComponentServiceObjects;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public class Mutation {

	public static void setObjectDefinitionResourceComponentServiceObjects(
		ComponentServiceObjects<ObjectDefinitionResource>
			objectDefinitionResourceComponentServiceObjects) {

		_objectDefinitionResourceComponentServiceObjects =
			objectDefinitionResourceComponentServiceObjects;
	}

	public static void setObjectFieldResourceComponentServiceObjects(
		ComponentServiceObjects<ObjectFieldResource>
			objectFieldResourceComponentServiceObjects) {

		_objectFieldResourceComponentServiceObjects =
			objectFieldResourceComponentServiceObjects;
	}

	@GraphQLField
	public ObjectDefinition createObjectDefinition(
			@GraphQLName("objectDefinition") ObjectDefinition objectDefinition)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectDefinitionResource ->
				objectDefinitionResource.postObjectDefinition(
					objectDefinition));
	}

	@GraphQLField
	public Response createObjectDefinitionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectDefinitionResource ->
				objectDefinitionResource.postObjectDefinitionBatch(
					callbackURL, object));
	}

	@GraphQLField
	public boolean deleteObjectDefinition(
			@GraphQLName("objectDefinitionId") Long objectDefinitionId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_objectDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectDefinitionResource ->
				objectDefinitionResource.deleteObjectDefinition(
					objectDefinitionId));

		return true;
	}

	@GraphQLField
	public Response deleteObjectDefinitionBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectDefinitionResource ->
				objectDefinitionResource.deleteObjectDefinitionBatch(
					callbackURL, object));
	}

	@GraphQLField
	public boolean createObjectDefinitionPublish(
			@GraphQLName("objectDefinitionId") Long objectDefinitionId)
		throws Exception {

		_applyVoidComponentServiceObjects(
			_objectDefinitionResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectDefinitionResource ->
				objectDefinitionResource.postObjectDefinitionPublish(
					objectDefinitionId));

		return true;
	}

	@GraphQLField
	public ObjectField createObjectDefinitionObjectField(
			@GraphQLName("objectDefinitionId") Long objectDefinitionId,
			@GraphQLName("objectField") ObjectField objectField)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectFieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectFieldResource ->
				objectFieldResource.postObjectDefinitionObjectField(
					objectDefinitionId, objectField));
	}

	@GraphQLField
	public Response createObjectDefinitionObjectFieldBatch(
			@GraphQLName("objectDefinitionId") Long objectDefinitionId,
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectFieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectFieldResource ->
				objectFieldResource.postObjectDefinitionObjectFieldBatch(
					objectDefinitionId, callbackURL, object));
	}

	@GraphQLField
	public ObjectField patchObjectField(
			@GraphQLName("objectFieldId") Long objectFieldId,
			@GraphQLName("objectField") ObjectField objectField)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectFieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectFieldResource -> objectFieldResource.patchObjectField(
				objectFieldId, objectField));
	}

	@GraphQLField
	public ObjectField updateObjectField(
			@GraphQLName("objectFieldId") Long objectFieldId,
			@GraphQLName("objectField") ObjectField objectField)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectFieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectFieldResource -> objectFieldResource.putObjectField(
				objectFieldId, objectField));
	}

	@GraphQLField
	public Response updateObjectFieldBatch(
			@GraphQLName("callbackURL") String callbackURL,
			@GraphQLName("object") Object object)
		throws Exception {

		return _applyComponentServiceObjects(
			_objectFieldResourceComponentServiceObjects,
			this::_populateResourceContext,
			objectFieldResource -> objectFieldResource.putObjectFieldBatch(
				callbackURL, object));
	}

	private <T, R, E1 extends Throwable, E2 extends Throwable> R
			_applyComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeFunction<T, R, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			return unsafeFunction.apply(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private <T, E1 extends Throwable, E2 extends Throwable> void
			_applyVoidComponentServiceObjects(
				ComponentServiceObjects<T> componentServiceObjects,
				UnsafeConsumer<T, E1> unsafeConsumer,
				UnsafeConsumer<T, E2> unsafeFunction)
		throws E1, E2 {

		T resource = componentServiceObjects.getService();

		try {
			unsafeConsumer.accept(resource);

			unsafeFunction.accept(resource);
		}
		finally {
			componentServiceObjects.ungetService(resource);
		}
	}

	private void _populateResourceContext(
			ObjectDefinitionResource objectDefinitionResource)
		throws Exception {

		objectDefinitionResource.setContextAcceptLanguage(_acceptLanguage);
		objectDefinitionResource.setContextCompany(_company);
		objectDefinitionResource.setContextHttpServletRequest(
			_httpServletRequest);
		objectDefinitionResource.setContextHttpServletResponse(
			_httpServletResponse);
		objectDefinitionResource.setContextUriInfo(_uriInfo);
		objectDefinitionResource.setContextUser(_user);
		objectDefinitionResource.setGroupLocalService(_groupLocalService);
		objectDefinitionResource.setRoleLocalService(_roleLocalService);
	}

	private void _populateResourceContext(
			ObjectFieldResource objectFieldResource)
		throws Exception {

		objectFieldResource.setContextAcceptLanguage(_acceptLanguage);
		objectFieldResource.setContextCompany(_company);
		objectFieldResource.setContextHttpServletRequest(_httpServletRequest);
		objectFieldResource.setContextHttpServletResponse(_httpServletResponse);
		objectFieldResource.setContextUriInfo(_uriInfo);
		objectFieldResource.setContextUser(_user);
		objectFieldResource.setGroupLocalService(_groupLocalService);
		objectFieldResource.setRoleLocalService(_roleLocalService);
	}

	private static ComponentServiceObjects<ObjectDefinitionResource>
		_objectDefinitionResourceComponentServiceObjects;
	private static ComponentServiceObjects<ObjectFieldResource>
		_objectFieldResourceComponentServiceObjects;

	private AcceptLanguage _acceptLanguage;
	private com.liferay.portal.kernel.model.Company _company;
	private GroupLocalService _groupLocalService;
	private HttpServletRequest _httpServletRequest;
	private HttpServletResponse _httpServletResponse;
	private RoleLocalService _roleLocalService;
	private BiFunction<Object, String, Sort[]> _sortsBiFunction;
	private UriInfo _uriInfo;
	private com.liferay.portal.kernel.model.User _user;

}