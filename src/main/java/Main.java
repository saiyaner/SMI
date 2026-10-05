package com.smi;

import com.smi.database.Database;
import com.smi.database.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== SMI - Sistem Manajemen Inventori ===");

        // 1. Init database
        DatabaseInitializer.initialize();

        // 2. Tes koneksi
        if (Database.testConnection()) {
            System.out.println("[OK] Koneksi database berhasil.");
        } else {
            System.err.println("[FAIL] Koneksi database gagal!");
            return;
        }

        // 3. TODO: launch JavaFX (nanti di batch berikutnya)
        // Application.launch(LoginApp.class, args);
    }
}
