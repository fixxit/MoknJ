package nl.it.fixx.moknj.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Colin on 10/12/2016 2:15 PM
 */
public class SecurityPropertiesManager {

    private static final String PROPERTIES_FILE = "security.properties";
    private static Properties properties = new Properties();
    private static Logger log = Logger.getLogger(SecurityPropertiesManager.class.getName());

    public static String getProperty(String property) {
	try {
	    InputStream inputStream = SecurityPropertiesManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
	    properties.load(inputStream);
	} catch (Exception ex) {
	    log.warning("Properties file not found: " + PROPERTIES_FILE);
	    return null;
	}

	if (properties != null) {
	    return properties.get(property).toString();
	} else {
	    log.warning("Could not find property: " + property);
	    return null;
	}
    }
}
