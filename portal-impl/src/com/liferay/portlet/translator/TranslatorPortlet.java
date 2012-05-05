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

package com.liferay.portlet.translator;

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.translator.model.Translation;
import com.liferay.portlet.translator.util.TranslatorUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

/**
 * @author Brian Wing Shun Chan
 */
public class TranslatorPortlet extends MVCPortlet {

	@Override
	public void processAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws PortletException {

		try {
			String fromId = ParamUtil.getString(actionRequest, "fromId");
			for(int i=1; i<=5; i++){
				String toId = ParamUtil.getString(actionRequest, "toId"+i);
				String translationId = fromId + "_" + toId;
				String fromText = ParamUtil.getString(actionRequest, "text");
	
				if (Validator.isNotNull(fromText)) {
					Translation translation = TranslatorUtil.getTranslation(
							translationId, fromText);
					actionRequest.setAttribute(
						"TRANSLATOR_TRANSLATION"+i, translation);
				}
			}
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

}