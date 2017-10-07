<%@ tag language="java" pageEncoding="UTF-8"%>
<%
	String encodedString = "";
	final org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
	if (null != authentication) {
		final String stringToEncode = authentication.getName() + ":" + authentication.getCredentials();
		encodedString = org.apache.commons.codec.binary.Base64.encodeBase64String(stringToEncode.getBytes());
	}
%>
	<input id="auth" name="auth" value="<%= encodedString %>" type="hidden" />