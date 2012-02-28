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

import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.DigesterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.util.Encryptor;

import java.net.URL;

import java.security.Key;

import org.apache.commons.io.IOUtils;

/**
 * @author Brian Wing Shun Chan
 */
public class DecryptorClassLoader extends ClassLoader {

	public DecryptorClassLoader() {
		super(PortalClassLoaderUtil.getClassLoader());

		try {
			String content = StringUtil.read(
				getParent(),
				"com/liferay/portal/license/classloader/keys.txt");

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
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized Class<?> loadClass(String name)
		throws ClassNotFoundException {

		Class<?> c = findLoadedClass(name);

		if (c == null) {
			if (name.endsWith(".license.EventsProcessorImpl") ||
				name.endsWith(".license.StartupAction")) {

				try {
					String resourceName = name.replace(
						StringPool.PERIOD, StringPool.SLASH);

					URL url = super.getResource(resourceName);

					byte[] bytes = IOUtils.toByteArray(url.openStream());

					for (Key key : _keys) {
						bytes = Encryptor.decryptUnencodedAsBytes(key, bytes);
					}

					c = defineClass(
						name, bytes, 0, bytes.length,
						getClass().getProtectionDomain());
				}
				catch (Exception e) {
					throw new ClassNotFoundException(e.getMessage(), e);
				}
			}
			else {
				c = super.loadClass(name);
			}

			if (c == null) {
				throw new ClassNotFoundException(name);
			}
		}

		return c;
	}

	private Key[] _keys = new Key[3];

}