package nl.it.fixx.moknj.security;

import javax.servlet.http.HttpServletRequest;
import nl.it.fixx.moknj.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 *
 * @author Riaan Schoeman
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ApplicationProperties properties;

    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(properties.getSecurity().getResourceId())
                .stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous().disable().requestMatchers()
                .antMatchers("/asset/**", "/type/**", "/link/**",
                        "/resource/**", "/menu/**", "/graph/**")
                .and()
                .authorizeRequests()
                .antMatchers("/asset/**")
                .hasAuthority("ASSET")
                .and()
                .authorizeRequests()
                .antMatchers("/type/**")
                .hasAuthority("TYPE")
                .and()
                .authorizeRequests()
                .antMatchers("/link/**")
                .hasAuthority("LINK")
                .and()
                .authorizeRequests()
                .antMatchers("/resource/**")
                .hasAuthority("RESOURCE")
                .and()
                .authorizeRequests()
                .antMatchers("/menu/**")
                .hasAuthority("MENU")
                .and()
                .authorizeRequests()
                .antMatchers("/graph/**")
                .hasAuthority("DASH");
    }

}
