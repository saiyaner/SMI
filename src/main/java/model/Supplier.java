package model;

public class Supplier {
    private int id;
    private String nama;
    private String alamat;
    private String telepon;
    private String email;
    private String tipeBarang;

    public Supplier() {}

    public Supplier(String nama, String alamat, String telepon, String email, String tipeBarang) {
        this.nama = nama;
        this.alamat = alamat;
        this.telepon = telepon;
        this.email = email;
        this.tipeBarang = tipeBarang;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getTelepon() { return telepon; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTipeBarang() { return tipeBarang; }
    public void setTipeBarang(String tipeBarang) { this.tipeBarang = tipeBarang; }

    @Override
    public String toString() { return nama; }
}
