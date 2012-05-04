<%--
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
--%>

<%@ include file="/html/portlet/translator/init.jsp" %>

<%
Translation translation = (Translation)request.getAttribute(WebKeys.TRANSLATOR_TRANSLATION);

if (translation == null) {
	translation = new Translation(PropsUtil.get(PropsKeys.TRANSLATOR_DEFAULT_LANGUAGES), StringPool.BLANK, StringPool.BLANK);
}
%>

<portlet:actionURL var="portletURL" />

<aui:form accept-charset="UTF-8" action="<%= portletURL %>" method="post" name="fm">
	<c:if test="<%= Validator.isNotNull(translation.getToText()) %>">
		<%= HtmlUtil.escape(translation.getToText()) %>
	</c:if>

	<aui:fieldset>
		<aui:input cssClass="lfr-textarea-container" label="" name="text" type="textarea" value="<%= translation.getFromText() %>" wrap="soft" />

		<%--BABEL FISH CODE
		 <aui:select label="" name="id">
			<aui:option label="en_zh" selected='<%= translation.getTranslationId().equals("en_zh") %>' />
			<aui:option label="en_zt" selected='<%= translation.getTranslationId().equals("en_zt") %>' />
			<aui:option label="en_nl" selected='<%= translation.getTranslationId().equals("en_nl") %>' />
			<aui:option label="en_fr" selected='<%= translation.getTranslationId().equals("en_fr") %>' />
			<aui:option label="en_de" selected='<%= translation.getTranslationId().equals("en_de") %>' />
			<aui:option label="en_it" selected='<%= translation.getTranslationId().equals("en_it") %>' />
			<aui:option label="en_ja" selected='<%= translation.getTranslationId().equals("en_ja") %>' />
			<aui:option label="en_ko" selected='<%= translation.getTranslationId().equals("en_ko") %>' />
			<aui:option label="en_pt" selected='<%= translation.getTranslationId().equals("en_pt") %>' />
			<aui:option label="en_es" selected='<%= translation.getTranslationId().equals("en_es") %>' />
			<aui:option label="zh_en" selected='<%= translation.getTranslationId().equals("zh_en") %>' />
			<aui:option label="zt_en" selected='<%= translation.getTranslationId().equals("zt_en") %>' />
			<aui:option label="nl_en" selected='<%= translation.getTranslationId().equals("nl_en") %>' />
			<aui:option label="fr_en" selected='<%= translation.getTranslationId().equals("fr_en") %>' />
			<aui:option label="fr_de" selected='<%= translation.getTranslationId().equals("fr_de") %>' />
			<aui:option label="de_en" selected='<%= translation.getTranslationId().equals("de_en") %>' />
			<aui:option label="de_fr" selected='<%= translation.getTranslationId().equals("de_fr") %>' />
			<aui:option label="it_en" selected='<%= translation.getTranslationId().equals("it_en") %>' />
			<aui:option label="ja_en" selected='<%= translation.getTranslationId().equals("ja_en") %>' />
			<aui:option label="ko_en" selected='<%= translation.getTranslationId().equals("ko_en") %>' />
			<aui:option label="pt_en" selected='<%= translation.getTranslationId().equals("pt_en") %>' />
			<aui:option label="ru_en" selected='<%= translation.getTranslationId().equals("ru_en") %>' />
			<aui:option label="es_en" selected='<%= translation.getTranslationId().equals("es_en") %>' />
		</aui:select> --%>
		
		Select From Language:
		<select name="<portlet:namespace />fromId" label="Select From Language">
			<%-- <option value="auto">Detect language</option> --%>
			<option selected="" value="auto">Try Detect Language</option>
			<option value="af">Afrikaans</option>
			<option value="sq">Albanian</option>
			<option value="ar">Arabic</option>
			<option value="be">Belarusian</option>
			<option value="bg">Bulgarian</option>
			<option value="ca">Catalan</option>
			<option value="zh-CN">Chinese (Simplified)</option>
			<option value="zh-TW">Chinese (Traditional)</option>
			<option value="hr">Croatian</option>
			<option value="cs">Czech</option>
			<option value="da">Danish</option>
			<option value="nl">Dutch</option>
			<option value="en">English</option>
			<option value="eo">Esperanto</option>
			<option value="et">Estonian</option>
			<option value="tl">Filipino</option>
			<option value="fi">Finnish</option>
			<option value="fr">French</option>
			<option value="gl">Galician</option>
			<option value="de">German</option>
			<option value="el">Greek</option>
			<option value="ht">Haitian Creole</option>
			<option value="iw">Hebrew</option>
			<option value="hi">Hindi</option>
			<option value="hu">Hungarian</option>
			<option value="is">Icelandic</option>
			<option value="id">Indonesian</option>
			<option value="ga">Irish</option>
			<option value="it">Italian</option>
			<option value="ja">Japanese</option>
			<option value="ko">Korean</option>
			<option value="lv">Latvian</option>
			<option value="lt">Lithuanian</option>
			<option value="mk">Macedonian</option>
			<option value="ms">Malay</option>
			<option value="mt">Maltese</option>
			<option value="no">Norwegian</option>
			<option value="fa">Persian</option>
			<option value="pl">Polish</option>
			<option value="pt">Portuguese</option>
			<option value="ro">Romanian</option>
			<option value="ru">Russian</option>
			<option value="sr">Serbian</option>
			<option value="sk">Slovak</option>
			<option value="sl">Slovenian</option>
			<option value="es">Spanish</option>
			<option value="sw">Swahili</option>
			<option value="sv">Swedish</option>
			<option value="th">Thai</option>
			<option value="tr">Turkish</option>
			<option value="uk">Ukrainian</option>
			<option value="vi">Vietnamese</option>
			<option value="cy">Welsh</option>
			<option value="yi">Yiddish</option>
		</select>
		
		<br />Select To Language:
		<select name="<portlet:namespace />toId" label="Select To Language">
			<option value="af">Afrikaans</option>
			<option value="sq">Albanian</option>
			<option value="ar">Arabic</option>
			<option value="be">Belarusian</option>
			<option value="bg">Bulgarian</option>
			<option value="ca">Catalan</option>
			<option value="zh-CN">Chinese (Simplified)</option>
			<option value="zh-TW">Chinese (Traditional)</option>
			<option value="hr">Croatian</option>
			<option value="cs">Czech</option>
			<option value="da">Danish</option>
			<option value="nl">Dutch</option>
			<option selected="" value="en">English</option>
			<option value="eo">Esperanto</option>
			<option value="et">Estonian</option>
			<option value="tl">Filipino</option>
			<option value="fi">Finnish</option>
			<option value="fr">French</option>
			<option value="gl">Galician</option>
			<option value="de">German</option>
			<option value="el">Greek</option>
			<option value="ht">Haitian Creole</option>
			<option value="iw">Hebrew</option>
			<option value="hi">Hindi</option>
			<option value="hu">Hungarian</option>
			<option value="is">Icelandic</option>
			<option value="id">Indonesian</option>
			<option value="ga">Irish</option>
			<option value="it">Italian</option>
			<option value="ja">Japanese</option>
			<option value="ko">Korean</option>
			<option value="lv">Latvian</option>
			<option value="lt">Lithuanian</option>
			<option value="mk">Macedonian</option>
			<option value="ms">Malay</option>
			<option value="mt">Maltese</option>
			<option value="no">Norwegian</option>
			<option value="fa">Persian</option>
			<option value="pl">Polish</option>
			<option value="pt">Portuguese</option>
			<option value="ro">Romanian</option>
			<option value="ru">Russian</option>
			<option value="sr">Serbian</option>
			<option value="sk">Slovak</option>
			<option value="sl">Slovenian</option>
			<option value="es">Spanish</option>
			<option value="sw">Swahili</option>
			<option value="sv">Swedish</option>
			<option value="th">Thai</option>
			<option value="tr">Turkish</option>
			<option value="uk">Ukrainian</option>
			<option value="vi">Vietnamese</option>
			<option value="cy">Welsh</option>
			<option value="yi">Yiddish</option>
		</select>
		
	</aui:fieldset>
	


	<aui:button-row>
		<aui:button type="submit" value="translate" />
	</aui:button-row>
</aui:form>

<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
	<aui:script>
		Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />text);
	</aui:script>
</c:if>
