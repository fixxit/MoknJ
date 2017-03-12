package nl.it.fixx.moknj.properties;

import nl.it.fixx.moknj.properties.enums.Admin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adriaan
 */
@Component
@PropertySource(Admin.CLASSPATH)
public class AdminProperties {

    @Value("${" + Admin.USER + "}")
    public String username;

    @Value("${" + Admin.PASSWORD + "}")
    public String password;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
