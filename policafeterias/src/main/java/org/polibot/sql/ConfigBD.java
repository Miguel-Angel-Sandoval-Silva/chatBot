package org.polibot.sql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigBD {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigBD.class.getClassLoader().getResourceAsStream("configure.properties")) {
            if (input == null) {
                System.out.println("No se puedo encontrar config.properties");
                System.exit(1);
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }

}
