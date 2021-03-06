package nl.it.fixx.moknj.security;

import javax.servlet.http.HttpServletRequest;
import nl.it.fixx.moknj.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                return super.getCorsConfiguration(request);
            }
        };

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);

        return bean;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .resourceId(properties.getSecurity().getResourceId())
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
                .hasAuthority("DASH")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedEntryHandler());
    }

}
