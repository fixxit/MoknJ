package nl.it.fixx.moknj.security;

import java.util.HashSet;
import java.util.Set;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.domain.core.user.UserAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import nl.it.fixx.moknj.repository.UserRepository;

/**
 *
 * @author adriaan
 */
public class CustomUserService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomUserService.class);

    public CustomUserService(UserRepository repository) {
        this.repository = repository;
    }

    private final UserRepository repository;

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getAuthorities() != null && !user.getAuthorities().isEmpty()) {
            user.getAuthorities().stream().forEach((auth) -> {
                UserAuthority resAuth = UserAuthority.authority(auth);
                if (!UserAuthority.ALL_ACCESS.equals(resAuth)) {
                    if (resAuth != null) {
                        authorities.add(new SimpleGrantedAuthority(resAuth.name()));
                    }
                } else {
                    LOG.info("adding full admin rights!");
                    for (UserAuthority auths : UserAuthority.values()) {
                        SimpleGrantedAuthority sgAuth = new SimpleGrantedAuthority(auths.name());
                        if (!authorities.contains(sgAuth)) {
                            authorities.add(sgAuth);
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
            User user = repository.findByUserName(username);
            if (user == null || !user.isSystemUser()) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthorities(user));
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
