/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fixx.asset.data.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adriaan
 */
public class DatabasePopertiesManager {

    private static final String PROPERTIES_FILE = "system.properties";
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
            log.log(Level.WARNING, "Could not find property: {0}", property);
            return null;
        }
    }
    
}
