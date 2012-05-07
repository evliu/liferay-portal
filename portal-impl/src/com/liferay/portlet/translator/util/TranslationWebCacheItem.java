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

import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.webcache.WebCacheException;
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.HttpImpl;
import com.liferay.portlet.translator.model.Translation;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brian Wing Shun Chan
 */
public class TranslationWebCacheItem implements WebCacheItem {

	public TranslationWebCacheItem(String translationId, String fromText) {
		_translationId = translationId;
		_fromText = fromText;
		
	}

	public static void main(String args[]) throws Throwable{
		new HttpUtil().setHttp(new HttpImpl());
		new FileUtil().setFile(new FileImpl());
		new JSONFactoryUtil().setJSONFactory(new JSONFactoryImpl());
		
		TranslationWebCacheItem asdf = new TranslationWebCacheItem(
				"en_zh","This is a test sentence.\n Another update has been administered.\n A third Sentence. This is a new line.");
		asdf.convert("asdf");
		
		System.exit(0);
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
			JSONArray textJSON = JSONFactoryUtil.createJSONArray(text);
			String toText = "";
			
			//Step through the JSONArray and pull out the translated component, then append to 'toText'
			for(int i = 0; i < textJSON.getJSONArray(0).length(); i++){
				String value = textJSON.getJSONArray(0).getJSONArray(i).getString(0);

				//Google Translate adds extra spaces if there are following sentences
				//both behind the sentence marker (ie:period, question mark) and after.
				//this compensates
				value = value.trim();
				value = value.replaceFirst("\\s+(\\p{Punct})", "$1") + " ";
				toText += value;
			}

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