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

package com.liferay.portal.license;

import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.license.validator.KeyValidator;

import java.io.Serializable;

import java.util.Date;
import java.util.Map;

/**
 * @author Tina Tian
 * @author Shuyang Zhou
 * @author Amos Fong
 */
public class License implements Serializable, Comparable<License> {

	public static final String TYPE_CLUSTER = "cluster";

	public static final String TYPE_DEVELOPER = "developer";

	public static final String TYPE_DEVELOPER_CLUSTER = "developer-cluster";

	public static final String TYPE_ENTERPRISE = "enterprise";

	public static final String TYPE_LIMITED = "limited";

	public static final String TYPE_OEM = "oem";

	public static final String TYPE_PER_USER = "per-user";

	public static final String TYPE_PRODUCTION = "production";

	public static final String TYPE_TRIAL = "trial";

	public static final String[] TYPES = {
		TYPE_CLUSTER, TYPE_DEVELOPER, TYPE_DEVELOPER_CLUSTER, TYPE_ENTERPRISE,
		TYPE_LIMITED, TYPE_OEM, TYPE_PER_USER, TYPE_PRODUCTION
	};

	public License(
		String accountEntryName, String owner, String description,
		String productEntryName, String productId, String productVersion,
		String licenseEntryName, String licenseEntryType, String licenseVersion,
		Date startDate, Date expirationDate, int maxServers,
		int maxHttpSessions, long maxConcurrentUsers, long maxUsers,
		String[] hostNames, String[] ipAddresses, String[] macAddresses,
		String[] serverIds, String key) {

		_accountEntryName = accountEntryName;
		_description = description;
		_expirationDate = expirationDate;
		_hostNames = hostNames;
		_ipAddresses = ipAddresses;
		_key = key;
		_licenseEntryName = licenseEntryName;
		_licenseEntryType = licenseEntryType;
		_licenseVersion = licenseVersion;
		_macAddresses = macAddresses;
		_maxHttpSessions = maxHttpSessions;
		_maxServers = maxServers;
		_maxConcurrentUsers = maxConcurrentUsers;
		_maxUsers = maxUsers;
		_owner = owner;
		_productEntryName = productEntryName;
		_productId = productId;
		_productVersion = productVersion;
		_serverIds = serverIds;
		_startDate = startDate;
	}

	public int compareTo(License license) {
		boolean expired = isExpired();

		if (!Validator.equals(expired, license.isExpired())) {
			if (expired) {
				return 1;
			}
			else {
				return -1;
			}
		}

		int result =
			getLicenseEntryTypeRank() - license.getLicenseEntryTypeRank();

		if (result != 0) {
			return result;
		}

		if (_licenseEntryType.equals(TYPE_CLUSTER) ||
			_licenseEntryType.equals(TYPE_DEVELOPER_CLUSTER)) {

			result = _maxServers - license.getMaxServers();
		}
		else if (_licenseEntryType.equals(TYPE_PRODUCTION)) {
			String[] serverIds = license.getServerIds();

			result = _serverIds.length - serverIds.length;
		}

		if (result != 0) {
			return result;
		}

		if (_expirationDate.after(license.getExpirationDate())) {
			return -1;
		}
		else {
			return 1;
		}
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof License)) {
			return false;
		}

		License license = (License)obj;

		if (Validator.equals(_key, license.getKey())) {
			return true;
		}

		return false;
	}

	public String getAccountEntryName() {
		return _accountEntryName;
	}

	public String getDescription() {
		return _description;
	}

	public Date getExpirationDate() {
		return _expirationDate;
	}

	public String[] getHostNames() {
		return _hostNames;
	}

	public String[] getIpAddresses() {
		return _ipAddresses;
	}

	public String getKey() {
		return _key;
	}

	public long getLastAccessedTime() {
		return _lastAccessedTime;
	}

	public String getLicenseEntryName() {
		return _licenseEntryName;
	}

	public String getLicenseEntryType() {
		return _licenseEntryType;
	}

	public int getLicenseEntryTypeRank() {
		if (_licenseEntryType.equals(TYPE_CLUSTER)) {
			return 3;
		}
		else if (_licenseEntryType.equals(TYPE_DEVELOPER)) {
			return 8;
		}
		else if (_licenseEntryType.equals(TYPE_DEVELOPER_CLUSTER)) {
			return 7;
		}
		else if (_licenseEntryType.equals(TYPE_ENTERPRISE)) {
			return 2;
		}
		else if (_licenseEntryType.equals(TYPE_LIMITED)) {
			return 5;
		}
		else if (_licenseEntryType.equals(TYPE_OEM)) {
			return 1;
		}
		else if (_licenseEntryType.equals(TYPE_PER_USER)) {
			return 6;
		}
		else if (_licenseEntryType.equals(TYPE_PRODUCTION)) {
			return 4;
		}
		else {
			return 10;
		}
	}

	public String getLicenseVersion() {
		return _licenseVersion;
	}

	public String[] getMacAddresses() {
		return _macAddresses;
	}

	public long getMaxConcurrentUsers() {
		return _maxConcurrentUsers;
	}

	public int getMaxHttpSessions() {
		return _maxHttpSessions;
	}

	public int getMaxServers() {
		return _maxServers;
	}

	public long getMaxUsers() {
		return _maxUsers;
	}

	public String getOwner() {
		return _owner;
	}

	public String getProductEntryName() {
		return _productEntryName;
	}

	public String getProductId() {
		return _productId;
	}

	public String getProductVersion() {
		return _productVersion;
	}

	public String[] getServerIds() {
		return _serverIds;
	}

	public Date getStartDate() {
		return _startDate;
	}

	public int hashCode() {
		return _key.hashCode();
	}

	public boolean isExpired() {
		long now = System.currentTimeMillis();

		if ((now - (Time.DAY * 2)) > _expirationDate.getTime()) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setAccountEntryName(String accountEntryName) {
		_accountEntryName = accountEntryName;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setExpirationDate(Date expirationDate) {
		_expirationDate = expirationDate;
	}

	public void setHostNames(String[] hostNames) {
		_hostNames = hostNames;
	}

	public void setIpAddresses(String[] ipAddresses) {
		_ipAddresses = ipAddresses;
	}

	public void setKey(String key) {
		_key = key;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		_lastAccessedTime = lastAccessedTime;
	}

	public void setLicenseEntryName(String licenseEntryName) {
		_licenseEntryName = licenseEntryName;
	}

	public void setLicenseEntryType(String licenseEntryType) {
		_licenseEntryType = licenseEntryType;
	}

	public void setLicenseVersion(String licenseVersion) {
		_licenseVersion = licenseVersion;
	}

	public void setMacAddresses(String[] macAddresses) {
		_macAddresses = macAddresses;
	}

	public void setMaxConcurrentUsers(long maxConcurrentUsers) {
		_maxConcurrentUsers = maxConcurrentUsers;
	}

	public void setMaxHttpSessions(int maxHttpSessions) {
		_maxHttpSessions = maxHttpSessions;
	}

	public void setMaxServers(int maxServers) {
		_maxServers = maxServers;
	}

	public void setMaxUsers(int maxUsers) {
		_maxUsers = maxUsers;
	}

	public void setOwner(String owner) {
		_owner = owner;
	}

	public void setProductEntryName(String productEntryName) {
		_productEntryName = productEntryName;
	}

	public void setProductId(String productId) {
		_productId = productId;
	}

	public void setProductVersion(String productVersion) {
		_productVersion = productVersion;
	}

	public void setServerIds(String[] serverIds) {
		_serverIds = serverIds;
	}

	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	public String toString() {
		Map<String, String> properties = KeyValidator.getProperties(this);

		return properties.toString();
	}

	private static final long serialVersionUID = 2779848304210680862L;

	private String _accountEntryName;
	private String _description;
	private Date _expirationDate;
	private String[] _hostNames;
	private String[] _ipAddresses;
	private String _key;
	private long _lastAccessedTime;
	private String _licenseEntryName;
	private String _licenseEntryType;
	private String _licenseVersion;
	private String[] _macAddresses;
	private long _maxConcurrentUsers;
	private int _maxHttpSessions;
	private int _maxServers;
	private long _maxUsers;
	private String _owner;
	private String _productEntryName;
	private String _productId;
	private String _productVersion;
	private String[] _serverIds;
	private Date _startDate;

}