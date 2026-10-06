import database.Database;
import database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  SMI - Sistem Manajemen Inventori       ║");
        System.out.println("╚══════════════════════════════════════════╝");

        try {
            // 1. Init database
            DatabaseInitializer.initialize();

            // 2. Test koneksi
            if (Database.testConnection()) {
                System.out.println("[OK] Koneksi database berhasil.");
            } else {
                System.err.println("[FAIL] Koneksi database gagal!");
                return;
            }

            System.out.println("[OK] Siap lanjut ke JavaFX.");
            // Application.launch(...) — nanti di batch view

        } catch (Exception e) {
            System.err.println("╔══════════════════════════════════════════╗");
            System.err.println("║  ERROR: " + e.getMessage());
            System.err.println("╚══════════════════════════════════════════╝");
            e.printStackTrace();
        } finally {
            Database.close();
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
    }
}
