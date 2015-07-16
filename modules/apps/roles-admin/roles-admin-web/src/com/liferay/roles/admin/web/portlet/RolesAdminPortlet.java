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

package com.liferay.roles.admin.web.portlet;

import com.liferay.portal.DuplicateRoleException;
import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.RequiredRoleException;
import com.liferay.portal.RoleAssignmentException;
import com.liferay.portal.RoleNameException;
import com.liferay.portal.RolePermissionsException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.ResourceActionsUtil;
import com.liferay.portal.security.permission.comparator.ActionComparator;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockLocalServiceUtil;
import com.liferay.portal.service.ResourceBlockServiceUtil;
import com.liferay.portal.service.ResourcePermissionServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.service.UserServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletCategoryKeys;
import com.liferay.portal.util.PortletKeys;
import com.liferay.roles.admin.web.constants.RolesAdminPortletKeys;
import com.liferay.roles.admin.web.upgrade.RolesAdminWebUpgrade;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Jorge Ferrer
 * @author Connor McKay
 * @author Drew Brokke
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.control-panel-entry-category=users",
		"com.liferay.portlet.control-panel-entry-weight=3.0",
		"com.liferay.portlet.css-class-wrapper=portlet-users-admin",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.footer-portlet-javascript=/js/main.js",
		"com.liferay.portlet.icon=/icons/roles_admin.png",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Roles Admin",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + RolesAdminPortletKeys.ROLES_ADMIN,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = javax.portlet.Portlet.class
)
public class RolesAdminPortlet extends MVCPortlet {

	public void deletePermission(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long roleId = ParamUtil.getLong(actionRequest, "roleId");
		String name = ParamUtil.getString(actionRequest, "name");
		int scope = ParamUtil.getInteger(actionRequest, "scope");
		String primKey = ParamUtil.getString(actionRequest, "primKey");
		String actionId = ParamUtil.getString(actionRequest, "actionId");

		Role role = RoleLocalServiceUtil.getRole(roleId);

		String roleName = role.getName();

		if (roleName.equals(RoleConstants.ADMINISTRATOR) ||
			roleName.equals(RoleConstants.ORGANIZATION_ADMINISTRATOR) ||
			roleName.equals(RoleConstants.ORGANIZATION_OWNER) ||
			roleName.equals(RoleConstants.OWNER) ||
			roleName.equals(RoleConstants.SITE_ADMINISTRATOR) ||
			roleName.equals(RoleConstants.SITE_OWNER)) {

			throw new RolePermissionsException(roleName);
		}

		if (ResourceBlockLocalServiceUtil.isSupported(name)) {
			if (scope == ResourceConstants.SCOPE_GROUP) {
				ResourceBlockServiceUtil.removeGroupScopePermission(
					themeDisplay.getScopeGroupId(), themeDisplay.getCompanyId(),
					GetterUtil.getLong(primKey), name, roleId, actionId);
			}
			else {
				ResourceBlockServiceUtil.removeCompanyScopePermission(
					themeDisplay.getScopeGroupId(), themeDisplay.getCompanyId(),
					name, roleId, actionId);
			}
		}
		else {
			ResourcePermissionServiceUtil.removeResourcePermission(
				themeDisplay.getScopeGroupId(), themeDisplay.getCompanyId(),
				name, scope, primKey, roleId, actionId);
		}

		// Send redirect

		SessionMessages.add(actionRequest, "permissionDeleted");

		String redirect = PortalUtil.escapeRedirect(
			ParamUtil.getString(actionRequest, "redirect"));

		if (Validator.isNotNull(redirect)) {
			actionResponse.sendRedirect(redirect);
		}
	}

	public void deleteRole(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long roleId = ParamUtil.getLong(actionRequest, "roleId");

		RoleServiceUtil.deleteRole(roleId);
	}

	public Role editRole(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long roleId = ParamUtil.getLong(actionRequest, "roleId");

		String name = ParamUtil.getString(actionRequest, "name");
		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "title");
		Map<Locale, String> descriptionMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "description");
		int type = ParamUtil.getInteger(
			actionRequest, "type", RoleConstants.TYPE_REGULAR);
		String subtype = ParamUtil.getString(actionRequest, "subtype");
		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			Role.class.getName(), actionRequest);

		if (roleId <= 0) {

			// Add role

			return RoleServiceUtil.addRole(
				null, 0, name, titleMap, descriptionMap, type, subtype,
				serviceContext);
		}
		else {

			// Update role

			return RoleServiceUtil.updateRole(
				roleId, name, titleMap, descriptionMap, subtype,
				serviceContext);
		}
	}

	public void editRoleAssignments(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long roleId = ParamUtil.getLong(actionRequest, "roleId");
		Role role = RoleLocalServiceUtil.getRole(roleId);

		if (role.getName().equals(RoleConstants.OWNER)) {
			throw new RoleAssignmentException(role.getName());
		}

		long[] addUserIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "addUserIds"), 0L);
		long[] removeUserIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "removeUserIds"), 0L);

		if (!ArrayUtil.isEmpty(addUserIds) ||
			!ArrayUtil.isEmpty(removeUserIds)) {

			UserServiceUtil.addRoleUsers(roleId, addUserIds);
			UserServiceUtil.unsetRoleUsers(roleId, removeUserIds);
		}

		long[] addGroupIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "addGroupIds"), 0L);
		long[] removeGroupIds = StringUtil.split(
			ParamUtil.getString(actionRequest, "removeGroupIds"), 0L);

		if (!ArrayUtil.isEmpty(addGroupIds) ||
			!ArrayUtil.isEmpty(removeGroupIds)) {

			GroupServiceUtil.addRoleGroups(roleId, addGroupIds);
			GroupServiceUtil.unsetRoleGroups(roleId, removeGroupIds);
		}
	}

	public void updateActions(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long roleId = ParamUtil.getLong(actionRequest, "roleId");

		Role role = RoleLocalServiceUtil.getRole(roleId);

		String roleName = role.getName();

		if (roleName.equals(RoleConstants.ADMINISTRATOR) ||
			roleName.equals(RoleConstants.ORGANIZATION_ADMINISTRATOR) ||
			roleName.equals(RoleConstants.ORGANIZATION_OWNER) ||
			roleName.equals(RoleConstants.OWNER) ||
			roleName.equals(RoleConstants.SITE_ADMINISTRATOR) ||
			roleName.equals(RoleConstants.SITE_OWNER)) {

			throw new RolePermissionsException(roleName);
		}

		String portletResource = ParamUtil.getString(
			actionRequest, "portletResource");
		String[] relatedPortletResources = StringUtil.split(
			ParamUtil.getString(actionRequest, "relatedPortletResources"));
		String[] modelResources = StringUtil.split(
			ParamUtil.getString(actionRequest, "modelResources"));

		Map<String, List<String>> resourceActionsMap = new HashMap<>();

		if (Validator.isNotNull(portletResource)) {
			resourceActionsMap.put(
				portletResource,
				ResourceActionsUtil.getResourceActions(portletResource, null));
		}

		for (String relatedPortletResource : relatedPortletResources) {
			resourceActionsMap.put(
				relatedPortletResource,
				ResourceActionsUtil.getResourceActions(
					relatedPortletResource, null));
		}

		for (String modelResource : modelResources) {
			resourceActionsMap.put(
				modelResource,
				ResourceActionsUtil.getResourceActions(null, modelResource));
		}

		int rootResourceScope = ResourceConstants.SCOPE_COMPANY;
		String[] rootResourceGroupIds = null;

		String[] selectedTargets = StringUtil.split(
			ParamUtil.getString(actionRequest, "selectedTargets"));
		String[] unselectedTargets = StringUtil.split(
			ParamUtil.getString(actionRequest, "unselectedTargets"));

		for (Map.Entry<String, List<String>> entry :
				resourceActionsMap.entrySet()) {

			String selResource = entry.getKey();
			List<String> actions = entry.getValue();

			actions = ListUtil.sort(
				actions, new ActionComparator(themeDisplay.getLocale()));

			for (String actionId : actions) {
				String target = selResource + actionId;

				boolean selected = ArrayUtil.contains(selectedTargets, target);

				if (!selected &&
					!ArrayUtil.contains(unselectedTargets, target)) {

					continue;
				}

				String[] groupIds = StringUtil.split(
					ParamUtil.getString(actionRequest, "groupIds" + target));

				groupIds = ArrayUtil.distinct(groupIds);

				int scope = ResourceConstants.SCOPE_COMPANY;

				if ((role.getType() == RoleConstants.TYPE_ORGANIZATION) ||
					(role.getType() == RoleConstants.TYPE_PROVIDER) ||
					(role.getType() == RoleConstants.TYPE_SITE)) {

					scope = ResourceConstants.SCOPE_GROUP_TEMPLATE;
				}
				else {
					if (groupIds.length > 0) {
						scope = ResourceConstants.SCOPE_GROUP;
					}
				}

				if (ResourceBlockLocalServiceUtil.isSupported(selResource)) {
					updateActions_Blocks(
						role, themeDisplay.getScopeGroupId(), selResource,
						actionId, selected, scope, groupIds);
				}
				else {
					updateAction(
						role, themeDisplay.getScopeGroupId(), selResource,
						actionId, selected, scope, groupIds);
				}

				if (selected &&
					actionId.equals(ActionKeys.ACCESS_IN_CONTROL_PANEL)) {

					updateViewControlPanelPermission(
						role, themeDisplay.getScopeGroupId(), selResource,
						scope, groupIds);

					rootResourceScope = scope;
					rootResourceGroupIds = groupIds;
				}
			}
		}

		// LPS-38031

		if (rootResourceGroupIds != null) {
			updateViewRootResourcePermission(
				role, themeDisplay.getScopeGroupId(), portletResource,
				rootResourceScope, rootResourceGroupIds);
		}

		// Send redirect

		SessionMessages.add(actionRequest, "permissionsUpdated");

		String redirect = PortalUtil.escapeRedirect(
			ParamUtil.getString(actionRequest, "redirect"));

		if (Validator.isNotNull(redirect)) {
			actionResponse.sendRedirect(redirect);
		}
	}

	@Override
	protected void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		long roleId = ParamUtil.getLong(renderRequest, "roleId");

		if (SessionErrors.contains(
				renderRequest, RequiredRoleException.class.getName()) &&
			(roleId < 1)) {

			include(
				"/html/portlet/roles_admin/view.jsp", renderRequest,
				renderResponse);
		}
		else if (SessionErrors.contains(
					renderRequest, DuplicateRoleException.class.getName()) ||
				 SessionErrors.contains(
					 renderRequest, RequiredRoleException.class.getName()) ||
				 SessionErrors.contains(
					 renderRequest, RoleNameException.class.getName())) {

			include(
				"/html/portlet/roles_admin/edit_role.jsp", renderRequest,
				renderResponse);
		}
		else if (SessionErrors.contains(
					renderRequest, NoSuchRoleException.class.getName()) ||
				 SessionErrors.contains(
					 renderRequest, PrincipalException.getNestedClasses()) ||
				 SessionErrors.contains(
					 renderRequest, RoleAssignmentException.class.getName()) ||
				 SessionErrors.contains(
					 renderRequest, RolePermissionsException.class.getName())) {

			include(
				"/html/portlet/roles_admin/error.jsp", renderRequest,
				renderResponse);
		}
		else {
			super.doDispatch(renderRequest, renderResponse);
		}
	}

	@Override
	protected boolean isSessionErrorException(Throwable cause) {
		if (cause instanceof DuplicateRoleException ||
			cause instanceof NoSuchRoleException ||
			cause instanceof PrincipalException ||
			cause instanceof RequiredRoleException ||
			cause instanceof RoleAssignmentException ||
			cause instanceof RoleNameException ||
			cause instanceof RolePermissionsException) {

			return true;
		}

		return false;
	}

	@Reference(unbind = "-")
	protected void setRolesAdminWebUpgrade(
		RolesAdminWebUpgrade rolesAdminWebUpgrade) {
	}

	protected void updateAction(
			Role role, long groupId, String selResource, String actionId,
			boolean selected, int scope, String[] groupIds)
		throws Exception {

		long companyId = role.getCompanyId();
		long roleId = role.getRoleId();

		if (selected) {
			if (scope == ResourceConstants.SCOPE_COMPANY) {
				ResourcePermissionServiceUtil.addResourcePermission(
					groupId, companyId, selResource, scope,
					String.valueOf(role.getCompanyId()), roleId, actionId);
			}
			else if (scope == ResourceConstants.SCOPE_GROUP_TEMPLATE) {
				ResourcePermissionServiceUtil.addResourcePermission(
					groupId, companyId, selResource,
					ResourceConstants.SCOPE_GROUP_TEMPLATE,
					String.valueOf(GroupConstants.DEFAULT_PARENT_GROUP_ID),
					roleId, actionId);
			}
			else if (scope == ResourceConstants.SCOPE_GROUP) {
				ResourcePermissionServiceUtil.removeResourcePermissions(
					groupId, companyId, selResource,
					ResourceConstants.SCOPE_GROUP, roleId, actionId);

				for (String curGroupId : groupIds) {
					ResourcePermissionServiceUtil.addResourcePermission(
						groupId, companyId, selResource,
						ResourceConstants.SCOPE_GROUP, curGroupId, roleId,
						actionId);
				}
			}
		}
		else {

			// Remove company, group template, and group permissions

			ResourcePermissionServiceUtil.removeResourcePermissions(
				groupId, companyId, selResource,
				ResourceConstants.SCOPE_COMPANY, roleId, actionId);

			ResourcePermissionServiceUtil.removeResourcePermissions(
				groupId, companyId, selResource,
				ResourceConstants.SCOPE_GROUP_TEMPLATE, roleId, actionId);

			ResourcePermissionServiceUtil.removeResourcePermissions(
				groupId, companyId, selResource, ResourceConstants.SCOPE_GROUP,
				roleId, actionId);
		}
	}

	protected void updateActions_Blocks(
			Role role, long scopeGroupId, String selResource, String actionId,
			boolean selected, int scope, String[] groupIds)
		throws Exception {

		long companyId = role.getCompanyId();
		long roleId = role.getRoleId();

		if (selected) {
			if (scope == ResourceConstants.SCOPE_GROUP) {
				ResourceBlockServiceUtil.removeAllGroupScopePermissions(
					scopeGroupId, companyId, selResource, roleId, actionId);
				ResourceBlockServiceUtil.removeCompanyScopePermission(
					scopeGroupId, companyId, selResource, roleId, actionId);

				for (String groupId : groupIds) {
					ResourceBlockServiceUtil.addGroupScopePermission(
						scopeGroupId, companyId, GetterUtil.getLong(groupId),
						selResource, roleId, actionId);
				}
			}
			else {
				ResourceBlockServiceUtil.removeAllGroupScopePermissions(
					scopeGroupId, companyId, selResource, roleId, actionId);
				ResourceBlockServiceUtil.addCompanyScopePermission(
					scopeGroupId, companyId, selResource, roleId, actionId);
			}
		}
		else {
			ResourceBlockServiceUtil.removeAllGroupScopePermissions(
				scopeGroupId, companyId, selResource, roleId, actionId);
			ResourceBlockServiceUtil.removeCompanyScopePermission(
				scopeGroupId, companyId, selResource, roleId, actionId);
		}
	}

	protected void updateViewControlPanelPermission(
			Role role, long scopeGroupId, String portletId, int scope,
			String[] groupIds)
		throws Exception {

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			role.getCompanyId(), portletId);

		String controlPanelCategory = portlet.getControlPanelEntryCategory();

		if (Validator.isNull(controlPanelCategory)) {
			return;
		}

		String selResource = null;
		String actionId = null;

		if (ArrayUtil.contains(PortletCategoryKeys.ALL, controlPanelCategory) &&
			(role.getType() == RoleConstants.TYPE_REGULAR)) {

			selResource = PortletKeys.PORTAL;
			actionId = ActionKeys.VIEW_CONTROL_PANEL;
		}
		else if (ArrayUtil.contains(
					PortletCategoryKeys.SITE_ADMINISTRATION_ALL,
					controlPanelCategory)) {

			selResource = Group.class.getName();
			actionId = ActionKeys.VIEW_SITE_ADMINISTRATION;
		}

		if (selResource != null) {
			updateAction(
				role, scopeGroupId, selResource, actionId, true, scope,
				groupIds);
		}
	}

	protected void updateViewRootResourcePermission(
			Role role, long scopeGroupId, String portletId, int scope,
			String[] groupIds)
		throws Exception {

		String modelResource = ResourceActionsUtil.getPortletRootModelResource(
			portletId);

		if (modelResource != null) {
			List<String> actions = ResourceActionsUtil.getModelResourceActions(
				modelResource);

			if (actions.contains(ActionKeys.VIEW)) {
				updateAction(
					role, scopeGroupId, modelResource, ActionKeys.VIEW, true,
					scope, groupIds);
			}
		}
	}

}