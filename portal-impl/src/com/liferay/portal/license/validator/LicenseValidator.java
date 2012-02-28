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

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.license.License;
import com.liferay.portal.license.LicenseManager;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Tina Tian
 * @author Amos Fong
 */
public class LicenseValidator {

	public void doValidateVersion1(License license) throws Exception {
	}

	public void doValidateVersion2(License license) throws Exception {
	}

	public void doValidateVersion3(License license) throws Exception {
	}

	public String[] getValidTypes() {
		return _VALID_TYPES;
	}

	public void setNextValidator(LicenseValidator nextValidator) {
		_nextValidator = nextValidator;
	}

	public void validate(License license) throws Exception {
		String[] validTypes = getValidTypes();

		if (ArrayUtil.contains(validTypes, license.getLicenseEntryType())) {
			String version = license.getLicenseVersion();

			if (version.equals("1")) {
				doValidateVersion1(license);
			}
			else if (version.equals("2")) {
				doValidateVersion2(license);
			}
			else {
				doValidateVersion3(license);
			}
		}

		if (_nextValidator != null) {
			_nextValidator.validate(license);
		}
	}

	protected boolean isClustered() {
		if (PropsValues.CLUSTER_LINK_ENABLED) {
			return true;
		}
		else {
			return false;
		}
	}

	protected void validateHostNames(String[] hostNames) throws Exception {
		List<String> allowedHostNames = new ArrayList<String>();

		for (String hostName : hostNames) {
			allowedHostNames.add(hostName.toLowerCase());
		}

		if ((allowedHostNames == null) || allowedHostNames.isEmpty()) {
			throw new Exception(
				"Your license does not have any allowed host names");
		}

		String localHostName = LicenseManager.getHostName();

		if (!allowedHostNames.contains(localHostName.toLowerCase())) {
			throw new Exception(
				"Host name matching failed. Allowed host names: " +
					StringUtil.merge(hostNames));
		}
	}

	protected void validateIpAddresses(String[] ipAddresses) throws Exception {
		List<String> allowedIpAddresses = ListUtil.fromArray(ipAddresses);

		if ((allowedIpAddresses == null) || allowedIpAddresses.isEmpty()) {
			throw new Exception(
				"Your license does not have any allowed IP addresses");
		}

		Set<String> localIpAddresses = LicenseManager.getIpAddresses();

		if (localIpAddresses.isEmpty()) {
			throw new Exception("Unable to read local server's IP addresses");
		}

		localIpAddresses.retainAll(allowedIpAddresses);

		if (localIpAddresses.isEmpty()) {
			throw new Exception(
				"IP address matching failed. Allowed IP addresses: " +
					allowedIpAddresses);
		}
	}

	protected void validateMacAddresses(String[] macAddresses)
		throws Exception {

		List<String> allowedMacAddresses = new ArrayList<String>();

		for (String macAddress : macAddresses) {
			allowedMacAddresses.add(macAddress.toLowerCase());
		}

		if ((allowedMacAddresses == null) || allowedMacAddresses.isEmpty()) {
			throw new Exception(
				"Your license does not have any allowed MAC addresses");
		}

		Set<String> localMacAddresses = LicenseManager.getMacAddresses();

		if (localMacAddresses.isEmpty()) {
			throw new Exception("Unable to read local server's MAC addresses");
		}

		localMacAddresses.retainAll(allowedMacAddresses);

		if (localMacAddresses.isEmpty()) {
			throw new Exception(
				"MAC address matching failed. Allowed MAC addresses: " +
					allowedMacAddresses);
		}
	}

	protected void validateServer(License license) throws Exception {
		try {
			validateHostNames(license.getHostNames());
		}
		catch (Exception e1) {
			try {
				validateIpAddresses(license.getIpAddresses());
			}
			catch (Exception e2) {
				validateMacAddresses(license.getMacAddresses());
			}
		}
	}

	protected static final int EXIT_CODE_CLUSTER_FORBIDDEN = -30;

	protected static final int EXIT_CODE_EXCEEDED_MAX_SERVERS = -20;

	protected static final int EXIT_CODE_INCONSISTENT_LICENSES = -10;

	protected LicenseValidator _nextValidator;

	private static final String[] _VALID_TYPES = License.TYPES;

}