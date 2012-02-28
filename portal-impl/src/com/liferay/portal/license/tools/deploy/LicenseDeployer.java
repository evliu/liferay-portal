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

package com.liferay.portal.license.tools.deploy;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.license.License;
import com.liferay.portal.license.LicenseManager;
import com.liferay.portal.license.validator.KeyValidator;
import com.liferay.portal.util.InitUtil;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Shuyang Zhou
 * @author Simon Zhang
 * @author Tina Tian
 * @author Amos Fong
 */
public class LicenseDeployer {

	public static void main(String[] args) throws Exception {
		InitUtil.initWithSpring();

		File licenseFile = new File(args[0]);

		LicenseDeployer licenseDeployer = new LicenseDeployer(licenseFile);

		licenseDeployer.deploy();
	}

	public void deploy() throws Exception {
		License license = _parseLicenseFile();

		if (!KeyValidator.validate(license)) {
			_log.error(
				"Corrupt license file. License was not registered: " + license);

			return;
		}

		LicenseManager.writeBinaryLicense(license);

		LicenseManager.checkBinaryLicense(license.getProductId());

		if (_log.isInfoEnabled()) {
			_log.info("License registered");
		}
	}

	public LicenseDeployer() {
	}

	public LicenseDeployer(String licenseFileContent) {
		this.licenseFileContent = licenseFileContent;
	}

	protected LicenseDeployer(File licenseFile) throws IOException {
		licenseFileContent = FileUtil.read(licenseFile);
	}

	private License _parseLicenseFile() throws Exception {
		Document document = SAXReaderUtil.read(licenseFileContent);

		Element rootElement = document.getRootElement();

		String accountEntryName = GetterUtil.getString(
			rootElement.elementTextTrim("account-name"));
		String owner = GetterUtil.getString(
			rootElement.elementTextTrim("owner"));
		String description = GetterUtil.getString(
			rootElement.elementTextTrim("description"));
		String productEntryName = GetterUtil.getString(
			rootElement.elementTextTrim("product-name"));
		String productId = GetterUtil.getString(
			rootElement.elementTextTrim("product-id"),
			LicenseManager.PRODUCT_ID_PORTAL);
		String productVersion = GetterUtil.getString(
			rootElement.elementTextTrim("product-version"));
		String licenseEntryName = GetterUtil.getString(
			rootElement.elementTextTrim("license-name"));
		String licenseEntryType = GetterUtil.getString(
			rootElement.elementTextTrim("license-type"));
		String licenseVersion = GetterUtil.getString(
			rootElement.elementTextTrim("license-version"));

		DateFormat longDateFormatDateTime = DateFormat.getDateTimeInstance(
			DateFormat.FULL, DateFormat.FULL, Locale.US);

		Date startDate = null;

		if (licenseEntryType.equals(License.TYPE_TRIAL)) {
			startDate = new Date();
		}
		else {
			startDate = longDateFormatDateTime.parse(
				rootElement.elementTextTrim("start-date"));
		}

		Date expirationDate = null;

		if (licenseEntryType.equals(License.TYPE_TRIAL)) {
			long lifetime = GetterUtil.getLong(
				rootElement.elementTextTrim("lifetime"));

			expirationDate = new Date(startDate.getTime() + lifetime);
		}
		else {
			expirationDate = longDateFormatDateTime.parse(
				rootElement.elementTextTrim("expiration-date"));
		}

		int maxServers = GetterUtil.getInteger(
			rootElement.elementTextTrim("max-servers"));
		int maxHttpSessions = GetterUtil.getInteger(
			rootElement.elementTextTrim("max-http-sessions"));
		long maxConcurrentUsers = GetterUtil.getLong(
			rootElement.elementTextTrim("max-concurrent-users"));
		long maxUsers = GetterUtil.getLong(
			rootElement.elementTextTrim("max-users"));

		List<String> hostNames = new ArrayList<String>();
		List<String> ipAddresses = new ArrayList<String>();
		List<String> macAddresses = new ArrayList<String>();
		List<String> serverIds = new ArrayList<String>();

		_appendChildElementsArray(
			hostNames, rootElement.element("host-names"), "host-name");
		_appendChildElementsArray(
			ipAddresses, rootElement.element("ip-addresses"), "ip-address");
		_appendChildElementsArray(
			macAddresses, rootElement.element("mac-addresses"), "mac-address");
		_appendChildElementsArray(
			serverIds, rootElement.element("server-ids"), "server-id");

		Element serversElements = rootElement.element("servers");

		if (serversElements != null) {
			List<Element> serverElements = serversElements.elements("server");

			for (Element serverElement : serverElements) {
				_appendChildElementsArray(
					hostNames, serverElement.element("host-names"),
					"host-name");
				_appendChildElementsArray(
					ipAddresses, serverElement.element("ip-addresses"),
					"ip-address");
				_appendChildElementsArray(
					macAddresses, serverElement.element("mac-addresses"),
					"mac-address");
				_appendChildElementsArray(
					serverIds, serverElement.element("server-ids"),
					"server-id");
			}
		}

		String key = rootElement.elementTextTrim("key");

		License license = new License(
			accountEntryName, owner, description, productEntryName, productId,
			productVersion, licenseEntryName, licenseEntryType, licenseVersion,
			startDate, expirationDate, maxServers, maxHttpSessions,
			maxConcurrentUsers, maxUsers,
			hostNames.toArray(new String[hostNames.size()]),
			ipAddresses.toArray(new String[ipAddresses.size()]),
			macAddresses.toArray(new String[macAddresses.size()]),
			serverIds.toArray(new String[serverIds.size()]), key);

		if (licenseEntryType.equals(License.TYPE_TRIAL)) {
			license = KeyValidator.registerTrial(license);
		}

		return license;
	}

	private void _appendChildElementsArray(
		List<String> list, Element element, String childName) {

		if (element != null) {
			List<Element> childElements = element.elements(childName);

			for (Element childElement : childElements) {
				list.add(childElement.getTextTrim());
			}
		}
	}

	protected String licenseFileContent;

	private static Log _log = LogFactoryUtil.getLog(LicenseDeployer.class);

}