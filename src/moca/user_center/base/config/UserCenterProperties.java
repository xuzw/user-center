package moca.user_center.base.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCenterProperties {

    public static final String CONFIG_PROPERTIES_FILE_PATH = "/user-center.properties";
    private static final Logger logger = LoggerFactory.getLogger(UserCenterProperties.class);
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(UserCenterProperties.class.getResourceAsStream(CONFIG_PROPERTIES_FILE_PATH));
        } catch (Exception e) {
            logger.error("Failed To Load User Center Properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static String get(String key) {
        return getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return getProperty(key, defaultValue);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.valueOf(getProperty(key, String.valueOf(defaultValue).trim()));
    }

}
