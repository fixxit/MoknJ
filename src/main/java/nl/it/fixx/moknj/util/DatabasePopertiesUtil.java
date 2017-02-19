/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.it.fixx.moknj.util;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adriaan
 */
public final class DatabasePopertiesUtil {

    private static final String PROPERTIES_FILE = "system.properties";
    private static final Properties PROPERTIES;
    private static final Logger LOG = LoggerFactory.getLogger(DatabasePopertiesUtil.class);

    static {
        PROPERTIES = new Properties();
    }

    public static String getProperty(String property) {
        try {
            InputStream inputStream = SecurityPropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            PROPERTIES.load(inputStream);
        } catch (Exception ex) {
            LOG.error("Database file not found: " + PROPERTIES_FILE);
        }

        if (PROPERTIES != null) {
            return PROPERTIES.get(property).toString();
        } else {
            LOG.error("Could not find property: {0}", property);
        }
        return null;
    }

    private DatabasePopertiesUtil() {
    }
}
