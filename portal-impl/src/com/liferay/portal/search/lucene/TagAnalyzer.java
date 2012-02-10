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

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

/**
 * @author Jonathan Potter
 */
public class TagAnalyzer extends Analyzer {

	@Override
	public TokenStream reusableTokenStream(String fieldName, Reader reader)
		throws IOException {

		Tokenizer tokenizer = (Tokenizer)getPreviousTokenStream();

		if (tokenizer == null) {
			tokenizer = new TagTokenizer(reader);

			setPreviousTokenStream(tokenizer);
		}
		else {
			tokenizer.reset(reader);
		}

		return tokenizer;
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new TagTokenizer(reader);
	}

}