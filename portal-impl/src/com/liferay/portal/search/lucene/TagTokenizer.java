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

package com.liferay.portal.search.lucene;

import com.liferay.portal.kernel.util.CharPool;

import java.io.Reader;

import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.util.AttributeSource;

/**
 * @author Jonathan Potter
 */
public class TagTokenizer extends CharTokenizer {

	public TagTokenizer(AttributeSource attributeSource, Reader reader) {
		super(attributeSource, reader);
	}

	public TagTokenizer(
		AttributeSource.AttributeFactory attributeFactory, Reader reader) {

		super(attributeFactory, reader);
	}

	public TagTokenizer(Reader reader) {
		super(reader);
	}

	@Override
	protected boolean isTokenChar(char c) {
		if (c != CharPool.COMMA) {
			return true;
		}
		else {
			return false;
		}
	}

}