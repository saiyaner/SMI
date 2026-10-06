package model;

import java.math.BigDecimal;

public class TransactionDetail {
    private int id;
    private int transactionId;
    private int productId;
    private int jumlah;
    private BigDecimal hargaSatuan;
    private BigDecimal subtotal;
    private String productNama;
    private String productKode;

    public TransactionDetail() {}

    public TransactionDetail(int productId, int jumlah, BigDecimal hargaSatuan) {
        this.productId = productId;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
        this.subtotal = hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; recalc(); }
    public BigDecimal getHargaSatuan() { return hargaSatuan; }
    public void setHargaSatuan(BigDecimal hargaSatuan) { this.hargaSatuan = hargaSatuan; recalc(); }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public String getProductNama() { return productNama; }
    public void setProductNama(String productNama) { this.productNama = productNama; }
    public String getProductKode() { return productKode; }
    public void setProductKode(String productKode) { this.productKode = productKode; }

    private void recalc() {
        if (hargaSatuan != null && jumlah > 0) {
            this.subtotal = hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
        }
    }
}
