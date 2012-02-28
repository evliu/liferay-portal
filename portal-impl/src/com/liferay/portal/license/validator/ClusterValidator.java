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

import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterNodeResponses;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.cluster.FutureClusterResponses;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.license.License;
import com.liferay.portal.license.LicenseManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Shuyang Zhou
 * @author Amos Fong
 */
public class ClusterValidator extends LicenseValidator {

	@Override
	public void doValidateVersion1(License license) throws Exception {
		validateMacAddresses(license.getServerIds());
	}

	@Override
	public void doValidateVersion2(License license) throws Exception {
		if (license.getMaxServers() <= 0) {
			throw new Exception(
				"The maximum allowed servers must be greater than 0");
		}

		try {
			_validateCluster(license);
		}
		catch (Exception e) {
			if (_log.isInfoEnabled()) {
				_log.info("A verify process will start in 1 minute");
			}

			Thread thread = new AsyncVerifyThread();

			thread.setDaemon(true);

			thread.start();

			throw e;
		}
	}

	@Override
	public void doValidateVersion3(License license) throws Exception {
		String licenseEntryType = license.getLicenseEntryType();

		if (licenseEntryType.equals(License.TYPE_DEVELOPER_CLUSTER)) {
			return;
		}

		validateServer(license);
	}

	@Override
	public String[] getValidTypes() {
		return _VALID_TYPES;
	}

	private void _validateCluster(License license) throws Exception {
		List<ClusterNode> clusterNodes = ClusterExecutorUtil.getClusterNodes();

		if (clusterNodes.size() <= 1) {
			return;
		}

		clusterNodes.remove(ClusterExecutorUtil.getLocalClusterNode());

		ClusterNode retrieveNode = clusterNodes.get(0);

		MethodHandler methodHandler = new MethodHandler(
			_getLicensePropertiesMethodKey, license.getProductId());

		ClusterRequest clusterRequest = ClusterRequest.createUnicastRequest(
			methodHandler, retrieveNode.getClusterNodeId());

		FutureClusterResponses futureClusterResponses =
			ClusterExecutorUtil.execute(clusterRequest);

		ClusterNodeResponses clusterNodeResponses = futureClusterResponses.get(
			20000, TimeUnit.MILLISECONDS);

		ClusterNodeResponse clusterNodeResponse =
			clusterNodeResponses.getClusterResponse(retrieveNode);

		Object result = clusterNodeResponse.getResult();

		if (result != null) {
			if (!(result instanceof Map)) {
				throw new Exception(
					result.getClass().getName() + " is not a Map object");
			}

			Map<String, String> licenseProperties =
				LicenseManager.getLicenseProperties(license.getProductId());

			Map<String, String> remoteProperties = (Map<String, String>)result;

			if (!remoteProperties.equals(licenseProperties)) {
				StringBundler sb = new StringBundler();

				sb.append("Remote license does not match local license. ");
				sb.append("Local license: ");
				sb.append(licenseProperties);
				sb.append("\nRemote node: ");
				sb.append(retrieveNode);
				sb.append("\nRemote license: ");
				sb.append(remoteProperties);
				sb.append("\nMixing licenses is not allowed. Local server is ");
				sb.append("shutting down.");

				_log.error(sb.toString());

				System.exit(EXIT_CODE_INCONSISTENT_LICENSES);
			}
		}

		clusterNodes = ClusterExecutorUtil.getClusterNodes();

		if (clusterNodes.size() > license.getMaxServers()) {
			StringBundler sb = new StringBundler();

			sb.append("Detected ");
			sb.append(clusterNodes.size());
			sb.append(" nodes in the cluster. Your license allows a maximum ");
			sb.append("of ");
			sb.append(license.getMaxServers());
			sb.append(" cluster nodes. Local server is shutting down.");

			_log.error(sb.toString());

			System.exit(EXIT_CODE_EXCEEDED_MAX_SERVERS);
		}
	}

	private static final String[] _VALID_TYPES = {
		License.TYPE_CLUSTER, License.TYPE_DEVELOPER_CLUSTER
	};

	private static final int _VERIFY_RETRY_ATTEMPTS = 10;

	private static final int _VERIFY_RETRY_INTERVAL = 60 * 1000;

	private static Log _log = LogFactoryUtil.getLog(ClusterValidator.class);

	private static MethodKey _getLicensePropertiesMethodKey = new MethodKey(
		LicenseManager.class.getName(), "getLicenseProperties", long.class);

	private class AsyncVerifyThread extends Thread {

		public void run() {
			for (int i = 0; i < _VERIFY_RETRY_ATTEMPTS; i++) {
				try {
					Thread.sleep(_VERIFY_RETRY_INTERVAL);
				}
				catch (InterruptedException ie) {
				}

				if (_log.isInfoEnabled()) {
					_log.info(
						"Verify process starting, this is attempt " + (i + 1));
				}

				LicenseManager.checkBinaryLicenses();

				int licenseState = LicenseManager.getLicenseState();

				if (licenseState == LicenseManager.STATE_GOOD) {
					break;
				}
			}
		}

	}

}