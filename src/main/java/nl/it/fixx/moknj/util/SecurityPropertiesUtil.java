package nl.it.fixx.moknj.util;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Colin on 10/12/2016 2:15 PM
 */
public class SecurityPropertiesUtil {

    private static final String PROPERTIES_FILE = "security.properties";
    private static final Properties PROPERTIES;
    private static final Logger LOG = LoggerFactory.getLogger(SecurityPropertiesUtil.class);

    static {
        PROPERTIES = new Properties();
    }

    public static String getProperty(String property) {
        try {
            InputStream inputStream = SecurityPropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            PROPERTIES.load(inputStream);
        } catch (Exception ex) {
            LOG.error("Properties file not found: " + PROPERTIES_FILE, ex);
        }

        if (PROPERTIES != null) {
            return PROPERTIES.get(property).toString();
        } else {
            LOG.error("Could not find property: {0}", property);
        }
        return null;
    }

    private SecurityPropertiesUtil() {
    }
}
