package nl.it.fixx.moknj.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.ArrayList;
import java.util.List;
import nl.it.fixx.moknj.properties.ApplicationProperties;
import nl.it.fixx.moknj.properties.DatabaseProperties;
import static nl.it.fixx.moknj.properties.DatabaseProperties.OpenShiftEnv.OPENSHIFT_MONGODB_DB_HOST;
import static nl.it.fixx.moknj.properties.DatabaseProperties.OpenShiftEnv.OPENSHIFT_MONGODB_DB_PASSWORD;
import static nl.it.fixx.moknj.properties.DatabaseProperties.OpenShiftEnv.OPENSHIFT_MONGODB_DB_PORT;
import static nl.it.fixx.moknj.properties.DatabaseProperties.OpenShiftEnv.OPENSHIFT_MONGODB_DB_USERNAME;
import nl.it.fixx.moknj.repository.RepositoryPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = RepositoryPackage.class)
public class MongoConfiguration extends AbstractMongoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MongoConfiguration.class);

    @Autowired
    private ApplicationProperties porperties;

    @Override
    protected String getDatabaseName() {
        try {
            return porperties.getSystem().getDb();
        } catch (Exception ex) {
            LOG.error("Error loading database properties", ex);
        }
        return null;
    }

    @Override
    public Mongo mongo() throws Exception {
        final String port = porperties.getSystem().getPort();
        if (port == null) {
            throw new Exception("No port provided for mongo db, failed connection to db!");
        }
        final String url = porperties.getSystem().getUrl();
        if (url == null) {
            throw new Exception("No url provided for mongo db, failed connection to db!");
        }

        final String environment = porperties.getEnvironment();
        LOG.debug("Starting up on environment: " + environment);
        if (environment != null
                && environment.contains(DatabaseProperties.ENVIRONMENT_OPENSHIFT)) {
            String openIp = OPENSHIFT_MONGODB_DB_HOST.toString();
            String openPort = OPENSHIFT_MONGODB_DB_PORT.toString();
            String openUserName = OPENSHIFT_MONGODB_DB_USERNAME.toString();
            String openPassword = OPENSHIFT_MONGODB_DB_PASSWORD.toString();

            if (openUserName != null && !openUserName.trim().isEmpty()
                    && openPassword != null && !openPassword.trim().isEmpty()) {
                ServerAddress address = new ServerAddress(openIp, Integer.valueOf(openPort));
                List<MongoCredential> credentials = new ArrayList<>();
                credentials.add(
                        MongoCredential.createCredential(
                                openUserName,
                                getDatabaseName(),
                                openPassword.toCharArray()
                        )
                );
                LOG.debug("####################################");
                LOG.debug("returning open shift db connection!");
                LOG.debug("####################################");
                return new MongoClient(address, credentials);
            }
        }

        final String userName = porperties.getSystem().getUsername();
        final String password = porperties.getSystem().getPassword();
        if (userName != null && !userName.trim().isEmpty()
                && password != null && !password.trim().isEmpty()) {
            List<ServerAddress> seeds = new ArrayList<>();
            seeds.add(new ServerAddress(url, Integer.valueOf(port)));
            List<MongoCredential> credentials = new ArrayList<>();
            credentials.add(
                    MongoCredential.createCredential(
                            userName,
                            getDatabaseName(),
                            password.toCharArray()
                    )
            );
            return new MongoClient(seeds, credentials);
        }

        return new MongoClient(url, Integer.valueOf(port));
    }

    @Override
    protected String getMappingBasePackage() {
        return "nl.it.fixx.moknj.repository";
    }

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

}
