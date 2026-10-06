package util;

import java.math.BigDecimal;

/**
 * Helper validasi input dari form.
 * Semua method static — tinggal panggil Validation.isEmpty(...) dll.
 */
public class Validation {

    private Validation() { /* prevent instantiation */ }

    // ========================================
    // STRING CHECKS
    // ========================================
    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    // ========================================
    // NUMBER CHECKS
    // ========================================
    public static boolean isInteger(String s) {
        if (isEmpty(s)) return false;
        try {
            Integer.parseInt(s.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveInt(String s) {
        if (!isInteger(s)) return false;
        return Integer.parseInt(s.trim()) > 0;
    }

    public static boolean isNonNegativeInt(String s) {
        if (!isInteger(s)) return false;
        return Integer.parseInt(s.trim()) >= 0;
    }

    public static boolean isDecimal(String s) {
        if (isEmpty(s)) return false;
        try {
            new BigDecimal(s.trim().replace(",", "."));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveDecimal(String s) {
        if (!isDecimal(s)) return false;
        return new BigDecimal(s.trim().replace(",", "."))
                .compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNonNegativeDecimal(String s) {
        if (!isDecimal(s)) return false;
        return new BigDecimal(s.trim().replace(",", "."))
                .compareTo(BigDecimal.ZERO) >= 0;
    }

    // ========================================
    // SAFE PARSERS
    // ========================================
    public static int parseInt(String s, int defaultVal) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return defaultVal; }
    }

    public static BigDecimal parseDecimal(String s) {
        if (isEmpty(s)) return BigDecimal.ZERO;
        try {
            return new BigDecimal(s.trim().replace(",", "."));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    // ========================================
    // EMAIL & PHONE
    // ========================================
    public static boolean isValidEmail(String s) {
        if (isEmpty(s)) return false;
        return s.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Format nomor HP Indonesia:
     * - 08xx (10-13 digit)
     * - +628xx
     * - 628xx
     * - 021-xxxx (telepon rumah, opsional)
     */
    public static boolean isValidPhone(String s) {
        if (isEmpty(s)) return false;
        String cleaned = s.replaceAll("[\\s\\-()]", "");
        return cleaned.matches("^(\\+?62|0)[0-9]{8,13}$");
    }

    // ========================================
    // KATEGORI (proteksi tabel categories)
    // ========================================
    /**
     * Normalisasi nama kategori:
     *   - trim
     *   - collapse multiple spaces
     *   - Title Case
     *
     * "  MAKANAN   ringan " → "Makanan Ringan"
     */
    public static String normalizeCategoryName(String input) {
        if (isEmpty(input)) return "";
        String s = input.trim().replaceAll("\\s+", " ");
        StringBuilder sb = new StringBuilder();
        for (String word : s.split(" ")) {
            if (word.isEmpty()) continue;
            sb.append(Character.toUpperCase(word.charAt(0)))
              .append(word.length() > 1 ? word.substring(1).toLowerCase() : "")
              .append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Cek apakah dua nama kategori "mirip" (Levenshtein distance ≤ threshold).
     */
    public static boolean isSimilar(String a, String b, int threshold) {
        if (a == null || b == null) return false;
        return levenshtein(a.toLowerCase(), b.toLowerCase()) <= threshold;
    }

    /**
     * Levenshtein distance — jumlah edit minimum untuk ubah string a → b.
     */
    public static int levenshtein(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1),
                    dp[i-1][j-1] + cost
                );
            }
        }
        return dp[a.length()][b.length()];
    }
}
