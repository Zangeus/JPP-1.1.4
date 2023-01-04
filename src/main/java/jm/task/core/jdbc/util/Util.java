package jm.task.core.jdbc.util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    public static final String URL = "jdbc:mysql://localhost:3306/hibernate_master";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "12345678q";
    public static Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.INFO, "Соединение не было установлено");
            e.printStackTrace();
        }
        return connection;
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }
}
