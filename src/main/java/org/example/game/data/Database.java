package org.example.game.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    public Connection init() throws SQLException {
        String url = "jdbc:postgresql://localhost/adultify";
        Properties props = new Properties();
        props.setProperty("user", "aandrango");
        props.setProperty("password", "lastoshka");
        props.setProperty("ssl", "false");
        Connection conn = DriverManager.getConnection(url, props);
        return conn;
    }
}
