package nl.it.fixx.moknj.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.it.fixx.moknj.properties.ApplicationProperties;
import static nl.it.fixx.moknj.security.CustomAuthenticationEntryPoint.getFullURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.AbstractOAuth2SecurityExceptionHandler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

/**
 *
 * @author adriaan
 */
@Configuration
public class CustomAccessDeniedEntryHandler extends AbstractOAuth2SecurityExceptionHandler implements AccessDeniedHandler {

    @Autowired
    private ApplicationProperties properties;

    private final String typeName = OAuth2AccessToken.BEARER_TYPE;

    @Override
    protected ResponseEntity<OAuth2Exception> enhanceResponse(ResponseEntity<OAuth2Exception> response, Exception exception) {
        HttpHeaders headers = response.getHeaders();
        String existing = null;
        if (headers.containsKey("WWW-Authenticate")) {
            existing = extractTypePrefix(headers.getFirst("WWW-Authenticate"));
        }
        StringBuilder builder = new StringBuilder();
        builder.append(typeName).append(" ");
        builder.append("realm=\"").append(properties.getSecurity().getRealm()).append("\"");
        if (existing != null) {
            builder.append(", ").append(existing);
        }
        HttpHeaders update = new HttpHeaders();
        update.putAll(response.getHeaders());

        update.set("WWW-Authenticate", builder.toString());
        return new ResponseEntity<>(response.getBody(), update, response.getStatusCode());
    }

    private String extractTypePrefix(String header) {
        String existing = header;
        String[] tokens = existing.split(" +");
        if (tokens.length > 1 && !tokens[0].endsWith(",")) {
            existing = StringUtils.arrayToDelimitedString(tokens, " ").substring(existing.indexOf(" ") + 1);
        }
        return existing;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String uri = getFullURL(request);
        System.out.println("access denied redirect:" + uri);
        response.addHeader("Access-Control-Allow-Origin", "*");
        doHandle(request, response, accessDeniedException);
    }

}
