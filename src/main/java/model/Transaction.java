package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int id;
    private String kodeTransaksi;
    private int userId;
    private BigDecimal total;
    private LocalDateTime tanggal;
    private String userNama;
    private List<TransactionDetail> details = new ArrayList<>();

    public Transaction() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getKodeTransaksi() { return kodeTransaksi; }
    public void setKodeTransaksi(String kodeTransaksi) { this.kodeTransaksi = kodeTransaksi; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }
    public String getUserNama() { return userNama; }
    public void setUserNama(String userNama) { this.userNama = userNama; }
    public List<TransactionDetail> getDetails() { return details; }
    public void setDetails(List<TransactionDetail> details) { this.details = details; }
    public void addDetail(TransactionDetail d) { this.details.add(d); }
}
