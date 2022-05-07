package ru.llm.pivoadm.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtil {
    private static final String PASSWORD_KEY = "db.password";

    private static final String USERNAME_KEY = "db.username";

    private static final String URL_KEY = "db.url";

    private Connection connection;

    public ConnectionUtil() {
        loadDriver();
    }

    public  Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseMetaData getDataBaseMetaData() throws SQLException {
        if (connection == null) {
            connection = getConnection();
        }
        return connection.getMetaData();
    }

    private  void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
