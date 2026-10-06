package model;

import java.time.LocalDateTime;

public class Inventory {
    private int id;
    private int productId;
    private int stok;
    private int stokMinimum;
    private LocalDateTime lastUpdated;
    private String productKode;
    private String productNama;

    public Inventory() {}

    public Inventory(int productId, int stok, int stokMinimum) {
        this.productId = productId;
        this.stok = stok;
        this.stokMinimum = stokMinimum;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public int getStokMinimum() { return stokMinimum; }
    public void setStokMinimum(int stokMinimum) { this.stokMinimum = stokMinimum; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public String getProductKode() { return productKode; }
    public void setProductKode(String productKode) { this.productKode = productKode; }
    public String getProductNama() { return productNama; }
    public void setProductNama(String productNama) { this.productNama = productNama; }

    public boolean isStokKritis() { return stok <= stokMinimum; }
}
