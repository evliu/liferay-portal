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
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.license.License;
import com.liferay.portal.license.LicenseManager;

import java.security.MessageDigest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Amos Fong
 */
public class KeyValidator {

	public static Map<String, String> getProperties(License license) {
		Date startDate = license.getStartDate();
		Date expirationDate = license.getExpirationDate();

		return _getProperties(
			license.getLicenseVersion(), license.getStartDate(),
			license.getAccountEntryName(), license.getLicenseEntryName(),
			license.getLicenseEntryType(), license.getProductEntryName(),
			license.getProductId(), license.getProductVersion(),
			license.getOwner(), license.getMaxServers(),
			license.getMaxHttpSessions(), license.getMaxConcurrentUsers(),
			license.getMaxUsers(), license.getDescription(),
			expirationDate.getTime() - startDate.getTime(),
			StringUtil.merge(license.getHostNames()),
			StringUtil.merge(license.getIpAddresses()),
			StringUtil.merge(license.getMacAddresses()),
			license.getServerIds());
	}

	public static License registerTrial(License license) {
		String licenseEntryType = license.getLicenseEntryType();

		if (!licenseEntryType.equals(License.TYPE_TRIAL)) {
			return license;
		}

		if (!validate(license)) {
			return license;
		}

		license.setLicenseEntryType(License.TYPE_DEVELOPER);

		Map<String, String> properties = getProperties(license);

		license.setKey(_instance._encrypt(properties));

		return license;
	}

	public static boolean validate(License license) {
		Map<String, String> properties = getProperties(license);

		String key = _instance._encrypt(properties);

		if (key.equals(_BANNED_KEY)) {
			return false;
		}

		if (key.equals(license.getKey())) {
			return true;
		}
		else {
			return false;
		}
	}

	private KeyValidator() {
	}

	private String _digest(String text, String algorithm) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

		messageDigest.update(text.getBytes());

		byte[] bytes = messageDigest.digest();

		StringBuilder sb = new StringBuilder(bytes.length << 1);

		for (int i = 0; i < bytes.length; i++) {
			int byte_ = bytes[i] & 0xff;

			sb.append(_HEX_CHARACTERS[byte_ >> 4]);
			sb.append(_HEX_CHARACTERS[byte_ & 0xf]);
		}

		return sb.toString();
	}

	private String _digestsToString(List<String> digests) {
		StringBundler sb = new StringBundler(digests.size());

		for (String digest : digests) {
			sb.append(digest);
		}

		return sb.toString();
	}

	private String _encrypt(Map<String, String> properties) {
		int licenseVersion = GetterUtil.getInteger(properties.get("version"));
		String productId = properties.get("productId");

		try {
			if (licenseVersion == 1) {
				throw new IllegalArgumentException(
					"Invalid version " + licenseVersion);
			}
			else if (licenseVersion >= 2) {
				return _encryptVersion2(productId, properties);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return StringPool.BLANK;
	}

	private String _encryptVersion2(
			String productId, Map<String, String> properties)
		throws Exception {

		List<String> keys = new ArrayList<String>(properties.keySet());

		Collections.sort(keys);

		List<String> digests = new ArrayList<String>(properties.size());

		for (int i = 0; i < keys.size(); i++) {
			String text = properties.get(keys.get(i));

			String algorithm = _getAlgorithm(productId, i);

			String digest = _digest(text, algorithm);

			digests.add(digest);
		}

		digests = _shortenDigests(digests);

		for (int i = 0; i < digests.size(); i++) {
			String digest = digests.get(i);

			String algorithm = _getAlgorithm(productId, i);

			digest = _digest(digest, algorithm);

			digests.set(i, digest);
		}

		if (com.liferay.portal.kernel.util.Validator.isNull(productId) ||
			productId.equals(LicenseManager.PRODUCT_ID_PORTAL)) {

			return _interweaveDigest(digests);
		}
		else {
			return _digestsToString (digests);
		}
	}

	private String _getAlgorithm(String productId, int i) {
		if (com.liferay.portal.kernel.util.Validator.isNull(productId) ||
			productId.equals(LicenseManager.PRODUCT_ID_PORTAL)) {

			return _ALGORITHMS[i % _ALGORITHMS.length];
		}
		else {
			return _ALGORITHMS[2];
		}
	}

	private static Map<String, String> _getProperties(
		String licenseVersion, Date startDate, String accountEntryName,
		String licenseEntryName, String licenseEntryType,
		String productEntryName, String productId, String productVersion,
		String owner, int maxServers, int maxHttpSessions,
		long maxConcurrentUsers, long maxUsers, String description,
		long lifetime, String hostNames, String ipAddresses,
		String macAddresses, String[] serverIds) {

		Map<String, String> properties = new HashMap<String, String>();

		properties.put("version", licenseVersion);

		if (!licenseEntryType.equals(License.TYPE_TRIAL)) {
			properties.put("startDate", String.valueOf(startDate.getTime()));
		}

		properties.put("type", licenseEntryType);
		properties.put("owner", owner);
		properties.put("description", description);

		if (licenseEntryType.equals(License.TYPE_TRIAL)) {
			properties.put("lifetime", String.valueOf(lifetime));
		}
		else {
			properties.put(
				"expirationDate",
				String.valueOf(startDate.getTime() + lifetime));
		}

		if (licenseVersion.equals("1")) {
			properties.put("productVersion", productVersion);

			if (licenseEntryType.equals(License.TYPE_CLUSTER) ||
				licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER)) {

				for (int i = 0; i < serverIds.length; i++) {
					String serverId = StringUtil.replace(
						serverIds[i], StringPool.DASH, StringPool.COLON);

					serverId = serverId.trim().toLowerCase();

					properties.put("macAddress." + i, serverId);
				}
			}
			else if (licenseEntryType.equals(License.TYPE_PRODUCTION)) {
				String serverId = serverIds[0].trim();

				properties.put("serverId", serverId);
			}
		}
		else if (licenseVersion.equals("2")) {
			properties.put("productVersion", productVersion);
			properties.put("accountEntryName", accountEntryName);
			properties.put("licenseEntryName", licenseEntryName);
			properties.put("productEntryName", productEntryName);

			if (licenseEntryType.equals(License.TYPE_CLUSTER) ||
				licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER)) {

				properties.put("maxServers", String.valueOf(maxServers));
			}

			if (licenseEntryType.equals(License.TYPE_DEVELOPER) ||
				licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER) ||
				licenseEntryType.equals(License.TYPE_TRIAL)) {

				properties.put(
					"maxHttpSessions", String.valueOf(maxHttpSessions));
			}

			if (licenseEntryType.equals(License.TYPE_PRODUCTION)) {
				String serverIdsList = StringUtil.merge(serverIds);

				serverIdsList = serverIdsList.toLowerCase();
				serverIdsList = StringUtil.replace(
					serverIdsList, StringPool.DASH, StringPool.COLON);

				properties.put("serverIds", serverIdsList);
			}
		}
		else if (licenseVersion.equals("3")) {
			properties.put("productVersion", productVersion);

			if (productId.equals(LicenseManager.PRODUCT_ID_PORTAL)) {
				properties.put("accountEntryName", accountEntryName);
				properties.put("licenseEntryName", licenseEntryName);
			}
			else {
				properties.put("productId", productId);
			}

			properties.put("productEntryName", productEntryName);

			if (licenseEntryType.equals(License.TYPE_CLUSTER)) {
				properties.put("maxServers", String.valueOf(maxServers));
			}

			if (licenseEntryType.equals(License.TYPE_DEVELOPER) ||
				licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER)) {

				properties.put(
					"maxHttpSessions", String.valueOf(maxHttpSessions));
			}

			if (licenseEntryType.equals(License.TYPE_PER_USER)) {
				if (maxConcurrentUsers > 0) {
					properties.put(
						"maxConcurrentUsers",
						String.valueOf(maxConcurrentUsers));
				}

				if (maxUsers > 0) {
					properties.put("maxUsers", String.valueOf(maxUsers));
				}
			}

			if (licenseEntryType.equals(License.TYPE_CLUSTER) ||
				licenseEntryType.equals(License.TYPE_LIMITED) ||
				licenseEntryType.equals(License.TYPE_PER_USER) ||
				licenseEntryType.equals(License.TYPE_PRODUCTION)) {

				properties.put("hostNames", hostNames);
				properties.put("ipAddresses", ipAddresses);
				properties.put(
					"macAddresses",
					StringUtil.replace(
						macAddresses, StringPool.DASH, StringPool.COLON));
				properties.put("serverIds", StringUtil.merge(serverIds));
			}
		}

		return properties;
	}

	private String _interweaveDigest(List<String> digests) {
		int size = digests.size();

		int finalLength = 0;
		int shortestLength = Integer.MAX_VALUE;

		for (String digest : digests) {
			int length = digest.length();

			finalLength += length;

			if (length < shortestLength) {
				shortestLength = length;
			}
		}

		StringBuilder sb = new StringBuilder(finalLength);

		for (int i = 0; i < shortestLength; i++) {
			for (int j = 0; j < size; j++) {
				String digest = digests.get(j);

				sb.append(digest.charAt(i));
			}
		}

		for (String digest : digests) {
			if (digest.length() > shortestLength) {
				sb.append(digest.substring(shortestLength));
			}
		}

		return sb.toString();
	}

	private List<String> _shortenDigests (List<String> digests)
		throws Exception {

		int size = digests.size();

		int groupSize = size / 4;

		if ((groupSize * 4) < size) {
			groupSize++;
		}

		List<String> shortenedDigests = new ArrayList<String>(4);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < size; i++) {
			String digest = (String)digests.get(i);

			if ((i != 0) && ((i % groupSize)) == 0) {
				shortenedDigests.add(sb.toString());

				sb.setLength(0);
			}

			sb.append(digest);
		}

		if (shortenedDigests.size() < 4) {
			shortenedDigests.add(sb.toString());
		}

		return shortenedDigests;
	}

	private static final String[] _ALGORITHMS = {
		"MD5", "SHA-1", "SHA-256", "SHA-512"
	};

	private static final String _BANNED_KEY =
		"4a4beb2b97c151cff83cbca7096325086817360a7b8c912b66e1d1dea172033a8c59" +
		"34cbbacbf7b443496cc119a6a482fc6225d28bcbcb2384f52862e6fd35e49a2625f1" +
		"458d24a1f62e71235dc16b9de5a971e638af32a9784e566f33dd90234d89e1dde83e" +
		"8a4a100a70d999b2bb7fa77eeb34fd1be9cdf3645f9478b14c2cd6b8f955";

	private static final char[] _HEX_CHARACTERS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		'e', 'f'
	};

	private static Log _log = LogFactoryUtil.getLog(KeyValidator.class);

	private static KeyValidator _instance = new KeyValidator();

}