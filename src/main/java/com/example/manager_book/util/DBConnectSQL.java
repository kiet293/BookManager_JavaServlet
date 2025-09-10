package com.example.manager_book.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectSQL {
    public static final String URL = "jdbc:mysql://localhost:3306/manager_book?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static final String USERNAME =  "root";
    public static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);

    }
}
