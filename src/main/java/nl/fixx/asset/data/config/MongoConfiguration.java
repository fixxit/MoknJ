package nl.fixx.asset.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import nl.fixx.asset.data.repository.RepositoryPackage;

@Configuration
@EnableMongoRepositories(basePackageClasses = RepositoryPackage.class)
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
	return "fixxit-assets";
    }

    @Override
    public Mongo mongo() throws Exception {
	return new MongoClient("localhost", 27017);
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
