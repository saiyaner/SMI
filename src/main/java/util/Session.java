package util;

import model.User;

/**
 * Menyimpan user yang sedang login.
 * Static holder — akses dari mana saja: Session.getUser()
 */
public class Session {

    private static User currentUser;

    private Session() { /* prevent instantiation */ }

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static int getUserId() {
        return currentUser != null ? currentUser.getId() : -1;
    }

    public static String getRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public static String getNamaLengkap() {
        return currentUser != null ? currentUser.getNamaLengkap() : "";
    }

    public static boolean isAdmin() {
        return isLoggedIn() && "ADMIN".equals(currentUser.getRole());
    }

    public static boolean isKasir() {
        return isLoggedIn() && "KASIR".equals(currentUser.getRole());
    }

    public static boolean isGudang() {
        return isLoggedIn() && "GUDANG".equals(currentUser.getRole());
    }

    public static void logout() {
        currentUser = null;
    }

    /**
     * Inisial untuk avatar (mis. "Administrator" → "A").
     */
    public static String getInitials() {
        if (currentUser == null || currentUser.getNamaLengkap() == null) return "?";
        String[] parts = currentUser.getNamaLengkap().trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
    }
}
