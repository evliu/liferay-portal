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

package com.liferay.portal.license.validator;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.license.License;

/**
 * @author Amos Fong
 */
public class LimitedValidator extends LicenseValidator {

	@Override
	public void doValidateVersion3(License license) throws Exception {
		if (isClustered()) {
			_log.error(
				"Clustering has been detected. Limited licenses do not " +
					"allow clustering. Local server is shutting down.");

			System.exit(EXIT_CODE_CLUSTER_FORBIDDEN);
		}

		validateServer(license);
	}

	@Override
	public String[] getValidTypes() {
		return _VALID_TYPES;
	}

	private static final String[] _VALID_TYPES = {License.TYPE_LIMITED};

	private static Log _log = LogFactoryUtil.getLog(LimitedValidator.class);

}