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
 * @author Shuyang Zhou
 * @author Amos Fong
 */
public class DeveloperValidator extends LicenseValidator {

	@Override
	public void doValidateVersion2(License license) throws Exception {
		_doValidate(license);
	}

	@Override
	public void doValidateVersion3(License license) throws Exception {
		_doValidate(license);
	}

	@Override
	public String[] getValidTypes() {
		return _VALID_TYPES;
	}

	private void _doValidate(License license) throws Exception {
		if (license.getMaxHttpSessions() <= 0) {
			throw new Exception(
				"The maximum HTTP sessions must be greater than 0");
		}

		if (isClustered()) {
			_log.error(
				"Clustering has been detected. Developer licenses do not " +
					"allow for clustering. Local server is shutting down.");

			System.exit(EXIT_CODE_CLUSTER_FORBIDDEN);
		}
	}

	private static final String[] _VALID_TYPES = {License.TYPE_DEVELOPER};

	private static Log _log = LogFactoryUtil.getLog(DeveloperValidator.class);

}