/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.action;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.admin.util.OmniadminUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Amos Fong
 */
public class UpdateLicenseAction extends Action {

	@Override
	public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		String cmd = com.liferay.portal.kernel.util.ParamUtil.getString (request, "cmd");

		if (cmd.equals("validateState")) {
			com.liferay.portal.license.LicenseManager.writeKey(request, response);

			return null;
		}

		if (_isValidRequest (request)) {
			if (cmd.equals("licenseProperties")) {
				String clusterNodeId = com.liferay.portal.kernel.util.ParamUtil.getString(request, "clusterNodeId");

				java.util.List<java.util.Map<String, String>> licenseProperties = com.liferay.portal.license.LicenseManager.getClusterLicenseProperties(clusterNodeId);

				com.liferay.portal.kernel.json.JSONArray jsonArray = com.liferay.portal.kernel.json.JSONFactoryUtil.createJSONArray();

				if (licenseProperties != null) {
					for (java.util.Map<String, String> propertiesMap : licenseProperties) {
						com.liferay.portal.kernel.json.JSONObject jsonObject = com.liferay.portal.kernel.json.JSONFactoryUtil.createJSONObject();

						for (java.util.Map.Entry<String, String> entry : propertiesMap.entrySet()) {
							jsonObject.put(entry.getKey(), entry.getValue());
						}

						jsonArray.put(jsonObject);
					}
				}

				response.setContentType(com.liferay.portal.kernel.util.ContentTypes.TEXT_JAVASCRIPT);

				com.liferay.portal.kernel.servlet.ServletResponseUtil.write(response, jsonArray.toString());

				return null;
			}
			else if (cmd.equals("serverInfo")) {
				String clusterNodeId = com.liferay.portal.kernel.util.ParamUtil.getString(request, "clusterNodeId");

				java.util.Map<String, String> serverInfo = com.liferay.portal.license.LicenseManager.getClusterServerInfo(clusterNodeId);

				com.liferay.portal.kernel.json.JSONObject jsonObject = com.liferay.portal.kernel.json.JSONFactoryUtil.createJSONObject();

				if (serverInfo != null) {
					for (java.util.Map.Entry<String, String> entry : serverInfo.entrySet()) {
						jsonObject.put(entry.getKey(), entry.getValue());
					}
				}

				response.setContentType(com.liferay.portal.kernel.util.ContentTypes.TEXT_JAVASCRIPT);

				com.liferay.portal.kernel.servlet.ServletResponseUtil.write(response, jsonObject.toString());

				return null;
			}
			return mapping.findForward("portal.license");
		}
		else {
			response.sendRedirect(
				PortalUtil.getPathContext() + "/c/portal/layout");

			return null;
		}
	}

	private boolean _isOmniAdmin(HttpServletRequest request) {
		User user = null;

		try {
			user = PortalUtil.getUser(request);
		}
		catch (Exception e) {
		}

		if ((user != null) && OmniadminUtil.isOmniadmin(user.getUserId())) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean _isValidRequest(HttpServletRequest request) {
		int licenseState = com.liferay.portal.license.LicenseManager.getLicenseState(request);

		if (licenseState != com.liferay.portal.license.LicenseManager.STATE_GOOD) {
			com.liferay.portal.license.LicenseManager.registerOrder(request);

			licenseState = com.liferay.portal.license.LicenseManager.getLicenseState(request);

			if (licenseState != com.liferay.portal.license.LicenseManager.STATE_GOOD) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (_isOmniAdmin (request)) {
			com.liferay.portal.license.LicenseManager.registerOrder(request);


			return true;
		}
		else {
			return false;
		}
	}

}