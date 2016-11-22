/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.security;

import java.util.HashSet;
import java.util.Set;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.domain.ResourceAuthority;
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
        if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
            user.getAuthorities().stream().forEach((auth) -> {
                ResourceAuthority resAuth = ResourceAuthority.authority(auth);
                if (!ResourceAuthority.ALL_ACCESS.equals(resAuth)) {
                    if (resAuth != null) {
                        authorities.add(new SimpleGrantedAuthority(resAuth.name()));
                    }
                } else {
                    LOG.info("adding full admin rights!");
                    for (ResourceAuthority auths : ResourceAuthority.values()) {
                        if (!authorities.contains(auths)) {
                            authorities.add(new SimpleGrantedAuthority(auths.name()));
                        }
                    }
                }
            });
        }
        LOG.info("authorities " + authorities);
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Resource user = repository.findByUserName(username);
            if (user == null || !user.isSystemUser()) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
