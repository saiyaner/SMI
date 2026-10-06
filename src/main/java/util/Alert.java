package util;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Helper untuk dialog JavaFX.
 * Semua method otomatis set title, header, dan icon.
 */
public class Alert {

    // ========================================
    // INFO
    // ========================================
    public static void info(String message) {
        info("Informasi", message);
    }

    public static void info(String title, String message) {
        show(AlertType.INFORMATION, title, message);
    }

    // ========================================
    // WARNING
    // ========================================
    public static void warning(String message) {
        warning("Peringatan", message);
    }

    public static void warning(String title, String message) {
        show(AlertType.WARNING, title, message);
    }

    // ========================================
    // ERROR
    // ========================================
    public static void error(String message) {
        error("Error", message);
    }

    public static void error(String title, String message) {
        show(AlertType.ERROR, title, message);
    }

    // ========================================
    // SUCCESS (pakai INFORMATION dengan judul berbeda)
    // ========================================
    public static void success(String message) {
        success("Berhasil", message);
    }

    public static void success(String title, String message) {
        show(AlertType.INFORMATION, title, message);
    }

    // ========================================
    // CONFIRMATION
    // ========================================
    public static boolean confirm(String message) {
        return confirm("Konfirmasi", message);
    }

    public static boolean confirm(String title, String message) {
        javafx.scene.control.Alert a = new javafx.scene.control.Alert(
                AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        styleDialog(a);

        Optional<ButtonType> result = a.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // ========================================
    // EXCEPTION HELPER
    // ========================================
    public static void exception(String title, Throwable ex) {
        String msg = ex.getMessage();
        if (msg == null || msg.isBlank()) {
            msg = ex.getClass().getSimpleName();
        }
        error(title, msg);
    }

    // ========================================
    // INTERNAL
    // ========================================
    private static void show(AlertType type, String title, String message) {
        javafx.scene.control.Alert a = new javafx.scene.control.Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        styleDialog(a);
        a.showAndWait();
    }

    /**
     * Tambahkan style CSS ke dialog (opsional).
     * Kalau file CSS belum ada, aman-aman saja (try-catch).
     */
    private static void styleDialog(javafx.scene.control.Alert a) {
        try {
            var css = Alert.class.getResource("/css/style.css");
            if (css != null) {
                a.getDialogPane().getStylesheets().add(css.toExternalForm());
            }
        } catch (Exception ignored) { /* silent */ }

        // Pastikan dialog tampil di depan window lain
        try {
            Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
        } catch (Exception ignored) { /* silent */ }
    }
}
