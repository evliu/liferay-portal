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
	String id1 = "en"; String idName1 = "English";
	String id2 = "none"; String idName2 = "none";
	String id3 = "none"; String idName3 = "none";
	String id4 = "none"; String idName4 = "none";
	String id5 = "none"; String idName5 = "none";

	if (translation1 == null) {
		translation1 = new Translation(PropsUtil.get(PropsKeys.TRANSLATOR_DEFAULT_LANGUAGES), StringPool.BLANK, StringPool.BLANK);
	}
	
%>

<portlet:actionURL var="portletURL" />

<aui:form accept-charset="UTF-8" action="<%= portletURL %>" method="post" name="fm">
	<c:if test="<%= Validator.isNotNull(translation1.getToText()) %>">
	<%
		//translationId into matching languageName
		
		id1 = translation1.getTranslationId().substring(translation1.getTranslationId().indexOf("_")+1).trim();
		id2 = translation2.getTranslationId().substring(translation2.getTranslationId().indexOf("_")+1).trim();
		id3 = translation3.getTranslationId().substring(translation3.getTranslationId().indexOf("_")+1).trim();
		id4 = translation4.getTranslationId().substring(translation4.getTranslationId().indexOf("_")+1).trim();
		id5 = translation5.getTranslationId().substring(translation5.getTranslationId().indexOf("_")+1).trim();
		if(id1.equals("af")) idName1="Afrikaans"; if(id1.equals("sq")) idName1="Albanian"; if(id1.equals("ar")) idName1="Arabic"; if(id1.equals("be")) idName1="Belarusian"; if(id1.equals("bg")) idName1="Bulgarian";
		if(id1.equals("ca")) idName1="Catalan"; if(id1.equals("zh-CN")) idName1="Chinese (Simplified)"; if(id1.equals("zh-TW")) idName1="Chinese (Traditional)"; if(id1.equals("hr")) idName1="Croatian";
		if(id1.equals("cs")) idName1="Czech"; if(id1.equals("da")) idName1="Danish"; if(id1.equals("nl")) idName1="Dutch"; if(id1.equals("en")) idName1="English"; if(id1.equals("eo")) idName1="Esperanto";
		if(id1.equals("et")) idName1="Estonian"; if(id1.equals("tl")) idName1="Filipino"; if(id1.equals("fi")) idName1="Finnish"; if(id1.equals("fr")) idName1="French"; if(id1.equals("gl")) idName1="Galician";
		if(id1.equals("de")) idName1="German"; if(id1.equals("el")) idName1="Greek"; if(id1.equals("ht")) idName1="Haitian Creole"; if(id1.equals("iw")) idName1="Hebrew"; if(id1.equals("hi")) idName1="Hindi";
		if(id1.equals("hu")) idName1="Hungarian"; if(id1.equals("is")) idName1="Icelandic"; if(id1.equals("id")) idName1="Indonesian"; if(id1.equals("ga")) idName1="Irish"; if(id1.equals("it")) idName1="Italian";
		if(id1.equals("ja")) idName1="Japanese"; if(id1.equals("ko")) idName1="Korean"; if(id1.equals("lv")) idName1="Latvian"; if(id1.equals("lt")) idName1="Lithuanian"; if(id1.equals("mk")) idName1="Macedonian";
		if(id1.equals("ms")) idName1="Malay"; if(id1.equals("mt")) idName1="Maltese"; if(id1.equals("no")) idName1="Norwegian"; if(id1.equals("fa")) idName1="Persian"; if(id1.equals("pl")) idName1="Polish";
		if(id1.equals("pt")) idName1="Portuguese"; if(id1.equals("ro")) idName1="Romanian"; if(id1.equals("ru")) idName1="Russian"; if(id1.equals("sr")) idName1="Serbian"; if(id1.equals("sk")) idName1="Slovak";
		if(id1.equals("sl")) idName1="Slovenian"; if(id1.equals("es")) idName1="Spanish"; if(id1.equals("sw")) idName1="Swahili"; if(id1.equals("sv")) idName1="Swedish"; if(id1.equals("th")) idName1="Thai";
		if(id1.equals("tr")) idName1="Turkish"; if(id1.equals("uk")) idName1="Ukrainian"; if(id1.equals("vi")) idName1="Vietnamese"; if(id1.equals("cy")) idName1="Welsh"; if(id1.equals("yi")) idName1="Yiddish";
		if(id2.equals("af")) idName2="Afrikaans"; if(id2.equals("sq")) idName2="Albanian"; if(id2.equals("ar")) idName2="Arabic"; if(id2.equals("be")) idName2="Belarusian"; if(id2.equals("bg")) idName2="Bulgarian";
		if(id2.equals("ca")) idName2="Catalan"; if(id2.equals("zh-CN")) idName2="Chinese (Simplified)"; if(id2.equals("zh-TW")) idName2="Chinese (Traditional)"; if(id2.equals("hr")) idName2="Croatian";
		if(id2.equals("cs")) idName2="Czech"; if(id2.equals("da")) idName2="Danish"; if(id2.equals("nl")) idName2="Dutch"; if(id2.equals("en")) idName2="English"; if(id2.equals("eo")) idName2="Esperanto";
		if(id2.equals("et")) idName2="Estonian"; if(id2.equals("tl")) idName2="Filipino"; if(id2.equals("fi")) idName2="Finnish"; if(id2.equals("fr")) idName2="French"; if(id2.equals("gl")) idName2="Galician";
		if(id2.equals("de")) idName2="German"; if(id2.equals("el")) idName2="Greek"; if(id2.equals("ht")) idName2="Haitian Creole"; if(id2.equals("iw")) idName2="Hebrew"; if(id2.equals("hi")) idName2="Hindi";
		if(id2.equals("hu")) idName2="Hungarian"; if(id2.equals("is")) idName2="Icelandic"; if(id2.equals("id")) idName2="Indonesian"; if(id2.equals("ga")) idName2="Irish"; if(id2.equals("it")) idName2="Italian";
		if(id2.equals("ja")) idName2="Japanese"; if(id2.equals("ko")) idName2="Korean"; if(id2.equals("lv")) idName2="Latvian"; if(id2.equals("lt")) idName2="Lithuanian"; if(id2.equals("mk")) idName2="Macedonian";
		if(id2.equals("ms")) idName2="Malay"; if(id2.equals("mt")) idName2="Maltese"; if(id2.equals("no")) idName2="Norwegian"; if(id2.equals("fa")) idName2="Persian"; if(id2.equals("pl")) idName2="Polish";
		if(id2.equals("pt")) idName2="Portuguese"; if(id2.equals("ro")) idName2="Romanian"; if(id2.equals("ru")) idName2="Russian"; if(id2.equals("sr")) idName2="Serbian"; if(id2.equals("sk")) idName2="Slovak";
		if(id2.equals("sl")) idName2="Slovenian"; if(id2.equals("es")) idName2="Spanish"; if(id2.equals("sw")) idName2="Swahili"; if(id2.equals("sv")) idName2="Swedish"; if(id2.equals("th")) idName2="Thai";
		if(id2.equals("tr")) idName2="Turkish"; if(id2.equals("uk")) idName2="Ukrainian"; if(id2.equals("vi")) idName2="Vietnamese"; if(id2.equals("cy")) idName2="Welsh"; if(id2.equals("yi")) idName2="Yiddish";
		if(id3.equals("af")) idName3="Afrikaans"; if(id3.equals("sq")) idName3="Albanian"; if(id3.equals("ar")) idName3="Arabic"; if(id3.equals("be")) idName3="Belarusian"; if(id3.equals("bg")) idName3="Bulgarian";
		if(id3.equals("ca")) idName3="Catalan"; if(id3.equals("zh-CN")) idName3="Chinese (Simplified)"; if(id3.equals("zh-TW")) idName3="Chinese (Traditional)"; if(id3.equals("hr")) idName3="Croatian";
		if(id3.equals("cs")) idName3="Czech"; if(id3.equals("da")) idName3="Danish"; if(id3.equals("nl")) idName3="Dutch"; if(id3.equals("en")) idName3="English"; if(id3.equals("eo")) idName3="Esperanto";
		if(id3.equals("et")) idName3="Estonian"; if(id3.equals("tl")) idName3="Filipino"; if(id3.equals("fi")) idName3="Finnish"; if(id3.equals("fr")) idName3="French"; if(id3.equals("gl")) idName3="Galician";
		if(id3.equals("de")) idName3="German"; if(id3.equals("el")) idName3="Greek"; if(id3.equals("ht")) idName3="Haitian Creole"; if(id3.equals("iw")) idName3="Hebrew"; if(id3.equals("hi")) idName3="Hindi";
		if(id3.equals("hu")) idName3="Hungarian"; if(id3.equals("is")) idName3="Icelandic"; if(id3.equals("id")) idName3="Indonesian"; if(id3.equals("ga")) idName3="Irish"; if(id3.equals("it")) idName3="Italian";
		if(id3.equals("ja")) idName3="Japanese"; if(id3.equals("ko")) idName3="Korean"; if(id3.equals("lv")) idName3="Latvian"; if(id3.equals("lt")) idName3="Lithuanian"; if(id3.equals("mk")) idName3="Macedonian";
		if(id3.equals("ms")) idName3="Malay"; if(id3.equals("mt")) idName3="Maltese"; if(id3.equals("no")) idName3="Norwegian"; if(id3.equals("fa")) idName3="Persian"; if(id3.equals("pl")) idName3="Polish";
		if(id3.equals("pt")) idName3="Portuguese"; if(id3.equals("ro")) idName3="Romanian"; if(id3.equals("ru")) idName3="Russian"; if(id3.equals("sr")) idName3="Serbian"; if(id3.equals("sk")) idName3="Slovak";
		if(id3.equals("sl")) idName3="Slovenian"; if(id3.equals("es")) idName3="Spanish"; if(id3.equals("sw")) idName3="Swahili"; if(id3.equals("sv")) idName3="Swedish"; if(id3.equals("th")) idName3="Thai";
		if(id3.equals("tr")) idName3="Turkish"; if(id3.equals("uk")) idName3="Ukrainian"; if(id3.equals("vi")) idName3="Vietnamese"; if(id3.equals("cy")) idName3="Welsh"; if(id3.equals("yi")) idName3="Yiddish";
		if(id4.equals("af")) idName4="Afrikaans"; if(id4.equals("sq")) idName4="Albanian"; if(id4.equals("ar")) idName4="Arabic"; if(id4.equals("be")) idName4="Belarusian"; if(id4.equals("bg")) idName4="Bulgarian";
		if(id4.equals("ca")) idName4="Catalan"; if(id4.equals("zh-CN")) idName4="Chinese (Simplified)"; if(id4.equals("zh-TW")) idName4="Chinese (Traditional)"; if(id4.equals("hr")) idName4="Croatian";
		if(id4.equals("cs")) idName4="Czech"; if(id4.equals("da")) idName4="Danish"; if(id4.equals("nl")) idName4="Dutch"; if(id4.equals("en")) idName4="English"; if(id4.equals("eo")) idName4="Esperanto";
		if(id4.equals("et")) idName4="Estonian"; if(id4.equals("tl")) idName4="Filipino"; if(id4.equals("fi")) idName4="Finnish"; if(id4.equals("fr")) idName4="French"; if(id4.equals("gl")) idName4="Galician";
		if(id4.equals("de")) idName4="German"; if(id4.equals("el")) idName4="Greek"; if(id4.equals("ht")) idName4="Haitian Creole"; if(id4.equals("iw")) idName4="Hebrew"; if(id4.equals("hi")) idName4="Hindi";
		if(id4.equals("hu")) idName4="Hungarian"; if(id4.equals("is")) idName4="Icelandic"; if(id4.equals("id")) idName4="Indonesian"; if(id4.equals("ga")) idName4="Irish"; if(id4.equals("it")) idName4="Italian";
		if(id4.equals("ja")) idName4="Japanese"; if(id4.equals("ko")) idName4="Korean"; if(id4.equals("lv")) idName4="Latvian"; if(id4.equals("lt")) idName4="Lithuanian"; if(id4.equals("mk")) idName4="Macedonian";
		if(id4.equals("ms")) idName4="Malay"; if(id4.equals("mt")) idName4="Maltese"; if(id4.equals("no")) idName4="Norwegian"; if(id4.equals("fa")) idName4="Persian"; if(id4.equals("pl")) idName4="Polish";
		if(id4.equals("pt")) idName4="Portuguese"; if(id4.equals("ro")) idName4="Romanian"; if(id4.equals("ru")) idName4="Russian"; if(id4.equals("sr")) idName4="Serbian"; if(id4.equals("sk")) idName4="Slovak";
		if(id4.equals("sl")) idName4="Slovenian"; if(id4.equals("es")) idName4="Spanish"; if(id4.equals("sw")) idName4="Swahili"; if(id4.equals("sv")) idName4="Swedish"; if(id4.equals("th")) idName4="Thai";
		if(id4.equals("tr")) idName4="Turkish"; if(id4.equals("uk")) idName4="Ukrainian"; if(id4.equals("vi")) idName4="Vietnamese"; if(id4.equals("cy")) idName4="Welsh"; if(id4.equals("yi")) idName4="Yiddish";
		if(id5.equals("af")) idName5="Afrikaans"; if(id5.equals("sq")) idName5="Albanian"; if(id5.equals("ar")) idName5="Arabic"; if(id5.equals("be")) idName5="Belarusian"; if(id5.equals("bg")) idName5="Bulgarian";
		if(id5.equals("ca")) idName5="Catalan"; if(id5.equals("zh-CN")) idName5="Chinese (Simplified)"; if(id5.equals("zh-TW")) idName5="Chinese (Traditional)"; if(id5.equals("hr")) idName5="Croatian";
		if(id5.equals("cs")) idName5="Czech"; if(id5.equals("da")) idName5="Danish"; if(id5.equals("nl")) idName5="Dutch"; if(id5.equals("en")) idName5="English"; if(id5.equals("eo")) idName5="Esperanto";
		if(id5.equals("et")) idName5="Estonian"; if(id5.equals("tl")) idName5="Filipino"; if(id5.equals("fi")) idName5="Finnish"; if(id5.equals("fr")) idName5="French"; if(id5.equals("gl")) idName5="Galician";
		if(id5.equals("de")) idName5="German"; if(id5.equals("el")) idName5="Greek"; if(id5.equals("ht")) idName5="Haitian Creole"; if(id5.equals("iw")) idName5="Hebrew"; if(id5.equals("hi")) idName5="Hindi";
		if(id5.equals("hu")) idName5="Hungarian"; if(id5.equals("is")) idName5="Icelandic"; if(id5.equals("id")) idName5="Indonesian"; if(id5.equals("ga")) idName5="Irish"; if(id5.equals("it")) idName5="Italian";
		if(id5.equals("ja")) idName5="Japanese"; if(id5.equals("ko")) idName5="Korean"; if(id5.equals("lv")) idName5="Latvian"; if(id5.equals("lt")) idName5="Lithuanian"; if(id5.equals("mk")) idName5="Macedonian";
		if(id5.equals("ms")) idName5="Malay"; if(id5.equals("mt")) idName5="Maltese"; if(id5.equals("no")) idName5="Norwegian"; if(id5.equals("fa")) idName5="Persian"; if(id5.equals("pl")) idName5="Polish";
		if(id5.equals("pt")) idName5="Portuguese"; if(id5.equals("ro")) idName5="Romanian"; if(id5.equals("ru")) idName5="Russian"; if(id5.equals("sr")) idName5="Serbian"; if(id5.equals("sk")) idName5="Slovak";
		if(id5.equals("sl")) idName5="Slovenian"; if(id5.equals("es")) idName5="Spanish"; if(id5.equals("sw")) idName5="Swahili"; if(id5.equals("sv")) idName5="Swedish"; if(id5.equals("th")) idName5="Thai";
		if(id5.equals("tr")) idName5="Turkish"; if(id5.equals("uk")) idName5="Ukrainian"; if(id5.equals("vi")) idName5="Vietnamese"; if(id5.equals("cy")) idName5="Welsh"; if(id5.equals("yi")) idName5="Yiddish";
	%>
		<%if(!id1.equals("none")){%><strong>Translation to <%= idName1 %>:</strong> <%= HtmlUtil.escape(translation1.getToText()) %><br /><% } %>
		<%if(!id2.equals("none")){%><strong>Translation to <%= idName2 %>:</strong> <%= HtmlUtil.escape(translation2.getToText()) %><br /><% }%>
		<%if(!id3.equals("none")){%><strong>Translation to <%= idName3 %>:</strong> <%= HtmlUtil.escape(translation3.getToText()) %><br /><% } %>
		<%if(!id4.equals("none")){%><strong>Translation to <%= idName4 %>:</strong> <%= HtmlUtil.escape(translation4.getToText()) %><br /><% } %>
		<%if(!id5.equals("none")){%><strong>Translation to <%= idName5 %>:</strong> <%= HtmlUtil.escape(translation5.getToText()) %><br /><% } %>

	</c:if>

	<aui:fieldset>
		<aui:input cssClass="lfr-textarea-container" label="" name="text" type="textarea" value="<%= translation1.getFromText() %>" wrap="soft" />
		
		Select From Language:
		<select name="<portlet:namespace />fromId" label="Select From Language">
			<option value="auto">Detect Language</option>
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
		<aui:select label="" name="toId1">
			<aui:option value="af" selected='<%= id1.equals("af") %>'>Afrikaans</aui:option>
			<aui:option value="sq" selected='<%= id1.equals("sq") %>'>Albanian</aui:option>
			<aui:option value="ar" selected='<%= id1.equals("ar") %>'>Arabic</aui:option>
			<aui:option value="be" selected='<%= id1.equals("be") %>'>Belarusian</aui:option>
			<aui:option value="bg" selected='<%= id1.equals("bg") %>'>Bulgarian</aui:option>
			<aui:option value="ca" selected='<%= id1.equals("ca") %>'>Catalan</aui:option>
			<aui:option value="zh-CN" selected='<%= id1.equals("zh-CN") %>'>Chinese (Simplified)</aui:option>
			<aui:option value="zh-TW" selected='<%= id1.equals("zh-TW") %>'>Chinese (Traditional)</aui:option>
			<aui:option value="hr" selected='<%= id1.equals("hr") %>'>Croatian</aui:option>
			<aui:option value="cs" selected='<%= id1.equals("cs") %>'>Czech</aui:option>
			<aui:option value="da" selected='<%= id1.equals("da") %>'>Danish</aui:option>
			<aui:option value="nl" selected='<%= id1.equals("nl") %>'>Dutch</aui:option>
			<aui:option selected='<%= id1.equals("en") %>' value="en">English</aui:option>
			<aui:option value="eo" selected='<%= id1.equals("eo") %>'>Esperanto</aui:option>
			<aui:option value="et" selected='<%= id1.equals("et") %>'>Estonian</aui:option>
			<aui:option value="tl" selected='<%= id1.equals("tl") %>'>Filipino</aui:option>
			<aui:option value="fi" selected='<%= id1.equals("fi") %>'>Finnish</aui:option>
			<aui:option value="fr" selected='<%= id1.equals("fr") %>'>French</aui:option>
			<aui:option value="gl" selected='<%= id1.equals("gl") %>'>Galician</aui:option>
			<aui:option value="de" selected='<%= id1.equals("de") %>'>German</aui:option>
			<aui:option value="el" selected='<%= id1.equals("el") %>'>Greek</aui:option>
			<aui:option value="ht" selected='<%= id1.equals("ht") %>'>Haitian Creole</aui:option>
			<aui:option value="iw" selected='<%= id1.equals("iw") %>'>Hebrew</aui:option>
			<aui:option value="hi" selected='<%= id1.equals("hi") %>'>Hindi</aui:option>
			<aui:option value="hu" selected='<%= id1.equals("hu") %>'>Hungarian</aui:option>
			<aui:option value="is" selected='<%= id1.equals("is") %>'>Icelandic</aui:option>
			<aui:option value="id" selected='<%= id1.equals("id") %>'>Indonesian</aui:option>
			<aui:option value="ga" selected='<%= id1.equals("ga") %>'>Irish</aui:option>
			<aui:option value="it" selected='<%= id1.equals("it") %>'>Italian</aui:option>
			<aui:option value="ja" selected='<%= id1.equals("ja") %>'>Japanese</aui:option>
			<aui:option value="ko" selected='<%= id1.equals("ko") %>'>Korean</aui:option>
			<aui:option value="lv" selected='<%= id1.equals("lv") %>'>Latvian</aui:option>
			<aui:option value="lt" selected='<%= id1.equals("lt") %>'>Lithuanian</aui:option>
			<aui:option value="mk" selected='<%= id1.equals("mk") %>'>Macedonian</aui:option>
			<aui:option value="ms" selected='<%= id1.equals("ms") %>'>Malay</aui:option>
			<aui:option value="mt" selected='<%= id1.equals("mt") %>'>Maltese</aui:option>
			<aui:option value="no" selected='<%= id1.equals("no") %>'>Norwegian</aui:option>
			<aui:option value="fa" selected='<%= id1.equals("fa") %>'>Persian</aui:option>
			<aui:option value="pl" selected='<%= id1.equals("pl") %>'>Polish</aui:option>
			<aui:option value="pt" selected='<%= id1.equals("pt") %>'>Portuguese</aui:option>
			<aui:option value="ro" selected='<%= id1.equals("ro") %>'>Romanian</aui:option>
			<aui:option value="ru" selected='<%= id1.equals("ru") %>'>Russian</aui:option>
			<aui:option value="sr" selected='<%= id1.equals("sr") %>'>Serbian</aui:option>
			<aui:option value="sk" selected='<%= id1.equals("sk") %>'>Slovak</aui:option>
			<aui:option value="sl" selected='<%= id1.equals("sl") %>'>Slovenian</aui:option>
			<aui:option value="es" selected='<%= id1.equals("es") %>'>Spanish</aui:option>
			<aui:option value="sw" selected='<%= id1.equals("sw") %>'>Swahili</aui:option>
			<aui:option value="sv" selected='<%= id1.equals("sv") %>'>Swedish</aui:option>
			<aui:option value="th" selected='<%= id1.equals("th") %>'>Thai</aui:option>
			<aui:option value="tr" selected='<%= id1.equals("tr") %>'>Turkish</aui:option>
			<aui:option value="uk" selected='<%= id1.equals("uk") %>'>Ukrainian</aui:option>
			<aui:option value="vi" selected='<%= id1.equals("vi") %>'>Vietnamese</aui:option>
			<aui:option value="cy" selected='<%= id1.equals("cy") %>'>Welsh</aui:option>
			<aui:option value="yi" selected='<%= id1.equals("yi") %>'>Yiddish</aui:option>
		</aui:select>
		
		<br />Select To Language:
		<aui:select label="" name="toId2">
			<aui:option value="none" selected='<%= id2.equals("none") %>'></aui:option>
			<aui:option value="af" selected='<%= id2.equals("af") %>'>Afrikaans</aui:option>
			<aui:option value="sq" selected='<%= id2.equals("sq") %>'>Albanian</aui:option>
			<aui:option value="ar" selected='<%= id2.equals("ar") %>'>Arabic</aui:option>
			<aui:option value="be" selected='<%= id2.equals("be") %>'>Belarusian</aui:option>
			<aui:option value="bg" selected='<%= id2.equals("bg") %>'>Bulgarian</aui:option>
			<aui:option value="ca" selected='<%= id2.equals("ca") %>'>Catalan</aui:option>
			<aui:option value="zh-CN" selected='<%= id2.equals("zh-CN") %>'>Chinese (Simplified)</aui:option>
			<aui:option value="zh-TW" selected='<%= id2.equals("zh-TW") %>'>Chinese (Traditional)</aui:option>
			<aui:option value="hr" selected='<%= id2.equals("hr") %>'>Croatian</aui:option>
			<aui:option value="cs" selected='<%= id2.equals("cs") %>'>Czech</aui:option>
			<aui:option value="da" selected='<%= id2.equals("da") %>'>Danish</aui:option>
			<aui:option value="nl" selected='<%= id2.equals("nl") %>'>Dutch</aui:option>
			<aui:option selected='<%= id2.equals("en") %>' value="en">English</aui:option>
			<aui:option value="eo" selected='<%= id2.equals("eo") %>'>Esperanto</aui:option>
			<aui:option value="et" selected='<%= id2.equals("et") %>'>Estonian</aui:option>
			<aui:option value="tl" selected='<%= id2.equals("tl") %>'>Filipino</aui:option>
			<aui:option value="fi" selected='<%= id2.equals("fi") %>'>Finnish</aui:option>
			<aui:option value="fr" selected='<%= id2.equals("fr") %>'>French</aui:option>
			<aui:option value="gl" selected='<%= id2.equals("gl") %>'>Galician</aui:option>
			<aui:option value="de" selected='<%= id2.equals("de") %>'>German</aui:option>
			<aui:option value="el" selected='<%= id2.equals("el") %>'>Greek</aui:option>
			<aui:option value="ht" selected='<%= id2.equals("ht") %>'>Haitian Creole</aui:option>
			<aui:option value="iw" selected='<%= id2.equals("iw") %>'>Hebrew</aui:option>
			<aui:option value="hi" selected='<%= id2.equals("hi") %>'>Hindi</aui:option>
			<aui:option value="hu" selected='<%= id2.equals("hu") %>'>Hungarian</aui:option>
			<aui:option value="is" selected='<%= id2.equals("is") %>'>Icelandic</aui:option>
			<aui:option value="id" selected='<%= id2.equals("id") %>'>Indonesian</aui:option>
			<aui:option value="ga" selected='<%= id2.equals("ga") %>'>Irish</aui:option>
			<aui:option value="it" selected='<%= id2.equals("it") %>'>Italian</aui:option>
			<aui:option value="ja" selected='<%= id2.equals("ja") %>'>Japanese</aui:option>
			<aui:option value="ko" selected='<%= id2.equals("ko") %>'>Korean</aui:option>
			<aui:option value="lv" selected='<%= id2.equals("lv") %>'>Latvian</aui:option>
			<aui:option value="lt" selected='<%= id2.equals("lt") %>'>Lithuanian</aui:option>
			<aui:option value="mk" selected='<%= id2.equals("mk") %>'>Macedonian</aui:option>
			<aui:option value="ms" selected='<%= id2.equals("ms") %>'>Malay</aui:option>
			<aui:option value="mt" selected='<%= id2.equals("mt") %>'>Maltese</aui:option>
			<aui:option value="no" selected='<%= id2.equals("no") %>'>Norwegian</aui:option>
			<aui:option value="fa" selected='<%= id2.equals("fa") %>'>Persian</aui:option>
			<aui:option value="pl" selected='<%= id2.equals("pl") %>'>Polish</aui:option>
			<aui:option value="pt" selected='<%= id2.equals("pt") %>'>Portuguese</aui:option>
			<aui:option value="ro" selected='<%= id2.equals("ro") %>'>Romanian</aui:option>
			<aui:option value="ru" selected='<%= id2.equals("ru") %>'>Russian</aui:option>
			<aui:option value="sr" selected='<%= id2.equals("sr") %>'>Serbian</aui:option>
			<aui:option value="sk" selected='<%= id2.equals("sk") %>'>Slovak</aui:option>
			<aui:option value="sl" selected='<%= id2.equals("sl") %>'>Slovenian</aui:option>
			<aui:option value="es" selected='<%= id2.equals("es") %>'>Spanish</aui:option>
			<aui:option value="sw" selected='<%= id2.equals("sw") %>'>Swahili</aui:option>
			<aui:option value="sv" selected='<%= id2.equals("sv") %>'>Swedish</aui:option>
			<aui:option value="th" selected='<%= id2.equals("th") %>'>Thai</aui:option>
			<aui:option value="tr" selected='<%= id2.equals("tr") %>'>Turkish</aui:option>
			<aui:option value="uk" selected='<%= id2.equals("uk") %>'>Ukrainian</aui:option>
			<aui:option value="vi" selected='<%= id2.equals("vi") %>'>Vietnamese</aui:option>
			<aui:option value="cy" selected='<%= id2.equals("cy") %>'>Welsh</aui:option>
			<aui:option value="yi" selected='<%= id2.equals("yi") %>'>Yiddish</aui:option>
		</aui:select>
		
		<br />Select To Language:
		<aui:select label="" name="toId3">
			<aui:option value="none" selected='<%= id3.equals("none") %>'></aui:option>
			<aui:option value="af" selected='<%= id3.equals("af") %>'>Afrikaans</aui:option>
			<aui:option value="sq" selected='<%= id3.equals("sq") %>'>Albanian</aui:option>
			<aui:option value="ar" selected='<%= id3.equals("ar") %>'>Arabic</aui:option>
			<aui:option value="be" selected='<%= id3.equals("be") %>'>Belarusian</aui:option>
			<aui:option value="bg" selected='<%= id3.equals("bg") %>'>Bulgarian</aui:option>
			<aui:option value="ca" selected='<%= id3.equals("ca") %>'>Catalan</aui:option>
			<aui:option value="zh-CN" selected='<%= id3.equals("zh-CN") %>'>Chinese (Simplified)</aui:option>
			<aui:option value="zh-TW" selected='<%= id3.equals("zh-TW") %>'>Chinese (Traditional)</aui:option>
			<aui:option value="hr" selected='<%= id3.equals("hr") %>'>Croatian</aui:option>
			<aui:option value="cs" selected='<%= id3.equals("cs") %>'>Czech</aui:option>
			<aui:option value="da" selected='<%= id3.equals("da") %>'>Danish</aui:option>
			<aui:option value="nl" selected='<%= id3.equals("nl") %>'>Dutch</aui:option>
			<aui:option selected='<%= id3.equals("en") %>' value="en">English</aui:option>
			<aui:option value="eo" selected='<%= id3.equals("eo") %>'>Esperanto</aui:option>
			<aui:option value="et" selected='<%= id3.equals("et") %>'>Estonian</aui:option>
			<aui:option value="tl" selected='<%= id3.equals("tl") %>'>Filipino</aui:option>
			<aui:option value="fi" selected='<%= id3.equals("fi") %>'>Finnish</aui:option>
			<aui:option value="fr" selected='<%= id3.equals("fr") %>'>French</aui:option>
			<aui:option value="gl" selected='<%= id3.equals("gl") %>'>Galician</aui:option>
			<aui:option value="de" selected='<%= id3.equals("de") %>'>German</aui:option>
			<aui:option value="el" selected='<%= id3.equals("el") %>'>Greek</aui:option>
			<aui:option value="ht" selected='<%= id3.equals("ht") %>'>Haitian Creole</aui:option>
			<aui:option value="iw" selected='<%= id3.equals("iw") %>'>Hebrew</aui:option>
			<aui:option value="hi" selected='<%= id3.equals("hi") %>'>Hindi</aui:option>
			<aui:option value="hu" selected='<%= id3.equals("hu") %>'>Hungarian</aui:option>
			<aui:option value="is" selected='<%= id3.equals("is") %>'>Icelandic</aui:option>
			<aui:option value="id" selected='<%= id3.equals("id") %>'>Indonesian</aui:option>
			<aui:option value="ga" selected='<%= id3.equals("ga") %>'>Irish</aui:option>
			<aui:option value="it" selected='<%= id3.equals("it") %>'>Italian</aui:option>
			<aui:option value="ja" selected='<%= id3.equals("ja") %>'>Japanese</aui:option>
			<aui:option value="ko" selected='<%= id3.equals("ko") %>'>Korean</aui:option>
			<aui:option value="lv" selected='<%= id3.equals("lv") %>'>Latvian</aui:option>
			<aui:option value="lt" selected='<%= id3.equals("lt") %>'>Lithuanian</aui:option>
			<aui:option value="mk" selected='<%= id3.equals("mk") %>'>Macedonian</aui:option>
			<aui:option value="ms" selected='<%= id3.equals("ms") %>'>Malay</aui:option>
			<aui:option value="mt" selected='<%= id3.equals("mt") %>'>Maltese</aui:option>
			<aui:option value="no" selected='<%= id3.equals("no") %>'>Norwegian</aui:option>
			<aui:option value="fa" selected='<%= id3.equals("fa") %>'>Persian</aui:option>
			<aui:option value="pl" selected='<%= id3.equals("pl") %>'>Polish</aui:option>
			<aui:option value="pt" selected='<%= id3	.equals("pt") %>'>Portuguese</aui:option>
			<aui:option value="ro" selected='<%= id3.equals("ro") %>'>Romanian</aui:option>
			<aui:option value="ru" selected='<%= id3.equals("ru") %>'>Russian</aui:option>
			<aui:option value="sr" selected='<%= id3.equals("sr") %>'>Serbian</aui:option>
			<aui:option value="sk" selected='<%= id3.equals("sk") %>'>Slovak</aui:option>
			<aui:option value="sl" selected='<%= id3.equals("sl") %>'>Slovenian</aui:option>
			<aui:option value="es" selected='<%= id3.equals("es") %>'>Spanish</aui:option>
			<aui:option value="sw" selected='<%= id3.equals("sw") %>'>Swahili</aui:option>
			<aui:option value="sv" selected='<%= id3.equals("sv") %>'>Swedish</aui:option>
			<aui:option value="th" selected='<%= id3.equals("th") %>'>Thai</aui:option>
			<aui:option value="tr" selected='<%= id3.equals("tr") %>'>Turkish</aui:option>
			<aui:option value="uk" selected='<%= id3.equals("uk") %>'>Ukrainian</aui:option>
			<aui:option value="vi" selected='<%= id3.equals("vi") %>'>Vietnamese</aui:option>
			<aui:option value="cy" selected='<%= id3.equals("cy") %>'>Welsh</aui:option>
			<aui:option value="yi" selected='<%= id3.equals("yi") %>'>Yiddish</aui:option>
		</aui:select>
		
		<br />Select To Language:
		<aui:select label="" name="toId4">
			<aui:option value="none" selected='<%= id4.equals("none") %>'></aui:option>
			<aui:option value="af" selected='<%= id4.equals("af") %>'>Afrikaans</aui:option>
			<aui:option value="sq" selected='<%= id4.equals("sq") %>'>Albanian</aui:option>
			<aui:option value="ar" selected='<%= id4.equals("ar") %>'>Arabic</aui:option>
			<aui:option value="be" selected='<%= id4.equals("be") %>'>Belarusian</aui:option>
			<aui:option value="bg" selected='<%= id4.equals("bg") %>'>Bulgarian</aui:option>
			<aui:option value="ca" selected='<%= id4.equals("ca") %>'>Catalan</aui:option>
			<aui:option value="zh-CN" selected='<%= id4.equals("zh-CN") %>'>Chinese (Simplified)</aui:option>
			<aui:option value="zh-TW" selected='<%= id4.equals("zh-TW") %>'>Chinese (Traditional)</aui:option>
			<aui:option value="hr" selected='<%= id4.equals("hr") %>'>Croatian</aui:option>
			<aui:option value="cs" selected='<%= id4.equals("cs") %>'>Czech</aui:option>
			<aui:option value="da" selected='<%= id4.equals("da") %>'>Danish</aui:option>
			<aui:option value="nl" selected='<%= id4.equals("nl") %>'>Dutch</aui:option>
			<aui:option selected='<%= id4.equals("en") %>' value="en">English</aui:option>
			<aui:option value="eo" selected='<%= id4.equals("eo") %>'>Esperanto</aui:option>
			<aui:option value="et" selected='<%= id4.equals("et") %>'>Estonian</aui:option>
			<aui:option value="tl" selected='<%= id4.equals("tl") %>'>Filipino</aui:option>
			<aui:option value="fi" selected='<%= id4.equals("fi") %>'>Finnish</aui:option>
			<aui:option value="fr" selected='<%= id4.equals("fr") %>'>French</aui:option>
			<aui:option value="gl" selected='<%= id4.equals("gl") %>'>Galician</aui:option>
			<aui:option value="de" selected='<%= id4.equals("de") %>'>German</aui:option>
			<aui:option value="el" selected='<%= id4.equals("el") %>'>Greek</aui:option>
			<aui:option value="ht" selected='<%= id4.equals("ht") %>'>Haitian Creole</aui:option>
			<aui:option value="iw" selected='<%= id4.equals("iw") %>'>Hebrew</aui:option>
			<aui:option value="hi" selected='<%= id4.equals("hi") %>'>Hindi</aui:option>
			<aui:option value="hu" selected='<%= id4.equals("hu") %>'>Hungarian</aui:option>
			<aui:option value="is" selected='<%= id4.equals("is") %>'>Icelandic</aui:option>
			<aui:option value="id" selected='<%= id4.equals("id") %>'>Indonesian</aui:option>
			<aui:option value="ga" selected='<%= id4.equals("ga") %>'>Irish</aui:option>
			<aui:option value="it" selected='<%= id4.equals("it") %>'>Italian</aui:option>
			<aui:option value="ja" selected='<%= id4.equals("ja") %>'>Japanese</aui:option>
			<aui:option value="ko" selected='<%= id4.equals("ko") %>'>Korean</aui:option>
			<aui:option value="lv" selected='<%= id4.equals("lv") %>'>Latvian</aui:option>
			<aui:option value="lt" selected='<%= id4.equals("lt") %>'>Lithuanian</aui:option>
			<aui:option value="mk" selected='<%= id4.equals("mk") %>'>Macedonian</aui:option>
			<aui:option value="ms" selected='<%= id4.equals("ms") %>'>Malay</aui:option>
			<aui:option value="mt" selected='<%= id4.equals("mt") %>'>Maltese</aui:option>
			<aui:option value="no" selected='<%= id4.equals("no") %>'>Norwegian</aui:option>
			<aui:option value="fa" selected='<%= id4.equals("fa") %>'>Persian</aui:option>
			<aui:option value="pl" selected='<%= id4.equals("pl") %>'>Polish</aui:option>
			<aui:option value="pt" selected='<%= id4.equals("pt") %>'>Portuguese</aui:option>
			<aui:option value="ro" selected='<%= id4.equals("ro") %>'>Romanian</aui:option>
			<aui:option value="ru" selected='<%= id4.equals("ru") %>'>Russian</aui:option>
			<aui:option value="sr" selected='<%= id4.equals("sr") %>'>Serbian</aui:option>
			<aui:option value="sk" selected='<%= id4.equals("sk") %>'>Slovak</aui:option>
			<aui:option value="sl" selected='<%= id4.equals("sl") %>'>Slovenian</aui:option>
			<aui:option value="es" selected='<%= id4.equals("es") %>'>Spanish</aui:option>
			<aui:option value="sw" selected='<%= id4.equals("sw") %>'>Swahili</aui:option>
			<aui:option value="sv" selected='<%= id4.equals("sv") %>'>Swedish</aui:option>
			<aui:option value="th" selected='<%= id4.equals("th") %>'>Thai</aui:option>
			<aui:option value="tr" selected='<%= id4.equals("tr") %>'>Turkish</aui:option>
			<aui:option value="uk" selected='<%= id4.equals("uk") %>'>Ukrainian</aui:option>
			<aui:option value="vi" selected='<%= id4.equals("vi") %>'>Vietnamese</aui:option>
			<aui:option value="cy" selected='<%= id4.equals("cy") %>'>Welsh</aui:option>
			<aui:option value="yi" selected='<%= id4.equals("yi") %>'>Yiddish</aui:option>
		</aui:select>
		
		<br />Select To Language:
		<aui:select label="" name="toId5">
			<aui:option value="none" selected='<%= id5.equals("none") %>'></aui:option>
			<aui:option value="af" selected='<%= id5.equals("af") %>'>Afrikaans</aui:option>
			<aui:option value="sq" selected='<%= id5.equals("sq") %>'>Albanian</aui:option>
			<aui:option value="ar" selected='<%= id5.equals("ar") %>'>Arabic</aui:option>
			<aui:option value="be" selected='<%= id5.equals("be") %>'>Belarusian</aui:option>
			<aui:option value="bg" selected='<%= id5.equals("bg") %>'>Bulgarian</aui:option>
			<aui:option value="ca" selected='<%= id5.equals("ca") %>'>Catalan</aui:option>
			<aui:option value="zh-CN" selected='<%= id5.equals("zh-CN") %>'>Chinese (Simplified)</aui:option>
			<aui:option value="zh-TW" selected='<%= id5.equals("zh-TW") %>'>Chinese (Traditional)</aui:option>
			<aui:option value="hr" selected='<%= id5.equals("hr") %>'>Croatian</aui:option>
			<aui:option value="cs" selected='<%= id5.equals("cs") %>'>Czech</aui:option>
			<aui:option value="da" selected='<%= id5.equals("da") %>'>Danish</aui:option>
			<aui:option value="nl" selected='<%= id5.equals("nl") %>'>Dutch</aui:option>
			<aui:option selected='<%= id5.equals("en") %>' value="en">English</aui:option>
			<aui:option value="eo" selected='<%= id5.equals("eo") %>'>Esperanto</aui:option>
			<aui:option value="et" selected='<%= id5.equals("et") %>'>Estonian</aui:option>
			<aui:option value="tl" selected='<%= id5.equals("tl") %>'>Filipino</aui:option>
			<aui:option value="fi" selected='<%= id5.equals("fi") %>'>Finnish</aui:option>
			<aui:option value="fr" selected='<%= id5.equals("fr") %>'>French</aui:option>
			<aui:option value="gl" selected='<%= id5.equals("gl") %>'>Galician</aui:option>
			<aui:option value="de" selected='<%= id5.equals("de") %>'>German</aui:option>
			<aui:option value="el" selected='<%= id5.equals("el") %>'>Greek</aui:option>
			<aui:option value="ht" selected='<%= id5.equals("ht") %>'>Haitian Creole</aui:option>
			<aui:option value="iw" selected='<%= id5.equals("iw") %>'>Hebrew</aui:option>
			<aui:option value="hi" selected='<%= id5.equals("hi") %>'>Hindi</aui:option>
			<aui:option value="hu" selected='<%= id5.equals("hu") %>'>Hungarian</aui:option>
			<aui:option value="is" selected='<%= id5.equals("is") %>'>Icelandic</aui:option>
			<aui:option value="id" selected='<%= id5.equals("id") %>'>Indonesian</aui:option>
			<aui:option value="ga" selected='<%= id5.equals("ga") %>'>Irish</aui:option>
			<aui:option value="it" selected='<%= id5.equals("it") %>'>Italian</aui:option>
			<aui:option value="ja" selected='<%= id5.equals("ja") %>'>Japanese</aui:option>
			<aui:option value="ko" selected='<%= id5.equals("ko") %>'>Korean</aui:option>
			<aui:option value="lv" selected='<%= id5.equals("lv") %>'>Latvian</aui:option>
			<aui:option value="lt" selected='<%= id5.equals("lt") %>'>Lithuanian</aui:option>
			<aui:option value="mk" selected='<%= id5.equals("mk") %>'>Macedonian</aui:option>
			<aui:option value="ms" selected='<%= id5.equals("ms") %>'>Malay</aui:option>
			<aui:option value="mt" selected='<%= id5.equals("mt") %>'>Maltese</aui:option>
			<aui:option value="no" selected='<%= id5.equals("no") %>'>Norwegian</aui:option>
			<aui:option value="fa" selected='<%= id5.equals("fa") %>'>Persian</aui:option>
			<aui:option value="pl" selected='<%= id5.equals("pl") %>'>Polish</aui:option>
			<aui:option value="pt" selected='<%= id5.equals("pt") %>'>Portuguese</aui:option>
			<aui:option value="ro" selected='<%= id5.equals("ro") %>'>Romanian</aui:option>
			<aui:option value="ru" selected='<%= id5.equals("ru") %>'>Russian</aui:option>
			<aui:option value="sr" selected='<%= id5.equals("sr") %>'>Serbian</aui:option>
			<aui:option value="sk" selected='<%= id5.equals("sk") %>'>Slovak</aui:option>
			<aui:option value="sl" selected='<%= id5.equals("sl") %>'>Slovenian</aui:option>
			<aui:option value="es" selected='<%= id5.equals("es") %>'>Spanish</aui:option>
			<aui:option value="sw" selected='<%= id5.equals("sw") %>'>Swahili</aui:option>
			<aui:option value="sv" selected='<%= id5.equals("sv") %>'>Swedish</aui:option>
			<aui:option value="th" selected='<%= id5.equals("th") %>'>Thai</aui:option>
			<aui:option value="tr" selected='<%= id5.equals("tr") %>'>Turkish</aui:option>
			<aui:option value="uk" selected='<%= id5.equals("uk") %>'>Ukrainian</aui:option>
			<aui:option value="vi" selected='<%= id5.equals("vi") %>'>Vietnamese</aui:option>
			<aui:option value="cy" selected='<%= id5.equals("cy") %>'>Welsh</aui:option>
			<aui:option value="yi" selected='<%= id5.equals("yi") %>'>Yiddish</aui:option>
		</aui:select>
		
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
