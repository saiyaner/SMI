package com.smi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton connection ke database MySQL.
 * Ubah USER & PASSWORD sesuai environment kamu.
 */
public class Database {

    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "smi_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";           // sesuaikan
    private static final String PARAMS   =
            "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + PARAMS;

    private static Connection connection;

    private Database() { /* prevent instantiation */ }

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal koneksi ke database: " + e.getMessage(), e);
        }
        return connection;
    }

    /**
     * Koneksi tanpa database — dipakai initializer buat CREATE DATABASE.
     */
    public static Connection getServerConnection() throws SQLException {
        String serverUrl = "jdbc:mysql://" + HOST + ":" + PORT + "/" + PARAMS;
        return DriverManager.getConnection(serverUrl, USER, PASSWORD);
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean testConnection() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
