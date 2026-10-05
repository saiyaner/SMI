package com.smi.database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Menjalankan schema.sql saat aplikasi pertama kali start.
 * Idempotent — pakai CREATE TABLE IF NOT EXISTS & INSERT IGNORE.
 */
public class DatabaseInitializer {

    private static final String SCHEMA_PATH = "/database/schema.sql";

    public static void initialize() {
        // 1. Pastikan database ada
        try (Connection serverConn = Database.getServerConnection();
             Statement st = serverConn.createStatement()) {
            st.execute("CREATE DATABASE IF NOT EXISTS smi_db "
                     + "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("[DB] Database siap.");
        } catch (Exception e) {
            throw new RuntimeException("Gagal membuat database: " + e.getMessage(), e);
        }

        // 2. Baca file schema.sql
        String sql = readSchema();
        if (sql == null || sql.isBlank()) {
            System.out.println("[DB] schema.sql kosong atau tidak ditemukan, skip.");
            return;
        }

        // 3. Eksekusi per-statement (split by ;)
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement()) {

            String[] statements = sql.split(";\\s*\\n");
            int executed = 0;
            for (String raw : statements) {
                String stmt = raw.trim();
                if (stmt.isEmpty() || stmt.startsWith("--")) continue;
                try {
                    st.execute(stmt);
                    executed++;
                } catch (Exception e) {
                    // Log warning saja, karena beberapa statement
                    // (DROP, CREATE DATABASE, USE) bisa error kalau sudah ada
                    System.err.println("[DB] Warning: " + e.getMessage());
                }
            }
            System.out.println("[DB] " + executed + " statement dijalankan.");
        } catch (Exception e) {
            throw new RuntimeException("Gagal inisialisasi schema: " + e.getMessage(), e);
        }
    }

    private static String readSchema() {
        try (InputStream is = DatabaseInitializer.class.getResourceAsStream(SCHEMA_PATH)) {
            if (is == null) return null;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
