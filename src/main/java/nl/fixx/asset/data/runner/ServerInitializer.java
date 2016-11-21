/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.runner;

import nl.fixx.asset.data.controller.ResourceController;
import nl.fixx.asset.data.domain.Resource;
import nl.fixx.asset.data.repository.ResourceRepository;
import static nl.fixx.asset.data.security.OAuth2SecurityConfig.PSW_ENCODER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class initializes on start up and checks if sysadmin is present if not
 * it creates sysadmin user for setup.
 *
 * @author adriaan
 */
@Component
public class ServerInitializer implements ApplicationRunner {

    @Autowired
    private ResourceRepository resp;

    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger LOG = LoggerFactory.getLogger(ServerInitializer.class);

    public ServerInitializer() {
        this.passwordEncoder = PSW_ENCODER;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        try {
            Resource resource = new Resource();
            resource.setFirstName("Fixxit");
            resource.setSurname("");
            resource.setEmail("info@fixx.it");
            resource.setSystemUser(true);
            resource.setUserName(ResourceController.ADMIN_NAME);
            resource.setPassword(passwordEncoder.encode("fix!2"));

            Resource indb = resp.findByUserName(ResourceController.ADMIN_NAME);
            if (indb != null && indb.getId() != null) {
                LOG.info("Sysadmin exists skipping creation of user!");
            } else {
                Resource saved = resp.save(resource);
                LOG.info("inserted sysadmin [" + saved.getId() + "] user "
                        + "fixxit no sysadmin is present...");
            }
        } catch (Exception ex) {
            LOG.info("Error running system init", ex);
        }
    }
}
