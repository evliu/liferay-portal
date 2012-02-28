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

import com.liferay.portal.deploy.auto.AutoDeployer;
import com.liferay.portal.kernel.deploy.auto.AutoDeployException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.license.tools.deploy.LicenseDeployer;

import java.io.File;

/**
 * @author Shuyang Zhou
 */
public class LicenseAutoDeployer
	extends LicenseDeployer implements AutoDeployer {

	public void autoDeploy(File file, String context)
		throws AutoDeployException {

		try {
			licenseFileContent = FileUtil.read(file);

			deploy();
		}
		catch (Exception e) {
			throw new AutoDeployException(e);
		}
	}

}