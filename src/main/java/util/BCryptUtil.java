package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Helper hash password pakai BCrypt.
 *
 * - hash(plain)         → generate hash baru
 * - verify(plain, hash) → cek apakah plain cocok dengan hash
 *
 * Jalankan main() untuk generate hash manual:
 *   mvn exec:java -Dexec.mainClass="util.BCryptUtil"
 */
public class BCryptUtil {

    /** Cost factor — makin tinggi makin lambat & aman. 10 = default aman. */
    private static final int COST = 10;

    private BCryptUtil() { /* prevent instantiation */ }

    /**
     * Hash password plain text.
     */
    public static String hash(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST));
    }

    /**
     * Verifikasi password plain vs hash tersimpan.
     * Return false kalau hash null/invalid.
     */
    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, storedHash);
        } catch (Exception e) {
            // hash format invalid
            return false;
        }
    }

    /**
     * Cek apakah string adalah hash BCrypt valid (prefix $2a$ / $2b$ / $2y$).
     */
    public static boolean isBcryptHash(String s) {
        if (s == null) return false;
        return s.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }

    // ========================================
    // CLI: generate hash untuk seed
    // ========================================
    public static void main(String[] args) {
        String[] defaults = { "admin123", "kasir123", "gudang123" };

        System.out.println("=== BCrypt Hash Generator ===");
        System.out.println();

        if (args.length > 0) {
            for (String pw : args) {
                System.out.println(pw + " → " + hash(pw));
            }
        } else {
            System.out.println("Contoh password default:");
            for (String pw : defaults) {
                System.out.println("  " + pw + " → " + hash(pw));
            }
            System.out.println();
            System.out.println("Generate custom:");
            System.out.println("  mvn exec:java -Dexec.mainClass=\"util.BCryptUtil\" \\");
            System.out.println("    -Dexec.args=\"password1 password2\"");
        }
    }
}
