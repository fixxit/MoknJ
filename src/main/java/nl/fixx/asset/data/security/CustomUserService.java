/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.security;

import java.util.HashSet;
import java.util.Set;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author adriaan
 */
public class CustomUserService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomUserService.class);

    public CustomUserService(ResourceRepository repository) {
        this.repository = repository;
    }

    private final ResourceRepository repository;

    private Set<GrantedAuthority> getAuthorities(Resource user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getRoles() != null && user.getRoles().isEmpty()) {
            user.getRoles().stream().forEach((role) -> {
                authorities.add(new SimpleGrantedAuthority(role));
            });
        } else {
            LOG.info("BAD BAD!");
            LOG.info("################################################");
            LOG.info("###This is bad setting default access rights!###");
            LOG.info("###default access rights are admin NOT COOL!!###");
            LOG.info("################################################");
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ADMIN");
            authorities.add(grantedAuthority);
        }
        LOG.info("user authorities are " + authorities.toString());
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Resource user = repository.findByUserName(username);
            if (user == null || !user.isSystemUser()) {
                LOG.info("user not found with the provided username");
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
