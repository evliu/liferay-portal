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

import com.liferay.portal.license.License;

/**
 * @author Tina Tian
 * @author Amos Fong
 */
public class LicenseTypeValidator extends LicenseValidator {

	@Override
	public void doValidateVersion1(License license) throws Exception {
		_doValidate(license);
	}

	@Override
	public void doValidateVersion2(License license) throws Exception {
		_doValidate(license);
	}

	@Override
	public void doValidateVersion3(License license) throws Exception {
		_doValidate(license);
	}

	private void _doValidate(License license) throws Exception {
		String licenseEntryType = license.getLicenseEntryType();

		if (licenseEntryType.equals(License.TYPE_CLUSTER) ||
			licenseEntryType.equals(License.TYPE_DEVELOPER) ||
			licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER) ||
			licenseEntryType.equals(License.TYPE_ENTERPRISE) ||
			licenseEntryType.equals(License.TYPE_LIMITED) ||
			licenseEntryType.equals(License.TYPE_OEM) ||
			licenseEntryType.equals(License.TYPE_PER_USER) ||
			licenseEntryType.equals(License.TYPE_PRODUCTION)) {

			return;
		}
		else {
			throw new Exception("Unknown license type " + licenseEntryType);
		}
	}

}