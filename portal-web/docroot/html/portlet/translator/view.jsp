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
Translation translation1 = (Translation)request.getAttribute("TRANSLATOR_TRANSLATION1");
Translation translation2 = (Translation)request.getAttribute("TRANSLATOR_TRANSLATION2");
Translation translation3 = (Translation)request.getAttribute("TRANSLATOR_TRANSLATION3");
Translation translation4 = (Translation)request.getAttribute("TRANSLATOR_TRANSLATION4");
Translation translation5 = (Translation)request.getAttribute("TRANSLATOR_TRANSLATION5");

	if (translation1 == null) {
		translation1 = new Translation(PropsUtil.get(PropsKeys.TRANSLATOR_DEFAULT_LANGUAGES), StringPool.BLANK, StringPool.BLANK);
	}
	


		
%>

<portlet:actionURL var="portletURL" />

<aui:form accept-charset="UTF-8" action="<%= portletURL %>" method="post" name="fm">
	<c:if test="<%= Validator.isNotNull(translation1.getToText()) %>">
	<%
		//translationId into matching languageName
		String id1 = translation1.getTranslationId().substring(translation1.getTranslationId().indexOf("_")+1).trim();
		String id2 = translation2.getTranslationId().substring(translation2.getTranslationId().indexOf("_")+1).trim();
		String id3 = translation3.getTranslationId().substring(translation3.getTranslationId().indexOf("_")+1).trim();
		String id4 = translation4.getTranslationId().substring(translation4.getTranslationId().indexOf("_")+1).trim();
		String id5 = translation5.getTranslationId().substring(translation5.getTranslationId().indexOf("_")+1).trim();
		if(id1.equals("af")) id1="Afrikaans"; if(id1.equals("sq")) id1="Albanian"; if(id1.equals("ar")) id1="Arabic"; if(id1.equals("be")) id1="Belarusian"; if(id1.equals("bg")) id1="Bulgarian";
		if(id1.equals("ca")) id1="Catalan"; if(id1.equals("zh-CN")) id1="Chinese (Simplified)"; if(id1.equals("zh-TW")) id1="Chinese (Traditional)"; if(id1.equals("hr")) id1="Croatian";
		if(id1.equals("cs")) id1="Czech"; if(id1.equals("da")) id1="Danish"; if(id1.equals("nl")) id1="Dutch"; if(id1.equals("en")) id1="English"; if(id1.equals("eo")) id1="Esperanto";
		if(id1.equals("et")) id1="Estonian"; if(id1.equals("tl")) id1="Filipino"; if(id1.equals("fi")) id1="Finnish"; if(id1.equals("fr")) id1="French"; if(id1.equals("gl")) id1="Galician";
		if(id1.equals("de")) id1="German"; if(id1.equals("el")) id1="Greek"; if(id1.equals("ht")) id1="Haitian Creole"; if(id1.equals("iw")) id1="Hebrew"; if(id1.equals("hi")) id1="Hindi";
		if(id1.equals("hu")) id1="Hungarian"; if(id1.equals("is")) id1="Icelandic"; if(id1.equals("id")) id1="Indonesian"; if(id1.equals("ga")) id1="Irish"; if(id1.equals("it")) id1="Italian";
		if(id1.equals("ja")) id1="Japanese"; if(id1.equals("ko")) id1="Korean"; if(id1.equals("lv")) id1="Latvian"; if(id1.equals("lt")) id1="Lithuanian"; if(id1.equals("mk")) id1="Macedonian";
		if(id1.equals("ms")) id1="Malay"; if(id1.equals("mt")) id1="Maltese"; if(id1.equals("no")) id1="Norwegian"; if(id1.equals("fa")) id1="Persian"; if(id1.equals("pl")) id1="Polish";
		if(id1.equals("pt")) id1="Portuguese"; if(id1.equals("ro")) id1="Romanian"; if(id1.equals("ru")) id1="Russian"; if(id1.equals("sr")) id1="Serbian"; if(id1.equals("sk")) id1="Slovak";
		if(id1.equals("sl")) id1="Slovenian"; if(id1.equals("es")) id1="Spanish"; if(id1.equals("sw")) id1="Swahili"; if(id1.equals("sv")) id1="Swedish"; if(id1.equals("th")) id1="Thai";
		if(id1.equals("tr")) id1="Turkish"; if(id1.equals("uk")) id1="Ukrainian"; if(id1.equals("vi")) id1="Vietnamese"; if(id1.equals("cy")) id1="Welsh"; if(id1.equals("yi")) id1="Yiddish";
		if(id2.equals("af")) id2="Afrikaans"; if(id2.equals("sq")) id2="Albanian"; if(id2.equals("ar")) id2="Arabic"; if(id2.equals("be")) id2="Belarusian"; if(id2.equals("bg")) id2="Bulgarian";
		if(id2.equals("ca")) id2="Catalan"; if(id2.equals("zh-CN")) id2="Chinese (Simplified)"; if(id2.equals("zh-TW")) id2="Chinese (Traditional)"; if(id2.equals("hr")) id2="Croatian";
		if(id2.equals("cs")) id2="Czech"; if(id2.equals("da")) id2="Danish"; if(id2.equals("nl")) id2="Dutch"; if(id2.equals("en")) id2="English"; if(id2.equals("eo")) id2="Esperanto";
		if(id2.equals("et")) id2="Estonian"; if(id2.equals("tl")) id2="Filipino"; if(id2.equals("fi")) id2="Finnish"; if(id2.equals("fr")) id2="French"; if(id2.equals("gl")) id2="Galician";
		if(id2.equals("de")) id2="German"; if(id2.equals("el")) id2="Greek"; if(id2.equals("ht")) id2="Haitian Creole"; if(id2.equals("iw")) id2="Hebrew"; if(id2.equals("hi")) id2="Hindi";
		if(id2.equals("hu")) id2="Hungarian"; if(id2.equals("is")) id2="Icelandic"; if(id2.equals("id")) id2="Indonesian"; if(id2.equals("ga")) id2="Irish"; if(id2.equals("it")) id2="Italian";
		if(id2.equals("ja")) id2="Japanese"; if(id2.equals("ko")) id2="Korean"; if(id2.equals("lv")) id2="Latvian"; if(id2.equals("lt")) id2="Lithuanian"; if(id2.equals("mk")) id2="Macedonian";
		if(id2.equals("ms")) id2="Malay"; if(id2.equals("mt")) id2="Maltese"; if(id2.equals("no")) id2="Norwegian"; if(id2.equals("fa")) id2="Persian"; if(id2.equals("pl")) id2="Polish";
		if(id2.equals("pt")) id2="Portuguese"; if(id2.equals("ro")) id2="Romanian"; if(id2.equals("ru")) id2="Russian"; if(id2.equals("sr")) id2="Serbian"; if(id2.equals("sk")) id2="Slovak";
		if(id2.equals("sl")) id2="Slovenian"; if(id2.equals("es")) id2="Spanish"; if(id2.equals("sw")) id2="Swahili"; if(id2.equals("sv")) id2="Swedish"; if(id2.equals("th")) id2="Thai";
		if(id2.equals("tr")) id2="Turkish"; if(id2.equals("uk")) id2="Ukrainian"; if(id2.equals("vi")) id2="Vietnamese"; if(id2.equals("cy")) id2="Welsh"; if(id2.equals("yi")) id2="Yiddish";
		if(id3.equals("af")) id3="Afrikaans"; if(id3.equals("sq")) id3="Albanian"; if(id3.equals("ar")) id3="Arabic"; if(id3.equals("be")) id3="Belarusian"; if(id3.equals("bg")) id3="Bulgarian";
		if(id3.equals("ca")) id3="Catalan"; if(id3.equals("zh-CN")) id3="Chinese (Simplified)"; if(id3.equals("zh-TW")) id3="Chinese (Traditional)"; if(id3.equals("hr")) id3="Croatian";
		if(id3.equals("cs")) id3="Czech"; if(id3.equals("da")) id3="Danish"; if(id3.equals("nl")) id3="Dutch"; if(id3.equals("en")) id3="English"; if(id3.equals("eo")) id3="Esperanto";
		if(id3.equals("et")) id3="Estonian"; if(id3.equals("tl")) id3="Filipino"; if(id3.equals("fi")) id3="Finnish"; if(id3.equals("fr")) id3="French"; if(id3.equals("gl")) id3="Galician";
		if(id3.equals("de")) id3="German"; if(id3.equals("el")) id3="Greek"; if(id3.equals("ht")) id3="Haitian Creole"; if(id3.equals("iw")) id3="Hebrew"; if(id3.equals("hi")) id3="Hindi";
		if(id3.equals("hu")) id3="Hungarian"; if(id3.equals("is")) id3="Icelandic"; if(id3.equals("id")) id3="Indonesian"; if(id3.equals("ga")) id3="Irish"; if(id3.equals("it")) id3="Italian";
		if(id3.equals("ja")) id3="Japanese"; if(id3.equals("ko")) id3="Korean"; if(id3.equals("lv")) id3="Latvian"; if(id3.equals("lt")) id3="Lithuanian"; if(id3.equals("mk")) id3="Macedonian";
		if(id3.equals("ms")) id3="Malay"; if(id3.equals("mt")) id3="Maltese"; if(id3.equals("no")) id3="Norwegian"; if(id3.equals("fa")) id3="Persian"; if(id3.equals("pl")) id3="Polish";
		if(id3.equals("pt")) id3="Portuguese"; if(id3.equals("ro")) id3="Romanian"; if(id3.equals("ru")) id3="Russian"; if(id3.equals("sr")) id3="Serbian"; if(id3.equals("sk")) id3="Slovak";
		if(id3.equals("sl")) id3="Slovenian"; if(id3.equals("es")) id3="Spanish"; if(id3.equals("sw")) id3="Swahili"; if(id3.equals("sv")) id3="Swedish"; if(id3.equals("th")) id3="Thai";
		if(id3.equals("tr")) id3="Turkish"; if(id3.equals("uk")) id3="Ukrainian"; if(id3.equals("vi")) id3="Vietnamese"; if(id3.equals("cy")) id3="Welsh"; if(id3.equals("yi")) id3="Yiddish";
		if(id4.equals("af")) id4="Afrikaans"; if(id4.equals("sq")) id4="Albanian"; if(id4.equals("ar")) id4="Arabic"; if(id4.equals("be")) id4="Belarusian"; if(id4.equals("bg")) id4="Bulgarian";
		if(id4.equals("ca")) id4="Catalan"; if(id4.equals("zh-CN")) id4="Chinese (Simplified)"; if(id4.equals("zh-TW")) id4="Chinese (Traditional)"; if(id4.equals("hr")) id4="Croatian";
		if(id4.equals("cs")) id4="Czech"; if(id4.equals("da")) id4="Danish"; if(id4.equals("nl")) id4="Dutch"; if(id4.equals("en")) id4="English"; if(id4.equals("eo")) id4="Esperanto";
		if(id4.equals("et")) id4="Estonian"; if(id4.equals("tl")) id4="Filipino"; if(id4.equals("fi")) id4="Finnish"; if(id4.equals("fr")) id4="French"; if(id4.equals("gl")) id4="Galician";
		if(id4.equals("de")) id4="German"; if(id4.equals("el")) id4="Greek"; if(id4.equals("ht")) id4="Haitian Creole"; if(id4.equals("iw")) id4="Hebrew"; if(id4.equals("hi")) id4="Hindi";
		if(id4.equals("hu")) id4="Hungarian"; if(id4.equals("is")) id4="Icelandic"; if(id4.equals("id")) id4="Indonesian"; if(id4.equals("ga")) id4="Irish"; if(id4.equals("it")) id4="Italian";
		if(id4.equals("ja")) id4="Japanese"; if(id4.equals("ko")) id4="Korean"; if(id4.equals("lv")) id4="Latvian"; if(id4.equals("lt")) id4="Lithuanian"; if(id4.equals("mk")) id4="Macedonian";
		if(id4.equals("ms")) id4="Malay"; if(id4.equals("mt")) id4="Maltese"; if(id4.equals("no")) id4="Norwegian"; if(id4.equals("fa")) id4="Persian"; if(id4.equals("pl")) id4="Polish";
		if(id4.equals("pt")) id4="Portuguese"; if(id4.equals("ro")) id4="Romanian"; if(id4.equals("ru")) id4="Russian"; if(id4.equals("sr")) id4="Serbian"; if(id4.equals("sk")) id4="Slovak";
		if(id4.equals("sl")) id4="Slovenian"; if(id4.equals("es")) id4="Spanish"; if(id4.equals("sw")) id4="Swahili"; if(id4.equals("sv")) id4="Swedish"; if(id4.equals("th")) id4="Thai";
		if(id4.equals("tr")) id4="Turkish"; if(id4.equals("uk")) id4="Ukrainian"; if(id4.equals("vi")) id4="Vietnamese"; if(id4.equals("cy")) id4="Welsh"; if(id4.equals("yi")) id4="Yiddish";
		if(id5.equals("af")) id5="Afrikaans"; if(id5.equals("sq")) id5="Albanian"; if(id5.equals("ar")) id5="Arabic"; if(id5.equals("be")) id5="Belarusian"; if(id5.equals("bg")) id5="Bulgarian";
		if(id5.equals("ca")) id5="Catalan"; if(id5.equals("zh-CN")) id5="Chinese (Simplified)"; if(id5.equals("zh-TW")) id5="Chinese (Traditional)"; if(id5.equals("hr")) id5="Croatian";
		if(id5.equals("cs")) id5="Czech"; if(id5.equals("da")) id5="Danish"; if(id5.equals("nl")) id5="Dutch"; if(id5.equals("en")) id5="English"; if(id5.equals("eo")) id5="Esperanto";
		if(id5.equals("et")) id5="Estonian"; if(id5.equals("tl")) id5="Filipino"; if(id5.equals("fi")) id5="Finnish"; if(id5.equals("fr")) id5="French"; if(id5.equals("gl")) id5="Galician";
		if(id5.equals("de")) id5="German"; if(id5.equals("el")) id5="Greek"; if(id5.equals("ht")) id5="Haitian Creole"; if(id5.equals("iw")) id5="Hebrew"; if(id5.equals("hi")) id5="Hindi";
		if(id5.equals("hu")) id5="Hungarian"; if(id5.equals("is")) id5="Icelandic"; if(id5.equals("id")) id5="Indonesian"; if(id5.equals("ga")) id5="Irish"; if(id5.equals("it")) id5="Italian";
		if(id5.equals("ja")) id5="Japanese"; if(id5.equals("ko")) id5="Korean"; if(id5.equals("lv")) id5="Latvian"; if(id5.equals("lt")) id5="Lithuanian"; if(id5.equals("mk")) id5="Macedonian";
		if(id5.equals("ms")) id5="Malay"; if(id5.equals("mt")) id5="Maltese"; if(id5.equals("no")) id5="Norwegian"; if(id5.equals("fa")) id5="Persian"; if(id5.equals("pl")) id5="Polish";
		if(id5.equals("pt")) id5="Portuguese"; if(id5.equals("ro")) id5="Romanian"; if(id5.equals("ru")) id5="Russian"; if(id5.equals("sr")) id5="Serbian"; if(id5.equals("sk")) id5="Slovak";
		if(id5.equals("sl")) id5="Slovenian"; if(id5.equals("es")) id5="Spanish"; if(id5.equals("sw")) id5="Swahili"; if(id5.equals("sv")) id5="Swedish"; if(id5.equals("th")) id5="Thai";
		if(id5.equals("tr")) id5="Turkish"; if(id5.equals("uk")) id5="Ukrainian"; if(id5.equals("vi")) id5="Vietnamese"; if(id5.equals("cy")) id5="Welsh"; if(id5.equals("yi")) id5="Yiddish";
	%>
			
		<strong>Translation to <%= id1 %>:</strong> <%= HtmlUtil.escape(translation1.getToText()) %><br />
		<strong>Translation to <%= id2 %>:</strong> <%= HtmlUtil.escape(translation2.getToText()) %><br />
		<strong>Translation to <%= id3 %>:</strong> <%= HtmlUtil.escape(translation3.getToText()) %><br />
		<strong>Translation to <%= id4 %>:</strong> <%= HtmlUtil.escape(translation4.getToText()) %><br />
		<strong>Translation to <%= id5 %>:</strong> <%= HtmlUtil.escape(translation5.getToText()) %><br />
	</c:if>

	<aui:fieldset>
		<aui:input cssClass="lfr-textarea-container" label="" name="text" type="textarea" value="<%= translation1.getFromText() %>" wrap="soft" />
		
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
			<option selected="" value="auto">Detect Language</option>
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
		<select name="<portlet:namespace />toId1" label="Select To Language">
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
		
		<br />Select To Language:
		<select name="<portlet:namespace />toId2" label="Select To Language">
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
		
		<br />Select To Language:
		<select name="<portlet:namespace />toId3" label="Select To Language">
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
		
		<br />Select To Language:
		<select name="<portlet:namespace />toId4" label="Select To Language">
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
		
		<br />Select To Language:
		<select name="<portlet:namespace />toId5" label="Select To Language">
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
