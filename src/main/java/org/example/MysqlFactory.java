package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlFactory extends DAOFactory{
    private static final String url = "jdbc:mysql://localhost:3308/integrador_1";
    private static final String username = "root";
    private static final String password = "";
    private static Connection conn;

    public Connection getInstance() {
        if (conn == null){
            try {
                conn = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return conn;
    }
}
