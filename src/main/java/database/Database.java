package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // ============================================
    // KONFIGURASI — sesuaikan dengan setup kamu
    // ============================================
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "smi_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "zxcvbnm";   // ← isi password MySQL kamu

    private static final String PARAMS =
            "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Asia/Jakarta"
            + "&characterEncoding=UTF-8";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME + PARAMS;

    private static final String SERVER_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + PARAMS;

    private static Connection connection;

    private Database() { /* prevent instantiation */ }

    /**
     * Koneksi ke database smi_db (untuk query biasa).
     */
    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL tidak ditemukan. " +
                    "Cek dependency mysql-connector-j di pom.xml", e);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal koneksi ke database smi_db: "
                    + e.getMessage(), e);
        }
        return connection;
    }

    /**
     * Koneksi ke server (tanpa database) — dipakai initializer
     * untuk CREATE DATABASE.
     */
    public static Connection getServerConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL tidak ditemukan.", e);
        }
        return DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
    }

    /**
     * Tutup koneksi. Dipanggil saat app shutdown.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("[DB] Koneksi ditutup.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cek koneksi sehat.
     */
    public static boolean testConnection() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (Exception e) {
            System.err.println("[DB] Test koneksi gagal: " + e.getMessage());
            return false;
        }
    }

    // Getter DB name (dipakai initializer)
    public static String getDbName() {
        return DB_NAME;
    }
}
