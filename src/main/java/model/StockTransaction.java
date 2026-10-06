package model;

import java.time.LocalDateTime;

public class StockTransaction {
    public static final String TIPE_IN  = "IN";
    public static final String TIPE_OUT = "OUT";

    private int id;
    private int productId;
    private int userId;
    private String tipe;
    private int jumlah;
    private String keterangan;
    private LocalDateTime tanggal;
    private String productNama;
    private String userNama;

    public StockTransaction() {}

    public StockTransaction(int productId, int userId, String tipe, int jumlah, String keterangan) {
        this.productId = productId;
        this.userId = userId;
        this.tipe = tipe;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }
    public String getProductNama() { return productNama; }
    public void setProductNama(String productNama) { this.productNama = productNama; }
    public String getUserNama() { return userNama; }
    public void setUserNama(String userNama) { this.userNama = userNama; }

    public boolean isMasuk() { return TIPE_IN.equals(tipe); }
    public boolean isKeluar() { return TIPE_OUT.equals(tipe); }
}
