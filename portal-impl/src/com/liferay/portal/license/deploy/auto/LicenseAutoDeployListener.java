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

package com.liferay.portal.license.deploy.auto;

import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.deploy.auto.AutoDeployListener;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.File;

/**
 * @author Simon Zhang
 * @author Shuyang Zhou
 */
public class LicenseAutoDeployListener implements AutoDeployListener {

	public LicenseAutoDeployListener() {
		_licenseAutoDeployer = new LicenseAutoDeployer();
	}

	public void deploy(File file) throws AutoDeployException {
		deploy(file, null);
	}

	public void deploy(File file, String context)
		throws AutoDeployException {

		if (_log.isDebugEnabled()) {
			_log.debug("Invoking deploy for " + file.getPath());
		}

		String extension = FileUtil.getExtension(file.getName());

		if (!extension.equals("xml")) {
			return;
		}

		try {
			String licenseFileContent = FileUtil.read(file);

			Document document = SAXReaderUtil.read(licenseFileContent);

			Element rootElement = document.getRootElement();

			String rootElementName = rootElement.getName();

			if (!rootElementName.equals("license")) {
				return;
			}
		}
		catch (Exception e) {
			return;
		}

		if (_log.isInfoEnabled()) {
			_log.info("Copying license for " + file.getPath());
		}

		_licenseAutoDeployer.autoDeploy(file, context);
	}

	private static Log _log = LogFactoryUtil.getLog(
		LicenseAutoDeployListener.class);

	private LicenseAutoDeployer _licenseAutoDeployer;

}