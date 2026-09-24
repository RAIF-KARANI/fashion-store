package com.fashionstore.util;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("Testing DB Connection...");
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("SUCCESS: Connected to the database!");
            DBConnection.closeConnection(conn);
        } else {
            System.out.println("FAILURE: Could not connect to the database.");
        }
    }
}
