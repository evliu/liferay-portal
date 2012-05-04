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

package com.liferay.portlet.translator.util;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.webcache.WebCacheException;
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portlet.translator.model.Translation;

import java.net.URL;

/**
 * @author Brian Wing Shun Chan
 */
public class TranslationWebCacheItem implements WebCacheItem {

	public TranslationWebCacheItem(String translationId, String fromText) {
		_translationId = translationId;
		_fromText = fromText;
	}

	public Object convert(String key) throws WebCacheException {
		Translation translation = new Translation(_translationId, _fromText);

		try {
			StringBundler sb = new StringBundler(6);
			
			//transition babelfish translationId parameter to Google Translate parameters
			String _fromId = _translationId.substring(0,_translationId.indexOf("_")).trim();
			String _toId = _translationId.substring(_translationId.indexOf("_")+1).trim();

			//Builds Request URL
			sb.append("http://translate.google.com/translate_a/t?client=t&text=");
			sb.append(HttpUtil.encodeURL(_fromText));
			sb.append("&hl=");
			sb.append(_fromId);
			sb.append("&tl=");
			sb.append(_toId);

			//Sends RequestURL 'sb' and receives a String with the return XHR payload
			String text = HttpUtil.URLtoString(new URL(sb.toString()));

			System.out.println("translationId: "+ _translationId+"\ntext: " + text);
			
			//Parses out form: [[["translatedText","originalText","",""]],[[...
			int x = text.indexOf("\"");

			x = text.indexOf("\"", x) + 1;

			int y = text.indexOf("\",\"", x);

			//Stores translation in return payload 'toText'
			String toText = text.substring(x, y).trim();

			toText = StringUtil.replace(
				toText, CharPool.NEW_LINE, CharPool.SPACE);

			System.out.println("toText in TWCI.java: " + toText);
			
			//Sets payload property in 'translation' object
			translation.setToText(toText);
		}
		catch (Exception e) {
			throw new WebCacheException(e);
		}

		return translation;
	}

	public long getRefreshTime() {
		return _REFRESH_TIME;
	}

	private static final long _REFRESH_TIME = Time.DAY * 90;

	private String _fromText;
	private String _translationId;

}