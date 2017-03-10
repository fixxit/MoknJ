package nl.it.fixx.moknj.runner;

import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.domain.core.user.User;
import nl.it.fixx.moknj.properties.AdminProperties;
import nl.it.fixx.moknj.repository.UserRepository;
import static nl.it.fixx.moknj.security.OAuth2SecurityConfig.PSW_ENCODER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class initializes on start up and checks if username is present if not
 * it creates username user for setup.
 *
 * @author adriaan
 */
@Component
public class ServerInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository resp;

    @Autowired
    private AdminProperties properties;

    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger LOG = LoggerFactory.getLogger(ServerInitializer.class);

    public ServerInitializer() {
        this.passwordEncoder = PSW_ENCODER;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        try {
            User resource = new User();
            resource.setFirstName("Fixxit");
            resource.setSurname("");
            resource.setEmail("info@fixx.it");
            resource.setSystemUser(true);

            LOG.info("properties.username : " + properties.username);
            LOG.info("properties.username : " + properties.password);

            resource.setUserName(properties.username);
            resource.setPassword(passwordEncoder.encode(properties.password));
            List<String> auths = new ArrayList<>();
            auths.add("Administrator rights");
            resource.setAuthorities(auths);

            User indb = resp.findByUserName(properties.username);
            if (indb != null && indb.getId() != null) {
                resource.setId(indb.getId());
            }

            User saved = resp.save(resource);
            LOG.info("Setting sysadmin [" + saved.getId() + "] user "
                    + "fixxit no sysadmin is present...");
        } catch (RuntimeException ex) {
            LOG.info("Error running system init", ex);
            throw ex;
        }
    }
}
