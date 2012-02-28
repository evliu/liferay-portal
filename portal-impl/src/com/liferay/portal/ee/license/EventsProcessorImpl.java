/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.ee.license;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.license.LicenseManager;
import com.liferay.portal.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Shuyang Zhou
 * @author Amos Fong
 */
public class EventsProcessorImpl
	extends com.liferay.portal.events.EventsProcessorImpl {

	public void process(
			String key, String[] classes, String[] ids,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session)
		throws ActionException {

		super.process(key, classes, ids, request, response, session);

		if (!key.equals(PropsKeys.SERVLET_SERVICE_EVENTS_PRE)) {
			return;
		}

		String path = request.getRequestURI();

		int licenseState = LicenseManager.getLicenseState(request);

		try {
			if ((licenseState != LicenseManager.STATE_GOOD) &&
				!path.contains("/portal/license")) {

				response.sendRedirect(
					PortalUtil.getPathContext() + "/c/portal/license");
			}
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

}