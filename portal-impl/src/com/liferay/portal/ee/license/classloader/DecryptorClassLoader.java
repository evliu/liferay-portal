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

package com.liferay.portal.ee.license.classloader;

import com.liferay.portal.kernel.util.PortalClassLoaderUtil;

/**
 * @author Brian Wing Shun Chan
 */
public class DecryptorClassLoader extends ClassLoader {

	public DecryptorClassLoader() {
		super(PortalClassLoaderUtil.getClassLoader());
	}

	public synchronized Class<?> loadClass(String name)
		throws ClassNotFoundException {

		Class<?> c = findLoadedClass(name);

		if (c == null) {
			c = super.loadClass(name);
		}

		if (c == null) {
			throw new ClassNotFoundException(name);
		}

		return c;
	}

}