/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.AbstractOAuth2SecurityExceptionHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import nl.fixx.asset.data.util.SecurityPropertiesManager;

/**
 *
 * @author adriaan
 */
public class CustomAuthenticationEntryPoint extends AbstractOAuth2SecurityExceptionHandler implements AuthenticationEntryPoint {

    private String typeName = OAuth2AccessToken.BEARER_TYPE;

    private String realmName = SecurityPropertiesManager.getProperty("security.realm");

    public void setRealmName(String realmName) {
	this.realmName = realmName;
    }

    public void setTypeName(String typeName) {
	this.typeName = typeName;
    }

    public static String getFullURL(HttpServletRequest request) {
	StringBuffer requestURL = request.getRequestURL();
	String queryString = request.getQueryString();

	if (queryString == null) {
	    return requestURL.toString();
	} else {
	    return requestURL.append('?').append(queryString).toString();
	}
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
	String uri = getFullURL(request);
	System.out.println("redirect:" + uri);
	response.addHeader("Access-Control-Allow-Origin", "*");
	doHandle(request, response, authException);
    }

    @Override
    protected ResponseEntity<OAuth2Exception> enhanceResponse(ResponseEntity<OAuth2Exception> response, Exception exception) {
	HttpHeaders headers = response.getHeaders();
	String existing = null;
	if (headers.containsKey("WWW-Authenticate")) {
	    existing = extractTypePrefix(headers.getFirst("WWW-Authenticate"));
	}
	StringBuilder builder = new StringBuilder();
	builder.append(typeName + " ");
	builder.append("realm=\"" + realmName + "\"");
	if (existing != null) {
	    builder.append(", " + existing);
	}
	HttpHeaders update = new HttpHeaders();
	update.putAll(response.getHeaders());

	update.set("WWW-Authenticate", builder.toString());

	System.out.println("ENHANCING RESPONSE!!!");
	System.out.println(response.getBody());
	return new ResponseEntity<OAuth2Exception>(response.getBody(), update, response.getStatusCode());
    }

    private String extractTypePrefix(String header) {
	String existing = header;
	String[] tokens = existing.split(" +");
	if (tokens.length > 1 && !tokens[0].endsWith(",")) {
	    existing = StringUtils.arrayToDelimitedString(tokens, " ").substring(existing.indexOf(" ") + 1);
	}
	return existing;
    }

}
