package nl.fixx.asset.data.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import nl.fixx.asset.data.repository.RepositoryPackage;
import nl.fixx.asset.data.util.DatabasePopertiesManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = RepositoryPackage.class)
public class MongoConfiguration extends AbstractMongoConfiguration {

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

        return new MongoClient(url, Integer.valueOf(port));
    }

    @Override
    protected String getMappingBasePackage() {
        return "nl.fixx.data";
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }

}
