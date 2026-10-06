package database;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Menjalankan schema.sql dari resources/database/schema.sql
 * saat aplikasi pertama kali dijalankan.
 *
 * Idempotent — pakai CREATE TABLE IF NOT EXISTS & INSERT IGNORE,
 * jadi aman dijalankan berulang.
 */
public class DatabaseInitializer {

    private static final String SCHEMA_PATH = "/database/schema.sql";

    public static void initialize() {
        System.out.println("[DB] === Inisialisasi Database ===");

        // 1. Pastikan database smi_db ada
        ensureDatabaseExists();

        // 2. Baca schema.sql dari resources
        String sql = readSchema();
        if (sql == null || sql.isBlank()) {
            throw new RuntimeException(
                "schema.sql tidak ditemukan atau kosong di " + SCHEMA_PATH);
        }

        // 3. Parse & eksekusi tiap statement
        List<String> statements = splitStatements(sql);
        System.out.println("[DB] Menjalankan " + statements.size() + " statement...");

        int sukses = 0;
        int gagal  = 0;

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement()) {

            for (String stmt : statements) {
                try {
                    st.execute(stmt);
                    sukses++;
                } catch (Exception e) {
                    gagal++;
                    System.err.println("[DB] ✗ Gagal: " + e.getMessage());
                    System.err.println("[DB]   Statement: " +
                            stmt.substring(0, Math.min(80, stmt.length())) + "...");
                }
            }

            System.out.println("[DB] Selesai: " + sukses + " sukses, "
                    + gagal + " gagal.");

        } catch (Exception e) {
            throw new RuntimeException("Gagal eksekusi schema: "
                    + e.getMessage(), e);
        }

        // 4. Verify
        verifyTables();

        // 5. Seed password BCrypt asli
        seedPasswords();
    }

    /**
     * Buat database smi_db kalau belum ada.
     */
    private static void ensureDatabaseExists() {
        String dbName = Database.getDbName();
        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName
                + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        try (Connection serverConn = Database.getServerConnection();
             Statement st = serverConn.createStatement()) {
            st.execute(sql);
            System.out.println("[DB] ✓ Database '" + dbName + "' siap.");
        } catch (Exception e) {
            throw new RuntimeException(
                "Gagal buat database '" + dbName + "': " + e.getMessage(), e);
        }
    }

    /**
     * Baca file schema.sql dari classpath.
     */
    private static String readSchema() {
        try (InputStream is = DatabaseInitializer.class
                                .getResourceAsStream(SCHEMA_PATH)) {
            if (is == null) return null;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return br.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            System.err.println("[DB] Gagal baca schema.sql: " + e.getMessage());
            return null;
        }
    }

    /**
     * Split SQL file jadi per-statement.
     *
     * Logika sederhana:
     *   - Buang baris komentar (-- dan /* ... *​/)
     *   - Split berdasarkan ';' di akhir baris
     *
     * Cukup untuk schema kita yang tidak punya stored procedure / trigger.
     */
    private static List<String> splitStatements(String sql) {
        List<String> result = new ArrayList<>();

        // 1. Buang baris komentar
        StringBuilder cleaned = new StringBuilder();
        boolean inBlockComment = false;

        for (String line : sql.split("\n")) {
            String trimmed = line.trim();

            // Block comment /* ... */
            if (inBlockComment) {
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                    trimmed = trimmed.substring(trimmed.indexOf("*/") + 2).trim();
                } else {
                    continue;
                }
            }
            if (trimmed.startsWith("/*")) {
                if (!trimmed.contains("*/")) {
                    inBlockComment = true;
                }
                continue;
            }

            // Line comment --
            if (trimmed.startsWith("--")) {
                continue;
            }

            cleaned.append(line).append("\n");
        }

        // 2. Split berdasarkan ';'
        String[] rawStmts = cleaned.toString().split(";");
        for (String s : rawStmts) {
            String stmt = s.trim();
            if (!stmt.isEmpty()) {
                result.add(stmt);
            }
        }
        return result;
    }

    /**
     * Cek tabel-tabel penting sudah ada.
     */
    private static void verifyTables() {
        String[] required = {
            "users", "categories", "suppliers", "products",
            "inventory", "stock_transactions", "transactions",
            "transaction_details"
        };

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement()) {

            int found = 0;
            for (String table : required) {
                var rs = st.executeQuery(
                    "SELECT COUNT(*) AS total FROM information_schema.tables "
                    + "WHERE table_schema = '" + Database.getDbName() + "' "
                    + "AND table_name = '" + table + "'"
                );
                if (rs.next() && rs.getInt("total") > 0) {
                    found++;
                } else {
                    System.err.println("[DB] ⚠️ Tabel '" + table + "' tidak ada!");
                }
            }

            if (found == required.length) {
                System.out.println("[DB] ✓ Semua " + found + " tabel terverifikasi.");
            } else {
                System.err.println("[DB] ⚠️ Hanya " + found + "/"
                        + required.length + " tabel yang ada!");
            }
        } catch (Exception e) {
            System.err.println("[DB] Gagal verifikasi tabel: " + e.getMessage());
        }
    }

    /**
     * Update password_hash user default yang masih placeholder.
     *
     * Cek apakah hash berawalan "$2a$10$placeholder" — kalau iya,
     * ganti dengan hash BCrypt asli untuk password yang sudah diketahui.
     *
     * Idempotent — aman dijalankan berkali-kali.
     */
    private static void seedPasswords() {
        // username → plain password
        String[][] defaults = {
            { "admin",   "admin123"  },
            { "kasir1",  "kasir123"  },
            { "gudang1", "gudang123" }
        };

        String selectSql = "SELECT password_hash FROM users WHERE username = ?";
        String updateSql = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (var conn = Database.getConnection();
             var psSelect = conn.prepareStatement(selectSql);
             var psUpdate = conn.prepareStatement(updateSql)) {

            int updated = 0;

            for (String[] pair : defaults) {
                String username = pair[0];
                String plain    = pair[1];

                psSelect.setString(1, username);
                var rs = psSelect.executeQuery();
                if (!rs.next()) continue;

                String currentHash = rs.getString("password_hash");

                // Skip kalau sudah pakai hash BCrypt valid (bukan placeholder)
                if (currentHash != null
                        && !currentHash.startsWith("$2a$10$placeholder")) {
                    continue;
                }

                String newHash = util.BCryptUtil.hash(plain);
                psUpdate.setString(1, newHash);
                psUpdate.setString(2, username);
                psUpdate.executeUpdate();
                updated++;
                System.out.println("[DB] ✓ Password '" + username + "' di-seed.");
            }

            if (updated == 0) {
                System.out.println("[DB] ✓ Password user default sudah OK.");
            }

        } catch (Exception e) {
            System.err.println("[DB] ⚠️ Gagal seed password: " + e.getMessage());
        }
    }
}
