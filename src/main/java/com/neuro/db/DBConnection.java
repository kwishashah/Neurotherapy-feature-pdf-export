package com.neuro.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DBConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {

        Properties props = new Properties();

        // 🔴 FIX 1: Handle IOException properly
        try (InputStream in = DBConnection.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (in == null) {
                throw new RuntimeException(
                        "db.properties not found in classpath (place it inside src/)"
                );
            }

            props.load(in);

        } catch (IOException e) {
            throw new RuntimeException("Error loading db.properties", e);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.username");
        String pass = props.getProperty("db.password");

        // 🔴 FIX 2: Validate config
        if (url == null || user == null) {
            throw new RuntimeException("Missing DB configuration in db.properties");
        }

        // 🔴 FIX 3: Handle driver loading safely
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }

        // 🔴 FIX 4: Avoid breaking existing query params
        if (!url.contains("useSSL")) {
            if (url.contains("?")) {
                url += "&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            } else {
                url += "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            }
        }

        return DriverManager.getConnection(url, user, pass);
    }
}