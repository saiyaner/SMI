package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Product {
    private int id;
    private String kode;
    private String nama;
    private int categoryId;
    private Integer supplierId;
    private BigDecimal hargaBeli;
    private BigDecimal hargaJual;
    private LocalDate tanggalKadaluarsa;
    private String satuan;
    private String merek;
    private Integer garansiBulan;
    private LocalDateTime createdAt;
    private String categoryNama;
    private String supplierNama;

    public Product() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
    public BigDecimal getHargaBeli() { return hargaBeli; }
    public void setHargaBeli(BigDecimal hargaBeli) { this.hargaBeli = hargaBeli; }
    public BigDecimal getHargaJual() { return hargaJual; }
    public void setHargaJual(BigDecimal hargaJual) { this.hargaJual = hargaJual; }
    public LocalDate getTanggalKadaluarsa() { return tanggalKadaluarsa; }
    public void setTanggalKadaluarsa(LocalDate tanggalKadaluarsa) { this.tanggalKadaluarsa = tanggalKadaluarsa; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public String getMerek() { return merek; }
    public void setMerek(String merek) { this.merek = merek; }
    public Integer getGaransiBulan() { return garansiBulan; }
    public void setGaransiBulan(Integer garansiBulan) { this.garansiBulan = garansiBulan; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getCategoryNama() { return categoryNama; }
    public void setCategoryNama(String categoryNama) { this.categoryNama = categoryNama; }
    public String getSupplierNama() { return supplierNama; }
    public void setSupplierNama(String supplierNama) { this.supplierNama = supplierNama; }

    @Override
    public String toString() { return kode + " - " + nama; }
}
