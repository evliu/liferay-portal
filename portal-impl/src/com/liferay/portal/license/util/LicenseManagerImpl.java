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

package com.liferay.portal.license.util;

import com.liferay.portal.license.LicenseInfo;
import com.liferay.portal.license.LicenseManager;

import java.util.Set;

/**
 * @author Amos Fong
 */
public class LicenseManagerImpl
	implements com.liferay.portal.license.util.LicenseManager {

	public void checkLicense(String productId) {
		LicenseManager.checkBinaryLicense(productId);
	}

	public String getHostName() {
		return LicenseManager.getHostName();
	}

	public Set<String> getIpAddresses() {
		return LicenseManager.getIpAddresses();
	}

	public LicenseInfo getLicenseInfo(String productId) {
		return LicenseManager.getLicenseInfo(productId);
	}

	public int getLicenseState(String productId) {
		return LicenseManager.getLicenseState(productId);
	}

	public Set<String> getMacAddresses() {
		return LicenseManager.getMacAddresses();
	}

}