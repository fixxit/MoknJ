package nl.fixx.asset.data.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.ArrayList;
import java.util.List;
import nl.fixx.asset.data.repository.RepositoryPackage;
import nl.fixx.asset.data.util.DatabasePopertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = RepositoryPackage.class)
public class MongoConfiguration extends AbstractMongoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MongoConfiguration.class);

    @Override
    protected String getDatabaseName() {
        return DatabasePopertiesManager.getProperty("system.db");
    }

    @Override
    public Mongo mongo() throws Exception {
        final String port = DatabasePopertiesManager.getProperty("system.port");
        if (port == null) {
            throw new Exception("No port provided for mongo db, failed connection to db!");
        }
        final String url = DatabasePopertiesManager.getProperty("system.url");
        if (url == null) {
            throw new Exception("No url provided for mongo db, failed connection to db!");
        }

        final String environment = DatabasePopertiesManager.getProperty("system.environment");
        LOG.info("environment: " + environment);

        if (environment != null && environment.contains("openshift")) {
            String openIp = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
            String openPort = System.getenv("OPENSHIFT_MONGODB_DB_PORT");
            String openUserName = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
            String openPassword = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");

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
                LOG.info("####################################");
                LOG.info("returning open shift db connection!");
                LOG.info("####################################");
                return new MongoClient(address, credentials);
            }
        }

        final String userName = DatabasePopertiesManager.getProperty("system.username");
        final String password = DatabasePopertiesManager.getProperty("system.password");
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
        return "nl.fixx.data";
    }

    @Bean
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

}
