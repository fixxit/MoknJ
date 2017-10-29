package nl.it.fixx.moknj.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2SecurityLoaderConfig {

    @Autowired
    private OAuth2SecurityConfig securityConfig;
}
