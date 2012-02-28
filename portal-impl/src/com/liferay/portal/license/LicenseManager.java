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

import com.liferay.portal.CompanyMaxUsersException;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterNodeResponses;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.Base64InputStream;
import com.liferay.portal.kernel.io.Base64OutputStream;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.license.tools.deploy.LicenseDeployer;
import com.liferay.portal.license.validator.ClusterValidator;
import com.liferay.portal.license.validator.DeveloperValidator;
import com.liferay.portal.license.validator.KeyValidator;
import com.liferay.portal.license.validator.LicenseTypeValidator;
import com.liferay.portal.license.validator.LicenseValidator;
import com.liferay.portal.license.validator.LimitedValidator;
import com.liferay.portal.license.validator.PerUserValidator;
import com.liferay.portal.license.validator.ProductionValidator;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.SubscriptionSender;
import com.liferay.util.Encryptor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

/**
 * @author Shuyang Zhou
 * @author Amos Fong
 */
public class LicenseManager {

	public static final String PRODUCT_ID_PORTAL = "Portal";

	public static final int STATE_ABSENT = 1;

	public static final int STATE_EXPIRED = 2;

	public static final int STATE_GOOD = 3;

	public static final int STATE_INACTIVE = 4;

	public static final int STATE_INVALID = 5;

	public static final int STATE_OVERLOAD = 6;

	public static void checkClusterLicense() {
		License license = _getLicense(PRODUCT_ID_PORTAL);

		if (license == null) {
			checkBinaryLicense(PRODUCT_ID_PORTAL);

			license = _getLicense(PRODUCT_ID_PORTAL);
		}

		if (license == null) {
			return;
		}

		String licenseEntryType = license.getLicenseEntryType();

		if ((licenseEntryType != null) && licenseEntryType.equals(License.TYPE_CLUSTER)) {
			int maxServers = license.getMaxServers();

			if (maxServers > 0) {
				List<ClusterNode> clusterNodes = ClusterExecutorUtil.getClusterNodes();

				if (clusterNodes.size() > maxServers) {
					Collections.sort(clusterNodes);

					ClusterNode lastClusterNode = clusterNodes.get(clusterNodes.size() - 1);

					try {
						if (lastClusterNode.equals(ClusterExecutorUtil.getLocalClusterNode())) {
							_log.error("Detected " + clusterNodes.size() + " nodes in the cluster. Your license allows a maximum of " + maxServers + " cluster nodes. Local server is shutting down.");

							System.exit(-20);
						}
					}
					catch (Exception e) {
					}
				}
			}
		}
	}

	public static void checkUserLicense() throws com.liferay.portal.kernel.exception.PortalException {
		try {
			License license = _getLicense(PRODUCT_ID_PORTAL);

			if (license == null) {
				return;
			}

			long maxUsersCount = license.getMaxUsers();

			if (maxUsersCount > 0) {
				java.sql.Connection con = null;
				java.sql.PreparedStatement ps = null;
				java.sql.ResultSet rs = null;

				try {
					con = com.liferay.portal.kernel.dao.jdbc.DataAccess.getConnection();

					ps = con.prepareStatement("select count(*) from User_ where (defaultUser = ?) and (status = ?)");

					ps.setBoolean(1, false);
					ps.setLong(2, com.liferay.portal.kernel.workflow.WorkflowConstants.STATUS_APPROVED);

					rs = ps.executeQuery();

					while (rs.next()) {
						long count = rs.getLong(1);

						if (count >= maxUsersCount) {
							throw new CompanyMaxUsersException();
						}
					}
				}
				finally {
					com.liferay.portal.kernel.dao.jdbc.DataAccess.cleanUp(con, ps, rs);
				}
			}
		}
		catch (java.sql.SQLException se) {
			throw new com.liferay.portal.kernel.exception.PortalException(se);
		}
	}

	public static void checkUserLicense(int authResult) throws com.liferay.portal.kernel.exception.PortalException {
		if (authResult != com.liferay.portal.security.auth.Authenticator.SUCCESS) {
			return;
		}

		License license = _getLicense(PRODUCT_ID_PORTAL);

		if (license == null) {
			return;
		}

		long maxConcurrentUsersCount = license.getMaxConcurrentUsers();

		if (maxConcurrentUsersCount > 0) {
			if (!PropsValues.LIVE_USERS_ENABLED) {
				_log.error("The property live.users.enabled must be true to use this license");

				throw new CompanyMaxUsersException();
			}

			if (com.liferay.portal.liveusers.LiveUsers.getUserIdsCount() >= maxConcurrentUsersCount) {
				throw new CompanyMaxUsersException();
			}
		}
	}

	public static com.liferay.portal.events.EventsProcessor getEventsProcessor() {
		ClassLoader classLoader = new com.liferay.portal.ee.license.classloader.DecryptorClassLoader();

		try {
			Class<?> eventProcessorClass = classLoader.loadClass(
				"com.liferay.portal.ee.license.EventsProcessorImpl");

			return (com.liferay.portal.events.EventsProcessor)eventProcessorClass.newInstance();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RuntimeException(e);
		}
	}

	public static com.liferay.portal.events.StartupAction getStartupAction()
		throws Exception {

		ClassLoader classLoader = new com.liferay.portal.ee.license.classloader.DecryptorClassLoader();

		Class<?> startupActionClass = classLoader.loadClass(
			"com.liferay.portal.ee.license.StartupAction");

		com.liferay.portal.events.StartupAction startupAction =
			(com.liferay.portal.events.StartupAction)startupActionClass.newInstance();

		return startupAction;
	}

	public static void writeKey(
		HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {

		try {
			long userId = com.liferay.portal.util.PortalUtil.getBasicAuthUserId(request);

			if (userId <= 0) {
				return;
			}
		}
		catch (Exception e) {
			return;
		}

		String productId = ParamUtil.getString(request, "productId");
		String uuid = ParamUtil.getString(request, "uuid");

		if (Validator.isNull(productId) || Validator.isNull(uuid)) {
			return;
		}

		int licenseState = getLicenseState(productId);

		try {
			String digest = _digest(productId, uuid, licenseState);

			response.setContentType(com.liferay.portal.kernel.util.ContentTypes.TEXT);

			com.liferay.portal.kernel.servlet.ServletResponseUtil.write(response, digest);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	private static String _digest(
			String productId, String uuid, int licenseState)
		throws Exception {

		java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance("MD5");

		String digest = _digest(messageDigest, uuid + productId);

		int length = digest.length();

		StringBuilder sb = new StringBuilder(length + (length / 4));

		for (int i = 0; i < (length / 2); i++) {
			if ((i % 2) == 0) {
				sb.append(licenseState);
			}

			sb.append(digest.charAt(i));
			sb.append(digest.charAt(length - i - 1));
		}

		return _digest(messageDigest, sb.toString());
	}

	private static String _digest(java.security.MessageDigest messageDigest, String text) {
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

	private static final char[] _HEX_CHARACTERS = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
		'e', 'f'
	};

	public static void checkBinaryLicenses () {
		Set<License> licenses = _getBinaryLicenses();

		if (licenses.isEmpty()) {
			_log.error("No binary licenses found");

			return;
		}

		for (License license : licenses) {
			_checkBinaryLicense(license);
		}
	}

	public static void checkBinaryLicense(String productId) {
		List<License> licenses = _getBinaryLicenses(productId);

		for (License license : licenses) {
			_checkBinaryLicense(license);
		}
	}

	public static void checkLicense(ServletContext servletContext)
		throws Exception {

		InputStream is = servletContext.getResourceAsStream(
			"/WEB-INF/liferay-plugin-package.properties");

		if (is == null) {
			return;
		}

		String propertiesString = StringUtil.read(is);

		is.close();

		Properties properties = PropertiesUtil.load(propertiesString);

		String productId = properties.getProperty("product-id");

		if (Validator.isNull(productId)) {
			return;
		}

		int productVersion = GetterUtil.getInteger(
			properties.getProperty("product-version-id"));

		License license = _getLicense(productId);

		int licenseState = getLicenseState(productId);

		if ((license == null) || (licenseState != STATE_GOOD)) {
			checkBinaryLicense(productId);

			license = _getLicense(productId);

			licenseState = getLicenseState(productId);

			if ((license == null) || (licenseState != STATE_GOOD)) {
				throw new Exception(
					"This application does not have a valid license");
			}
		}

		int requiredProductVersion = GetterUtil.getInteger(
			license.getProductVersion());

		if (requiredProductVersion > productVersion) {
			throw new Exception(
				"The version of your application is not compatible with the " +
					"registered license");
		}

		List<ClusterNode> clusterNodes = ClusterExecutorUtil.getClusterNodes();

		if (clusterNodes.size() <= 1) {
			return;
		}

		clusterNodes.remove(ClusterExecutorUtil.getLocalClusterNode());

		for (ClusterNode clusterNode : clusterNodes) {
			MethodHandler methodHandler = new MethodHandler(
				_getLicenseStateMethodKey, productId);

			ClusterRequest clusterRequest = ClusterRequest.createUnicastRequest(
				methodHandler, clusterNode.getClusterNodeId());

			FutureClusterResponses futureClusterResponses =
				ClusterExecutorUtil.execute(clusterRequest);

			ClusterNodeResponses clusterNodeResponses =
				futureClusterResponses.get(10000, TimeUnit.MILLISECONDS);

			ClusterNodeResponse clusterNodeResponse =
				clusterNodeResponses.getClusterResponse(clusterNode);

			Object result = clusterNodeResponse.getResult();

			if (result == null) {
				return;
			}

			Integer remoteLicenseState = (Integer)result;

			if (remoteLicenseState != STATE_GOOD) {
				throw new Exception(
					"A clustered server has an invalid license.");
			}
		}
	}

	public static List<Map<String, String>> getClusterLicenseProperties(
			String clusterNodeId)
		throws Exception {

		List<ClusterNode> clusterNodes = ClusterExecutorUtil.getClusterNodes();

		ClusterNode clusterNode = null;

		for (ClusterNode curClusterNode : clusterNodes) {
			String curClusterNodeId = curClusterNode.getClusterNodeId();

			if (curClusterNodeId.equals(clusterNodeId)) {
				clusterNode = curClusterNode;

				break;
			}
		}

		if (clusterNode == null) {
			return null;
		}

		try {
			if (clusterNode.equals(ClusterExecutorUtil.getLocalClusterNode())) {
				return getLicenseProperties();
			}

			ClusterRequest clusterRequest = ClusterRequest.createUnicastRequest(
				_getLicensePropertiesMethodHandler, clusterNodeId);

			FutureClusterResponses futureClusterResponses =
				ClusterExecutorUtil.execute(clusterRequest);

			ClusterNodeResponses clusterNodeResponses =
				futureClusterResponses.get(20000, TimeUnit.MILLISECONDS);

			ClusterNodeResponse clusterNodeResponse =
				clusterNodeResponses.getClusterResponse(clusterNode);

			return (List<Map<String, String>>)clusterNodeResponse.getResult();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw e;
		}
	}

	public static Map<String, String> getClusterServerInfo(String clusterNodeId)
		throws Exception {

		List<ClusterNode> clusterNodes = ClusterExecutorUtil.getClusterNodes();

		ClusterNode clusterNode = null;

		for (ClusterNode curClusterNode : clusterNodes) {
			String curClusterNodeId = curClusterNode.getClusterNodeId();

			if (curClusterNodeId.equals(clusterNodeId)) {
				clusterNode = curClusterNode;

				break;
			}
		}

		if (clusterNode == null) {
			return null;
		}

		try {
			if (clusterNode.equals(ClusterExecutorUtil.getLocalClusterNode())) {
				return getServerInfo();
			}

			ClusterRequest clusterRequest = ClusterRequest.createUnicastRequest(
				_getServerInfoMethodHandler, clusterNodeId);

			FutureClusterResponses futureClusterResponses =
				ClusterExecutorUtil.execute(clusterRequest);

			ClusterNodeResponses clusterNodeResponses =
				futureClusterResponses.get(20000, TimeUnit.MILLISECONDS);

			ClusterNodeResponse clusterNodeResponse =
				clusterNodeResponses.getClusterResponse(clusterNode);

			return (Map<String, String>)clusterNodeResponse.getResult();
		}
		catch (Exception e) {
			_log.error(e, e);

			throw e;
		}
	}

	public static String getHostName() {
		if (_hostName == null) {
			_hostName = StringPool.BLANK;

			try {
				Runtime runtime = Runtime.getRuntime();

				Process process = runtime.exec("hostname");

				BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()), 128);

				_hostName = bufferedReader.readLine();

				bufferedReader.close();
			}
			catch (Exception e) {
				_log.error("Unable to read local server's host name");

				_log.error(e, e);
			}
		}

		return _hostName;
	}

	public static Set<String> getIpAddresses() {
		if (_ipAddresses == null) {
			_ipAddresses = new HashSet<String>();

			try {
				List<NetworkInterface> networkInterfaces = Collections.list(
					NetworkInterface.getNetworkInterfaces());

				for (NetworkInterface networkInterface : networkInterfaces) {
					List<InetAddress> inetAddresses = Collections.list(
						networkInterface.getInetAddresses());

					for (InetAddress inetAddress : inetAddresses) {
						if (inetAddress.isLinkLocalAddress() ||
							inetAddress.isLoopbackAddress() ||
							!(inetAddress instanceof Inet4Address)) {

							continue;
						}

						_ipAddresses.add(inetAddress.getHostAddress());
					}
				}
			}
			catch (Exception e) {
				_log.error("Unable to read local server's IP addresses");

				_log.error(e, e);
			}
		}

		return new HashSet<String>(_ipAddresses);
	}

	public static LicenseInfo getLicenseInfo(String productId) {
		License license = _getLicense(productId);

		if (license == null) {
			return null;
		}

		return new LicenseInfo(
			license.getOwner(), license.getDescription(),
			license.getProductEntryName(), license.getProductId(),
			license.getProductVersion(), license.getLicenseEntryType(),
			license.getLicenseVersion(), license.getStartDate(),
			license.getExpirationDate(), license.getMaxUsers(),
			license.getHostNames(), license.getIpAddresses(),
			license.getMacAddresses());
	}

	public static List<Map<String, String>> getLicenseProperties() {
		List<Map<String, String>> licenseProperties =
			new ArrayList<Map<String, String>>();

		for (Map.Entry<String, AtomicStampedReference<License>> entry :
				_licenseStampedReferences.entrySet()) {

			String productId = entry.getKey();

			AtomicStampedReference<License> licenseStampedReference =
				entry.getValue();

			int[] stampHolder = new int[1];

			License license = licenseStampedReference.get(stampHolder);

			int licenseState = stampHolder[0];

			if ((license == null) || (licenseState == STATE_ABSENT)) {
				continue;
			}

			Map<String, String> curLicenseProperties =
				KeyValidator.getProperties(license);

			curLicenseProperties.put(
				"licenseState", String.valueOf(licenseState));
			curLicenseProperties.put("productId", productId);

			if (productId.equals(PRODUCT_ID_PORTAL)) {
				licenseProperties.add(0, curLicenseProperties);
			}
			else {
				licenseProperties.add(curLicenseProperties);
			}
		}

		return licenseProperties;
	}

	public static Map<String, String> getLicenseProperties(String productId) {
		Map<String, String> properties = new HashMap<String, String>();

		AtomicStampedReference<License> licenseStampedReference =
			_licenseStampedReferences.get(productId);

		if (licenseStampedReference == null) {
			return properties;
		}

		int[] stampHolder = new int[1];

		License license = licenseStampedReference.get(stampHolder);

		int licenseState = stampHolder[0];

		if ((license == null) || (licenseState == STATE_ABSENT)) {
			return properties;
		}
		else {
			return KeyValidator.getProperties(license);
		}
	}

	public static int getLicenseState() {
		return getLicenseState(null, PRODUCT_ID_PORTAL);
	}

	public static int getLicenseState(HttpServletRequest request) {
		return getLicenseState(request, PRODUCT_ID_PORTAL);
	}

	public static int getLicenseState(String productId) {
		return getLicenseState(null, productId);
	}

	public static int getLicenseState(
		HttpServletRequest request, String productId) {

		AtomicStampedReference<License> licenseStampedReference =
			_licenseStampedReferences.get(productId);

		int[] stampHolder = new int[1];

		License license = null;

		if (licenseStampedReference != null) {
			license = licenseStampedReference.get(stampHolder);
		}

		int licenseState = stampHolder[0];

		if (license == null) {
			return STATE_ABSENT;
		}

		if (licenseState != STATE_GOOD) {
			return licenseState;
		}

		if (license.isExpired()) {
			_setLicense(productId, license, STATE_EXPIRED);

			return STATE_EXPIRED;
		}

		String licenseEntryType = license.getLicenseEntryType();

		if (licenseEntryType.equals(License.TYPE_DEVELOPER) ||
			licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER)) {

			if (request != null) {
				String remoteAddr = request.getRemoteAddr();

				_clientIPAddresses.add(remoteAddr);
			}

			if (_clientIPAddresses.size() > license.getMaxHttpSessions()) {
				return STATE_OVERLOAD;
			}
		}

		//  Good license state

		return licenseState;
	}

	public static Set<String> getMacAddresses() {
		if (_macAddresses == null) {
			_macAddresses = new HashSet<String>();

			try {
				Process process = null;

				Runtime runtime = Runtime.getRuntime();

				File ifconfigFile = new File("/sbin/ifconfig");

				if (ifconfigFile.exists()) {
					process = runtime.exec(
						new String[] {"/sbin/ifconfig", "-a"}, null);
				}
				else {
					process = runtime.exec(
						new String[] {"ipconfig", "/all"}, null);
				}

				BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()), 128);

				String line = null;

				while ((line = bufferedReader.readLine()) != null) {
					Matcher matcher = _macAddressPattern.matcher(line);

					if (matcher.find()) {
						String macAddress = matcher.group(1);

						if (Validator.isNotNull(macAddress)) {
							macAddress = macAddress.toLowerCase();
							macAddress = StringUtil.replace(
								macAddress, "-", ":");

							_macAddresses.add(macAddress);
						}
					}
				}

				bufferedReader.close();
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}

		return new HashSet<String>(_macAddresses);
	}

	public static Map<String, String> getServerInfo() {
		Map<String, String> serverInfo = new HashMap<String, String>();

		serverInfo.put("hostName", getHostName());
		serverInfo.put("ipAddresses", StringUtil.merge(getIpAddresses()));
		serverInfo.put("macAddresses", StringUtil.merge(getMacAddresses()));

		return serverInfo;
	}

	public static void registerOrder(HttpServletRequest request) {
		String orderUuid = ParamUtil.getString(request, "orderUuid");
		String productEntryName = ParamUtil.getString(
			request, "productEntryName");
		int maxServers = ParamUtil.getInteger(request, "maxServers");

		List<ClusterNode> clusterNodes = ClusterExecutorUtil.getClusterNodes();

		if ((clusterNodes.size() <= 1) || Validator.isNull(productEntryName) ||
			Validator.isNull(orderUuid)) {

			Map<String, Object> attributes = registerOrder(
				orderUuid, productEntryName, maxServers);

			for (Map.Entry<String, Object> entry : attributes.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}
		}
		else {
			for (ClusterNode clusterNode : clusterNodes) {
				boolean register = ParamUtil.getBoolean(
					request, clusterNode.getClusterNodeId() + "_register");

				if (!register) {
					continue;
				}

				try {
					_registerClusterOrder(
						request, clusterNode, orderUuid, productEntryName,
						maxServers);
				}
				catch (Exception e) {
					_log.error(e, e);

					String message =
						"Error contacting " + clusterNode.getHostName();

					if (clusterNode.getPort() != -1) {
						message += StringPool.COLON + clusterNode.getPort();
					}

					request.setAttribute(
						clusterNode.getClusterNodeId() + "_ERROR_MESSAGE",
						message);
				}
			}
		}
	}

	public static Map<String, Object> registerOrder(
		String orderUuid, String productEntryName, int maxServers) {

		Map<String, Object> attributes = new HashMap<String, Object>();

		if (Validator.isNull(orderUuid)) {
			return attributes;
		}

		try {
			JSONObject jsonObject = _createRequest(
				orderUuid, productEntryName, maxServers);

			String response = _sendRequest(jsonObject.toString());

			JSONObject responseJSONObject = JSONFactoryUtil.createJSONObject(
				response);

			attributes.put(
				"ORDER_PRODUCT_ID", responseJSONObject.getString("productId"));
			attributes.put(
				"ORDER_PRODUCTS", _getOrderProducts(responseJSONObject));

			String errorMessage = responseJSONObject.getString("errorMessage");

			if (Validator.isNotNull(errorMessage)) {
				attributes.put("ERROR_MESSAGE", errorMessage);

				return attributes;
			}

			String licenseXML = responseJSONObject.getString("licenseXML");

			if (Validator.isNotNull(licenseXML)) {
				LicenseDeployer licenseDeployer = new LicenseDeployer(
					licenseXML);

				licenseDeployer.deploy();

				attributes.clear();
				attributes.put(
					"SUCCESS_MESSAGE",
					"Your license has been successfully registered.");
			}
		}
		catch (Exception e) {
			_log.error(e, e);

			attributes.put(
				"ERROR_MESSAGE",
				"There was an error contacting " + _LICENSE_SERVER_URL);
		}

		return attributes;
	}

	public static void writeBinaryLicense(License license) throws Exception {
		File licenseRepositoryDir = new File(_LICENSE_REPOSITORY_DIR);

		if (!licenseRepositoryDir.exists()) {
			licenseRepositoryDir.mkdirs();
		}

		File binaryLicenseFile = _buildBinaryFile(license);

		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream(binaryLicenseFile);

			objectOutputStream = new ObjectOutputStream(
				new Base64OutputStream(fileOutputStream));

			license.setLastAccessedTime(System.currentTimeMillis());

			objectOutputStream.writeInt(3);
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getAccountEntryName()));
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getDescription()));
			objectOutputStream.writeObject(license.getExpirationDate());
			objectOutputStream.writeObject(license.getHostNames());
			objectOutputStream.writeObject(license.getIpAddresses());
			objectOutputStream.writeUTF(GetterUtil.getString(license.getKey()));
			objectOutputStream.writeLong(license.getLastAccessedTime());
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getLicenseEntryName()));
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getLicenseEntryType()));
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getLicenseVersion()));
			objectOutputStream.writeObject(license.getMacAddresses());
			objectOutputStream.writeInt(license.getMaxHttpSessions());
			objectOutputStream.writeInt(license.getMaxServers());
			objectOutputStream.writeLong(license.getMaxConcurrentUsers());
			objectOutputStream.writeLong(license.getMaxUsers());
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getOwner()));
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getProductEntryName()));
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getProductId()));
			objectOutputStream.writeUTF(
				GetterUtil.getString(license.getProductVersion()));
			objectOutputStream.writeObject(license.getServerIds());
			objectOutputStream.writeObject(license.getStartDate());
		}
		finally {
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}

			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}
	}

	private static File _buildBinaryFile(License license) {
		StringBundler sb = new StringBundler(6);

		String productId = license.getProductId();

		if (productId.equals(PRODUCT_ID_PORTAL)) {
			sb.append(StringUtil.extractChars(license.getAccountEntryName()));
			sb.append("_");
		}

		sb.append(StringUtil.extractChars(license.getProductEntryName()));
		sb.append("_");
		sb.append(StringUtil.extractChars(license.getLicenseEntryType()));
		sb.append(".li");

		return new File(_LICENSE_REPOSITORY_DIR, sb.toString());
	}

	private static void _checkBinaryLicense(License license) {
		try {
			String serverId = _getServerId();

			if (license == null) {
				return;
			}

			String[] serverIds = license.getServerIds();

			if ((serverIds != null) && (serverIds.length > 0)) {
				if (!ArrayUtil.contains(serverIds, serverId)) {
					throw new Exception(
						"Server id matching failed. Allowed server ids: " +
							StringUtil.merge(serverIds));
				}

				if (!_isActiveLicense(license, false)) {
					_setLicense(
						license.getProductId(), license, STATE_INACTIVE);

					_log.error(
						license.getProductEntryName() + " license is inactive");

					return;
				}
			}

			if (license.isExpired()) {
				_setLicense(license.getProductId(), license, STATE_EXPIRED);

				_log.error(
					license.getProductEntryName() + " license is expired");

				return;
			}

			_validatorChain.validate(license);

			_setLicense(license.getProductId(), license, STATE_GOOD);

			if (_log.isInfoEnabled()) {
				_log.info(
					license.getProductEntryName() +
						" license validation passed");
			}
		}
		catch (CompanyMaxUsersException cmue) {
			_setLicense(license.getProductId(), license, STATE_OVERLOAD);

			_log.error(
				license.getProductEntryName() + " license validation failed",
				cmue);
		}
		catch (Exception e) {
			_setLicense(license.getProductId(), license, STATE_INVALID);

			_log.error(
				license.getProductEntryName() + " license validation failed",
				e);
		}
	}

	private static JSONObject _createRequest(
			String orderUuid, String productEntryName, int maxServers)
		throws Exception {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("version", 1);
		jsonObject.put("orderUuid", orderUuid);
		jsonObject.put("liferayVersion", ReleaseInfo.getBuildNumber());

		if (Validator.isNull(productEntryName)) {
			jsonObject.put("cmd", "QUERY");
		}
		else {
			jsonObject.put("cmd", "REGISTER");

			if (productEntryName.startsWith("basic")) {
				jsonObject.put("productEntryName", "basic");

				if (productEntryName.equals("basic-cluster")) {
					jsonObject.put("cluster", true);
					jsonObject.put("maxServers", maxServers);
				}
				else if (productEntryName.startsWith("basic-")) {
					String[] productNameArray = StringUtil.split(
						productEntryName, StringPool.DASH);

					if (productNameArray.length >= 3) {
						jsonObject.put("offeringEntryId", productNameArray[1]);
						jsonObject.put("clusterId", productNameArray[2]);
					}
				}
			}
			else {
				jsonObject.put("productEntryName", productEntryName);
			}

			jsonObject.put("hostName", getHostName());
			jsonObject.put("ipAddresses", StringUtil.merge(getIpAddresses()));
			jsonObject.put("macAddresses", StringUtil.merge(getMacAddresses()));
			jsonObject.put("serverId", _getServerId());
		}

		return jsonObject;
	}

	private static String _decryptResponse(
			String serverURL, InputStream inputStream)
		throws Exception {

		if (serverURL.startsWith(Http.HTTPS)) {
			return StringUtil.read(inputStream);
		}
		else {
			byte[] bytes = IOUtils.toByteArray(inputStream);

			if ((bytes == null) || (bytes.length <= 0)) {
				return null;
			}

			bytes = Encryptor.decryptUnencodedAsBytes(_symmetricKey, bytes);

			return new String(bytes, StringPool.UTF8);
		}
	}

	private static byte[] _encryptRequest(String serverURL, String request)
		throws Exception {

		byte[] bytes = request.getBytes(StringPool.UTF8);

		if (serverURL.startsWith(Http.HTTPS)) {
			return bytes;
		}
		else {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			bytes = Encryptor.encryptUnencoded(_symmetricKey, bytes);

			jsonObject.put("content", Base64.objectToString(bytes));
			jsonObject.put("key", _encryptedSymmetricKey);

			return jsonObject.toString().getBytes(StringPool.UTF8);
		}
	}

	private static String _generateServerId() throws Exception {
		String hostName = getHostName();
		Set<String> ipAddresses = getIpAddresses();
		Set<String> macAddresses = getMacAddresses();

		Properties serverIdProperties = new Properties();

		serverIdProperties.put("hostName", hostName);
		serverIdProperties.put("ipAddresses", StringUtil.merge(ipAddresses));
		serverIdProperties.put("macAddresses", StringUtil.merge(macAddresses));
		serverIdProperties.put("salt", UUID.randomUUID().toString());

		String propertiesString = PropertiesUtil.toString(serverIdProperties);

		byte[] bytes = propertiesString.getBytes(StringPool.UTF8);

		for (int i = _keys.length - 1; i >= 0; i--) {
			bytes = Encryptor.encryptUnencoded(_keys[i], bytes);
		}

		return Base64.objectToString(bytes);
	}

	private static TreeSet<License> _getBinaryLicenses() {
		TreeSet<License> licenses = new TreeSet<License>();

		File licenseRepositoryDir = new File(_LICENSE_REPOSITORY_DIR);

		if (!licenseRepositoryDir.exists() ||
			!licenseRepositoryDir.isDirectory()) {

			_log.info("Failed to find directory " + licenseRepositoryDir);

			return licenses;
		}

		File[] binaryLicenseFiles = licenseRepositoryDir.listFiles();

		if ((binaryLicenseFiles == null) || (binaryLicenseFiles.length == 0)) {
			_log.info(
				"Failed to find license files in directory " +
					licenseRepositoryDir);

			return licenses;
		}

		for (File binaryLicenseFile : binaryLicenseFiles) {
			if (binaryLicenseFile.isDirectory()) {
				continue;
			}

			FileInputStream fileInputStream = null;
			ObjectInputStream objectInputStream = null;

			try {
				fileInputStream = new FileInputStream(binaryLicenseFile);

				objectInputStream = new ObjectInputStream(
					new Base64InputStream(fileInputStream));

				int licenseVersion = objectInputStream.readInt();

				if (licenseVersion == 3) {
					licenses.add(_getBinaryLicenseVersion3(objectInputStream));
				}
			}
			catch (Exception e) {
				_log.error(
					"Failed to read license file " + binaryLicenseFile, e);
			}
			finally {
				if (objectInputStream != null) {
					try {
						objectInputStream.close();
					}
					catch (IOException ioe) {
					}
				}

				if (fileInputStream != null) {
					try {
						fileInputStream.close();
					}
					catch (IOException ioe) {
					}
				}
			}
		}

		long currentTime = System.currentTimeMillis();

		Iterator<License> licenseIterator = licenses.iterator();

		while (licenseIterator.hasNext()) {
			License license = licenseIterator.next();

			// Remove corrupted licenses

			if (!KeyValidator.validate(license)) {
				licenseIterator.remove();

				File file = _buildBinaryFile(license);

				file.delete();

				_log.error(
					"Corrupt license file. Removing license file " + license);

				continue;
			}

			// Skip licenses with future last accessed times

			long lastAccessedTime = license.getLastAccessedTime();

			if (currentTime < lastAccessedTime) {
				licenseIterator.remove();

				_log.error(
					"A license modified in the future was detected. Skipping " +
						"license file " + license);

				continue;
			}

			// Touch license file

			try {
				writeBinaryLicense(license);
			}
			catch (Exception e) {
			}

			// Skip licenses that have not started yet

			Date startDate = license.getStartDate();

			if ((currentTime + (Time.DAY * 2)) < startDate.getTime()) {
				licenseIterator.remove();

				_log.error(
					"License has not reached start date yet. Skipping " +
						"license file " + license);

				continue;
			}
		}

		return licenses;
	}

	private static List<License> _getBinaryLicenses(String productId) {
		List<License> licenses = new ArrayList<License>();

		TreeSet<License> binaryLicenses = _getBinaryLicenses();

		if (binaryLicenses.isEmpty()) {
			if (productId.equals(PRODUCT_ID_PORTAL)) {
				licenses.add(_getBinaryLicenseVersion1());
			}
			else {
				return licenses;
			}
		}

		for (License binaryLicense : binaryLicenses) {
			String curProductId = binaryLicense.getProductId();

			if (curProductId.equals(productId)) {
				licenses.add(binaryLicense);
			}
		}

		return licenses;
	}

	private static License _getBinaryLicenseVersion1() {
		File licenseFile = new File(PropsValues.LIFERAY_HOME + "/ee/license");

		if (!licenseFile.exists()) {
			return null;
		}

		try {
			String licenseFileEncoded = FileUtil.read(licenseFile);
			String licenseFileDecoded = (String)Base64.stringToObject(
				licenseFileEncoded);

			Properties licenseFileProperties = PropertiesUtil.load(
				licenseFileDecoded);

			String licenseKey = licenseFileProperties.getProperty("licenseKey");

			Properties licenseKeyProperties = new Properties();

			if (Validator.isNull(licenseKey)) {
				return null;
			}

			licenseKey = licenseKey.replaceAll("\\s", StringPool.BLANK);

			byte[] bytes = (byte[])Base64.stringToObject(licenseKey);

			for (Key key : _keys) {
				bytes = Encryptor.decryptUnencodedAsBytes(key, bytes);
			}

			PropertiesUtil.load(licenseKeyProperties, new String(bytes));

			int version = GetterUtil.getInteger(
				licenseKeyProperties.getProperty("version"));

			if (version < 1) {
				licenseKeyProperties.setProperty("version", "1");
				licenseKeyProperties.setProperty("type", "production");
			}

			String owner = GetterUtil.getString(
				licenseKeyProperties.getProperty("owner"));
			String description = GetterUtil.getString(
				licenseKeyProperties.getProperty("description"));
			String productVersion = GetterUtil.getString(
				licenseKeyProperties.getProperty("productVersion"));
			String type = GetterUtil.getString(
				licenseKeyProperties.getProperty("type"), "production");
			String licenseVersion = GetterUtil.getString(
				licenseKeyProperties.getProperty("version"));
			long startDate = GetterUtil.getLong(
				licenseKeyProperties.getProperty("startDate"));
			long lifetime = GetterUtil.getLong(
				licenseKeyProperties.getProperty("lifetime"));

			String[] serverIds = new String[0];

			if (type.equals("cluster")) {
				for (int i = 0;; i++) {
					String macAddress = licenseKeyProperties.getProperty(
						"macAddress." + i);

					if (Validator.isNull(macAddress)) {
						break;
					}

					serverIds = ArrayUtil.append(serverIds, macAddress);
				}
			}
			else if (type.equals("production")) {
				String licenseKeyServerId = GetterUtil.getString(
					licenseKeyProperties.getProperty("serverId"));

				serverIds = ArrayUtil.append(serverIds, licenseKeyServerId);
			}

			return new License(
				owner, owner, description, "", PRODUCT_ID_PORTAL,
				productVersion, "", type, licenseVersion, new Date(startDate),
				new Date(startDate + lifetime), 0, 5, 0, 0, null, null, null,
				serverIds, licenseKey);
		}
		catch (Exception e) {
			return null;
		}
	}

	private static License _getBinaryLicenseVersion3(
			ObjectInputStream objectInputStream)
		throws ClassNotFoundException, IOException {

		String accountEntryName = objectInputStream.readUTF();
		String description = objectInputStream.readUTF();
		Date expirationDate = (Date)objectInputStream.readObject();
		String[] hostNames = (String[])objectInputStream.readObject();
		String[] ipAddresses = (String[])objectInputStream.readObject();
		String key = objectInputStream.readUTF();
		long lastAccessedTime = objectInputStream.readLong();
		String licenseEntryName = objectInputStream.readUTF();
		String licenseEntryType = objectInputStream.readUTF();
		String licenseVersion = objectInputStream.readUTF();
		String[] macAddresses = (String[])objectInputStream.readObject();
		int maxHttpSessions = objectInputStream.readInt();
		int maxServers = objectInputStream.readInt();
		long maxConcurrentUsers = objectInputStream.readLong();
		long maxUsers = objectInputStream.readLong();
		String owner = objectInputStream.readUTF();
		String productEntryName = objectInputStream.readUTF();
		String productId = objectInputStream.readUTF();
		String productVersion = objectInputStream.readUTF();
		String[] serverIds = (String[])objectInputStream.readObject();
		Date startDate = (Date)objectInputStream.readObject();

		License license = new License(
			accountEntryName, owner, description, productEntryName, productId,
			productVersion, licenseEntryName, licenseEntryType, licenseVersion,
			startDate, expirationDate, maxServers, maxHttpSessions,
			maxConcurrentUsers, maxUsers, hostNames, ipAddresses, macAddresses,
			serverIds, key);

		license.setLastAccessedTime(lastAccessedTime);

		return license;
	}

	private static License _getLicense(String productId) {
		AtomicStampedReference<License> licenseStampedReference =
			_licenseStampedReferences.get(productId);

		if (licenseStampedReference == null) {
			return null;
		}

		int[] stampHolder = new int[1];

		return licenseStampedReference.get(stampHolder);
	}

	private static Map<String, String> _getOrderProducts(
		JSONObject jsonObject) {

		JSONObject productsJSONObject = jsonObject.getJSONObject(
			"productsJSONObject");

		if (productsJSONObject == null) {
			return null;
		}

		Map<String, String> sortedMap = new TreeMap<String, String>(
			String.CASE_INSENSITIVE_ORDER);

		Iterator<String> itr = productsJSONObject.keys();

		while (itr.hasNext()) {
			String key = itr.next();

			sortedMap.put(key, productsJSONObject.getString(key));
		}

		return sortedMap;
	}

	private static String _getServerId() throws Exception {
		if (Validator.isNotNull(_serverId)) {
			return _serverId;
		}

		Properties serverProperties = _getServerProperties();

		_serverId = serverProperties.getProperty("serverId");

		if (Validator.isNull(_serverId)) {
			_serverId = _generateServerId();

			serverProperties.put("serverId", _serverId);

			_writeServerProperties(serverProperties);
		}
		else {
			byte[] serverIdBytes = (byte[])Base64.stringToObject(_serverId);

			for (Key key : _keys) {
				serverIdBytes = Encryptor.decryptUnencodedAsBytes(
					key, serverIdBytes);
			}

			Properties serverIdProperties = new Properties();

			PropertiesUtil.load(serverIdProperties, new String(serverIdBytes));

			String serverIdHostName = GetterUtil.getString(
				serverIdProperties.getProperty("hostName"));

			if (serverIdHostName.equalsIgnoreCase(getHostName())) {
				return _serverId;
			}

			List<String> serverIdIpAddresses = ListUtil.fromArray(
				StringUtil.split(
					serverIdProperties.getProperty("ipAddresses")));

			serverIdIpAddresses.retainAll(getIpAddresses());

			if (!serverIdIpAddresses.isEmpty()) {
				return _serverId;
			}

			List<String> serverIdMacAddresses = ListUtil.fromArray(
				StringUtil.split(
					serverIdProperties.getProperty("macAddresses")));

			serverIdMacAddresses.retainAll(getMacAddresses());

			if (!serverIdMacAddresses.isEmpty()) {
				return _serverId;
			}

			_serverId = _generateServerId();

			serverProperties.put("serverId", _serverId);

			_writeServerProperties(serverProperties);
		}

		return _serverId;
	}

	private static Properties _getServerProperties() throws Exception {
		Properties serverProperties = new Properties();

		File serverIdFile = new File(
			_LICENSE_REPOSITORY_DIR + "/server/serverId");

		if (!serverIdFile.exists()) {
			return serverProperties;
		}

		byte[] bytes = FileUtil.getBytes(serverIdFile);

		for (Key key : _keys) {
			bytes = Encryptor.decryptUnencodedAsBytes(key, bytes);
		}

		PropertiesUtil.load(serverProperties, new String(bytes));

		return serverProperties;
	}

	private static void _initKeys() {
		ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();

		if (_keys == null) {
			_keys = new Key[3];

			String content = null;

			try {
				content = StringUtil.read(
					classLoader,
					"com/liferay/portal/license/classloader/keys.txt");
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			String contentDigest = DigesterUtil.digestBase64(content);

			String[] keys = StringUtil.split(content, StringPool.NEW_LINE);

			int count = 0;
			int marker = 3;
			int pos = 0;

			char[] charArray = contentDigest.toCharArray();

			for (char c : charArray) {
				int x = c;

				count++;

				if ((count % marker) == 0) {
					_keys[((marker / 3) - 1)] = (Key)Base64.stringToObject(
						keys[pos]);

					count = 0;
					marker = marker + 3;
					pos = 0;
				}
				else {
					pos += x;
				}
			}
		}

		if (_symmetricKey == null) {
			try {
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

				keyGenerator.init(128, new SecureRandom());

				_symmetricKey = keyGenerator.generateKey();

				URL url = classLoader.getResource(
					"com/liferay/portal/license/public.key");

				byte[] bytes = IOUtils.toByteArray(url.openStream());

				X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
					bytes);

				KeyFactory keyFactory = KeyFactory.getInstance("RSA");

				PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

				byte[] encryptedSymmetricKey = Encryptor.encryptUnencoded(
					publicKey, _symmetricKey.getEncoded());

				_encryptedSymmetricKey = Base64.objectToString(
					encryptedSymmetricKey);
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	private static boolean _isActiveLicense(
			License license, boolean scheduledCheck)
		throws Exception {

		long now = System.currentTimeMillis();

		Properties serverProperties = _getServerProperties();

		String serverId = serverProperties.getProperty("serverId");

		String productId = license.getProductId();

		String randomUuid = UUID.randomUUID().toString();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("version", 1);
		jsonObject.put("cmd", "VALIDATE");
		jsonObject.put("serverId", serverId);
		jsonObject.put("productId", productId);
		jsonObject.put("key", license.getKey());
		jsonObject.put("randomUuid", randomUuid);

		try {
			String response = _sendRequest(jsonObject.toString());

			JSONObject responseJSONObject = JSONFactoryUtil.createJSONObject(
				response);

			boolean active = responseJSONObject.getBoolean("active");
			String responseRandomUuid = responseJSONObject.getString(
				"randomUuid");

			if (active && responseRandomUuid.equals(randomUuid)) {
				serverProperties.put(
					productId + "_lastActiveTime", String.valueOf(now));

				_writeServerProperties(serverProperties);

				return true;
			}
			else {
				return false;
			}
		}
		catch (Exception e) {
			if (_LICENSE_ACTIVE_CHECK_GRACE_TIME >
					_LICENSE_ACTIVE_CHECK_GRACE_MAX_TIME) {

				_LICENSE_ACTIVE_CHECK_GRACE_TIME =
					_LICENSE_ACTIVE_CHECK_GRACE_MAX_TIME;
			}

			StringBundler sb = new StringBundler();

			sb.append("Unable to communicate with ");
			sb.append(_LICENSE_SERVER_URL);
			sb.append(". Please check the connection.");

			long lastActiveTime = GetterUtil.getLong(
				serverProperties.getProperty(productId + "_lastActiveTime"));

			long diff = now - lastActiveTime;

			if ((lastActiveTime <= 0) ||
				(diff > _LICENSE_ACTIVE_CHECK_GRACE_TIME)) {

				throw new Exception(sb.toString());
			}
			else {
				sb.append(" You have a grace period of ");
				sb.append((_LICENSE_ACTIVE_CHECK_GRACE_TIME - diff) / Time.DAY);
				sb.append(" days.");

				_log.error(sb.toString(), e);

				if (scheduledCheck) {
					throw e;
				}
				else {
					_scheduleActiveCheckDaily();
				}
			}
		}

		return true;
	}

	private static void _registerClusterOrder(
			HttpServletRequest request, ClusterNode clusterNode,
			String orderUuid, String productEntryName, int maxServers)
		throws Exception {

		MethodHandler methodHandler = new MethodHandler(
			_registerOrderMethodKey, orderUuid, productEntryName, maxServers);

		ClusterRequest clusterRequest = ClusterRequest.createUnicastRequest(
			methodHandler, clusterNode.getClusterNodeId());

		FutureClusterResponses futureClusterResponses =
			ClusterExecutorUtil.execute(clusterRequest);

		ClusterNodeResponses clusterNodeResponses = futureClusterResponses.get(
			20000, TimeUnit.MILLISECONDS);

		ClusterNodeResponse clusterNodeResponse =
			clusterNodeResponses.getClusterResponse(clusterNode);

		Map<String, Object> attributes =
			(Map<String, Object>)clusterNodeResponse.getResult();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			request.setAttribute(
				clusterNode.getClusterNodeId() + StringPool.UNDERLINE +
					entry.getKey(),
				entry.getValue());
		}
	}

	private static void _scheduleActiveCheckDaily() {
		if (_scheduledThreadPoolExecutor != null) {
			return;
		}

		_scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
			1,
			new ThreadFactory() {

				public Thread newThread(Runnable runnable) {
					Thread thread = new Thread(runnable, "");

					thread.setDaemon(true);

					return thread;
				}

			}
		);

		_scheduledThreadPoolExecutor.scheduleAtFixedRate(
			new Runnable() {

				public void run() {
					_verifyActiveLicenses();
				}

			}, _INITIAL_DELAY, _LICENSE_ACTIVE_CHECK_TIME, TimeUnit.MILLISECONDS
		);
	}

	private static void _sendEmail() throws PortalException, SystemException {
		String subject = "[$PORTAL_URL$] License Unable to Validate";

		StringBundler sb = new StringBundler(8);

		sb.append("Dear [$TO_NAME$],<br /><br />");
		sb.append("Your Liferay Portal instance with host name, ");
		sb.append("[$HOST_NAME$], is unable to contact [$SERVER_URL$]. ");
		sb.append("Please check its internet connection and make sure it is ");
		sb.append("able to connect to [$SERVER_URL$] otherwise your license ");
		sb.append("will become inactive.<br /><br />");
		sb.append("Sincerely,<br />[$FROM_NAME$]<br />[$FROM_ADDRESS$]<br />");
		sb.append("[$PORTAL_URL$]<br />");

		String body = sb.toString();

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		subscriptionSender.setBody(body);
		subscriptionSender.setCompanyId(PortalInstances.getDefaultCompanyId());
		subscriptionSender.setContextAttributes(
			"[$HOST_NAME$]", getHostName(), "[$SERVER_URL$]",
			_LICENSE_SERVER_URL);
		subscriptionSender.setFrom(
			PropsValues.ADMIN_EMAIL_FROM_ADDRESS,
			PropsValues.ADMIN_EMAIL_FROM_NAME);
		subscriptionSender.setHtmlFormat(true);
		subscriptionSender.setMailId("license", _LICENSE_SERVER_URL);
		subscriptionSender.setReplyToAddress(
			PropsValues.ADMIN_EMAIL_FROM_ADDRESS);
		subscriptionSender.setSubject(subject);

		if (PropsValues.OMNIADMIN_USERS.length > 0) {
			for (long userId : PropsValues.OMNIADMIN_USERS) {
				try {
					User user = UserLocalServiceUtil.getUserById(userId);

					if (user.getCompanyId() ==
							PortalInstances.getDefaultCompanyId()) {

						subscriptionSender.addRuntimeSubscribers(
							user.getEmailAddress(), user.getFullName());
					}
				}
				catch (Exception e) {
				}
			}
		}
		else {
			Role role = RoleLocalServiceUtil.getRole(
				PortalInstances.getDefaultCompanyId(),
				RoleConstants.ADMINISTRATOR);

			List<User> users = UserLocalServiceUtil.getRoleUsers(
				role.getRoleId());

			for (User user : users) {
				subscriptionSender.addRuntimeSubscribers(
					user.getEmailAddress(), user.getFullName());
			}
		}

		subscriptionSender.flushNotificationsAsync();
	}

	private static String _sendRequest(String request) throws Exception {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			String serverURL = _LICENSE_SERVER_URL;

			if (!serverURL.endsWith(StringPool.SLASH)) {
				serverURL += StringPool.SLASH;
			}

			serverURL += "osb-portlet/license";

			URL url = new URL(serverURL);

			URLConnection connection = null;

			if (Validator.isNotNull(_PROXY_URL)) {
				if (_log.isInfoEnabled()) {
					_log.info(
						"Using proxy " + _PROXY_URL + StringPool.COLON +
							_PROXY_PORT);
				}

				Proxy proxy = new Proxy(
					Proxy.Type.HTTP,
					new InetSocketAddress(_PROXY_URL, _PROXY_PORT));

				connection = url.openConnection(proxy);

				if (Validator.isNotNull(_PROXY_USER_NAME)) {
					String login =
						_PROXY_USER_NAME + StringPool.COLON + _PROXY_PASSWORD;

					String encodedLogin = Base64.encode(login.getBytes());

					connection.setRequestProperty(
						"Proxy-Authorization", "Basic " + encodedLogin);
				}
			}
			else {
				 connection = url.openConnection();
			}

			connection.setDoOutput(true);

			outputStream = connection.getOutputStream();

			outputStream.write(_encryptRequest(serverURL, request));

			String response = _decryptResponse(
				serverURL, connection.getInputStream());

			if (_log.isDebugEnabled()) {
				_log.debug("Server response: " + response);
			}

			if (Validator.isNull(response)) {
				throw new Exception("Server response is null");
			}

			return response;
		}
		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			}
			catch (Exception e) {
			}

			try {
				if (outputStream != null) {
					outputStream.close();
				}
			}
			catch (Exception e) {
			}
		}
	}

	private static void _setLicense(
		String productId, License license, int licenseState) {

		AtomicStampedReference<License> licenseStampedReference =
			_licenseStampedReferences.get(productId);

		if (licenseStampedReference == null) {
			licenseStampedReference = new AtomicStampedReference<License>(
				license, licenseState);

			_licenseStampedReferences.put(productId, licenseStampedReference);

			return;
		}

		int[] stampHolder = new int[1];

		License curLicense = licenseStampedReference.get(stampHolder);

		int curLicenseState = stampHolder[0];

		if (((licenseState == STATE_GOOD) && (curLicenseState != STATE_GOOD)) ||
			((licenseState == curLicenseState) &&
			 (license.compareTo(curLicense) > 0))) {

			licenseStampedReference.set(license, licenseState);

			_licenseStampedReferences.put(productId, licenseStampedReference);
		}
	}

	private static void _verifyActiveLicenses() {
		boolean connectionFailed = false;

		for (Map.Entry<String, AtomicStampedReference<License>> entry :
				_licenseStampedReferences.entrySet()) {

			AtomicStampedReference<License> licenseStampedReference =
				entry.getValue();

			int[] stampHolder = new int[1];

			License license = licenseStampedReference.get(stampHolder);

			try {
				_isActiveLicense(license, true);
			}
			catch (Exception e) {
				connectionFailed = true;
			}
		}

		if (!connectionFailed) {
			if (_scheduledThreadPoolExecutor != null) {
				_scheduledThreadPoolExecutor.shutdown();
			}
		}
		else {
			try {
				_sendEmail();
			}
			catch (Exception e) {
				_log.error(e, e);
			}
		}
	}

	private static void _writeServerProperties(Properties serverProperties)
		throws Exception {

		File serverIdFile = new File(
			_LICENSE_REPOSITORY_DIR + "/server/serverId");

		String serverPropertiesString = PropertiesUtil.toString(
			serverProperties);

		byte[] bytes = serverPropertiesString.getBytes(StringPool.UTF8);

		for (int i = _keys.length - 1; i >= 0; i--) {
			bytes = Encryptor.encryptUnencoded(_keys[i], bytes);
		}

		FileUtil.write(serverIdFile, bytes);
	}

	private static final long _INITIAL_DELAY = 60 * Time.SECOND;

	private static long _LICENSE_ACTIVE_CHECK_GRACE_TIME = GetterUtil.getLong(
		PropsUtil.get("license.active.check.grace.time"),
		(long)(7.5 * Time.DAY));

	private static final long _LICENSE_ACTIVE_CHECK_GRACE_MAX_TIME =
		(long)(60 * Time.DAY);

	private static final long _LICENSE_ACTIVE_CHECK_TIME = GetterUtil.getLong(
		PropsUtil.get("license.active.check.time"), Time.DAY);

	private static final String _LICENSE_REPOSITORY_DIR =
		PropsValues.LIFERAY_HOME.concat("/data/license");

	private static final String _LICENSE_SERVER_URL = GetterUtil.get(
		PropsUtil.get("license.server.url"), "https://www.liferay.com");

	private static final String _PROXY_PASSWORD = GetterUtil.getString(
		PropsUtil.get("license.proxy.password"));

	private static final int _PROXY_PORT = GetterUtil.getInteger(
		PropsUtil.get("license.proxy.port"), 80);

	private static final String _PROXY_URL = PropsUtil.get("license.proxy.url");

	private static final String _PROXY_USER_NAME = GetterUtil.getString(
		PropsUtil.get("license.proxy.username"));

	private static Log _log = LogFactoryUtil.getLog(LicenseManager.class);

	private static Set<String> _clientIPAddresses = new HashSet<String>();
	private static String _encryptedSymmetricKey;
	private static MethodHandler _getLicensePropertiesMethodHandler =
		new MethodHandler(
			new MethodKey(
				LicenseManager.class.getName(), "getLicenseProperties"));
	private static MethodKey _getLicenseStateMethodKey = new MethodKey(
		LicenseManager.class.getName(), "getLicenseState", String.class);
	private static MethodHandler _getServerInfoMethodHandler =
		new MethodHandler(
			new MethodKey(LicenseManager.class.getName(), "getServerInfo"));
	private static String _hostName;
	private static Set<String> _ipAddresses;
	private static Key[] _keys;
	private static Map<String, AtomicStampedReference<License>>
		_licenseStampedReferences =
			new HashMap<String, AtomicStampedReference<License>>();
	private static Set<String> _macAddresses;
	private static Pattern _macAddressPattern = Pattern.compile(
		"\\s((\\p{XDigit}{1,2}(-|:)){5}(\\p{XDigit}{1,2}))(?:\\s|$)");
	private static MethodKey _registerOrderMethodKey = new MethodKey(
		LicenseManager.class.getName(), "registerOrder", String.class,
		String.class, int.class);
	private static ScheduledThreadPoolExecutor _scheduledThreadPoolExecutor;
	private static String _serverId;
	private static Key _symmetricKey;
	private static LicenseValidator _validatorChain;

	static {
		_initKeys();

		LicenseTypeValidator licenseTypeValidator = new LicenseTypeValidator();
		ClusterValidator clusterValidator = new ClusterValidator();
		ProductionValidator productionValidator = new ProductionValidator();
		LimitedValidator limitedValidator = new LimitedValidator();
		PerUserValidator perUserValidator = new PerUserValidator();
		DeveloperValidator developerValidator = new DeveloperValidator();

		licenseTypeValidator.setNextValidator(clusterValidator);
		clusterValidator.setNextValidator(productionValidator);
		productionValidator.setNextValidator(limitedValidator);
		limitedValidator.setNextValidator(perUserValidator);
		perUserValidator.setNextValidator(developerValidator);

		_validatorChain = licenseTypeValidator;
	}

}