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
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.license.License;
import com.liferay.portal.util.PropsValues;

import java.io.File;

import java.util.Properties;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 * @author Amos Fong
 */
public class ProductionValidator extends LicenseValidator {

	@Override
	public void doValidateVersion1(License license) throws Exception {
		String[] serverIds = license.getServerIds();

		File licenseFile = new File(PropsValues.LIFERAY_HOME + "/ee/license");

		if (licenseFile.exists()) {
			String licenseFileEncoded = FileUtil.read(licenseFile);
			String licenseFileDecoded = (String)Base64.stringToObject(
				licenseFileEncoded);

			Properties licenseFileProperties = PropertiesUtil.load(
				licenseFileDecoded);

			String serverId = GetterUtil.getString(
				licenseFileProperties.getProperty("serverId"));

			if (!serverId.equals(serverIds[0])) {
				throw new Exception("Server id matching failed");
			}
		}
	}

	@Override
	public void doValidateVersion2(License license) throws Exception {
		if (isClustered()) {
			_log.error(
				"Clustering has been detected. Production licenses do not" +
					"allow clustering. Local server is shutting down.");

			System.exit(EXIT_CODE_CLUSTER_FORBIDDEN);
		}

		validateMacAddresses(license.getServerIds());
	}

	@Override
	public void doValidateVersion3(License license) throws Exception {
		validateServer(license);
	}

	@Override
	public String[] getValidTypes() {
		return _VALID_TYPES;
	}

	private static final String[] _VALID_TYPES = {License.TYPE_PRODUCTION};

	private static Log _log = LogFactoryUtil.getLog(ProductionValidator.class);

}