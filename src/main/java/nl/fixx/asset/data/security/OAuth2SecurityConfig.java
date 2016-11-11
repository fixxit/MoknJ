/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.security;

import java.util.List;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.repository.ResourceRepository;
import nl.fixx.asset.data.util.SecurityPropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 *
 * @author Riaan Schoeman
 */
@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private ResourceRepository repository;

    // In memory auth service and token store made public so it is accessible
    // from rest classes.
    private static InMemoryTokenStore inMemoryTokenStore = new InMemoryTokenStore();
    private static InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authService;

    /**
     * Define user access to user roles here
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        authService = auth.inMemoryAuthentication();
        List<Resource> resources = repository.findAll();
        if (!resources.isEmpty()) {
            loadUserAccess(resources);
        } else {
            authService.withUser(SecurityPropertiesManager.getProperty("admin.user"))
                    .password(SecurityPropertiesManager.getProperty("admin.pass"))
                    .roles("ADMIN");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .anonymous()
                .disable()
                .authorizeRequests()
                .antMatchers("/oauth/token").permitAll();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenStore tokenStore() {
        return inMemoryTokenStore;
    }

    @Bean
    public OAuth2AccessDeniedHandler accessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

    public static void loadUserAccess(List<Resource> resources) throws Exception {
        for (Resource resource : resources) {
            if (resource.isSystemUser()) {
                authService.withUser(resource.getUserName())
                        .password(resource.getPassword())
                        .roles("ADMIN");
            }
        }
    }

    public static String getUserForToken(String token) {
        return inMemoryTokenStore.readAuthentication(token).getName();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
