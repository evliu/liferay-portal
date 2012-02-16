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

package com.liferay.portal.upgrade;

import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.upgrade.v6_0_12.UpgradeAsset;
import com.liferay.portal.upgrade.v6_0_12.UpgradeBlogs;
import com.liferay.portal.upgrade.v6_0_12.UpgradeDocumentLibrary;
import com.liferay.portal.upgrade.v6_0_12.UpgradeExpando;
import com.liferay.portal.upgrade.v6_0_12.UpgradeLock;
import com.liferay.portal.upgrade.v6_0_12.UpgradeMessageBoards;
import com.liferay.portal.upgrade.v6_0_12.UpgradePermission;
import com.liferay.portal.upgrade.v6_0_12.UpgradePortletPreferences;
import com.liferay.portal.upgrade.v6_0_12.UpgradeResourcePermission;
import com.liferay.portal.upgrade.v6_0_12.UpgradeScheduler;
import com.liferay.portal.upgrade.v6_0_12.UpgradeSchema;
import com.liferay.portal.upgrade.v6_0_12.UpgradeScopes;
import com.liferay.portal.upgrade.v6_0_12.UpgradeUserName;
import com.liferay.portal.upgrade.v6_0_12.UpgradeVirtualHost;

/**
 * @author Brian Wing Shun Chan
 * @author Douglas Wong
 */
public class UpgradeProcess_6_0_12 extends UpgradeProcess {

	@Override
	public int getThreshold() {
		return ReleaseInfo.RELEASE_6_0_12_BUILD_NUMBER;
	}

	@Override
	protected void doUpgrade() throws Exception {
		upgrade(UpgradeSchema.class);
		upgrade(UpgradeAsset.class);
		upgrade(UpgradeBlogs.class);
		upgrade(UpgradeDocumentLibrary.class);
		upgrade(UpgradeExpando.class);
		upgrade(UpgradeLock.class);
		upgrade(UpgradeMessageBoards.class);
		upgrade(UpgradePermission.class);
		upgrade(UpgradePortletPreferences.class);
		upgrade(UpgradeResourcePermission.class);
		upgrade(UpgradeScheduler.class);
		upgrade(UpgradeScopes.class);
		upgrade(UpgradeUserName.class);
		upgrade(UpgradeVirtualHost.class);
	}

}