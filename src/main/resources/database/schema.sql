-- =============================================
-- SMI - Sistem Manajemen Inventori
-- Schema Database (MySQL 8 / MariaDB 10.6+)
-- =============================================

-- Database & USE di-handle oleh DatabaseInitializer
-- jadi file ini fokus ke tabel + seed saja.

-- ---------------------------------------------
-- 1. USERS
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    nama_lengkap    VARCHAR(100) NOT NULL,
    role            ENUM('ADMIN','KASIR','GUDANG') NOT NULL DEFAULT 'KASIR',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 2. CATEGORIES (user bisa nambah, ada unique constraint)
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nama        VARCHAR(50) NOT NULL,
    deskripsi   VARCHAR(255),
    created_by  INT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_categories_nama UNIQUE (nama),
    CONSTRAINT fk_cat_creator FOREIGN KEY (created_by)
        REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 3. SUPPLIERS
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS suppliers (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nama         VARCHAR(100) NOT NULL,
    alamat       VARCHAR(255),
    telepon      VARCHAR(20),
    email        VARCHAR(100),
    tipe_barang  ENUM('MAKANAN','ELEKTRONIK','LAINNYA') NOT NULL DEFAULT 'LAINNYA'
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 4. PRODUCTS
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    kode                VARCHAR(30) NOT NULL UNIQUE,
    nama                VARCHAR(100) NOT NULL,
    category_id         INT NOT NULL,
    supplier_id         INT NULL,
    harga_beli          DECIMAL(15,2) NOT NULL DEFAULT 0,
    harga_jual          DECIMAL(15,2) NOT NULL DEFAULT 0,
    tanggal_kadaluarsa  DATE NULL,
    satuan              VARCHAR(20) NULL,
    merek               VARCHAR(50) NULL,
    garansi_bulan       INT NULL,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id)
        REFERENCES categories(id) ON DELETE RESTRICT,
    CONSTRAINT fk_product_supplier FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 5. INVENTORY
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS inventory (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    product_id      INT NOT NULL UNIQUE,
    stok            INT NOT NULL DEFAULT 0,
    stok_minimum    INT NOT NULL DEFAULT 5,
    last_updated    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                    ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 6. STOCK_TRANSACTIONS
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS stock_transactions (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    product_id  INT NOT NULL,
    user_id     INT NOT NULL,
    tipe        ENUM('IN','OUT') NOT NULL,
    jumlah      INT NOT NULL,
    keterangan  VARCHAR(255),
    tanggal     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_product FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE RESTRICT,
    CONSTRAINT fk_stock_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT chk_stock_jumlah CHECK (jumlah > 0)
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 7. TRANSACTIONS
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS transactions (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    kode_transaksi  VARCHAR(30) NOT NULL UNIQUE,
    user_id         INT NOT NULL,
    total           DECIMAL(15,2) NOT NULL DEFAULT 0,
    tanggal         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trx_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------
-- 8. TRANSACTION_DETAILS
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS transaction_details (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id  INT NOT NULL,
    product_id      INT NOT NULL,
    jumlah          INT NOT NULL,
    harga_satuan    DECIMAL(15,2) NOT NULL,
    subtotal        DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_detail_trx FOREIGN KEY (transaction_id)
        REFERENCES transactions(id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_product FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------
-- INDEX
-- ---------------------------------------------
-- CREATE INDEX idx_products_category ON products(category_id);
-- CREATE INDEX idx_products_supplier ON products(supplier_id);
-- CREATE INDEX idx_stock_tanggal     ON stock_transactions(tanggal);
-- CREATE INDEX idx_trx_tanggal       ON transactions(tanggal);

-- ---------------------------------------------
-- SEED: user default
-- Password asli: admin123 / kasir123 / gudang123
-- (hash di bawah adalah placeholder — nanti di-generate ulang
--  lewat BCryptUtil saat pertama kali login/register)
-- ---------------------------------------------
INSERT IGNORE INTO users (username, password_hash, nama_lengkap, role) VALUES
('admin',   '$2a$10$placeholderHashForAdminAccount000000000000000000000', 'Administrator', 'ADMIN'),
('kasir1',  '$2a$10$placeholderHashForKasirAccount000000000000000000000', 'Kasir Satu',    'KASIR'),
('gudang1', '$2a$10$placeholderHashForGudangAccount00000000000000000000', 'Staff Gudang',  'GUDANG');

-- ---------------------------------------------
-- SEED: kategori awal (5 dasar)
-- ---------------------------------------------
INSERT IGNORE INTO categories (nama, deskripsi) VALUES
('Makanan',      'Produk makanan dan minuman'),
('Minuman',      'Produk minuman kemasan / segar'),
('Elektronik',   'Produk elektronik dan gadget'),
('Alat Tulis',   'Alat tulis kantor dan sekolah'),
('Perlengkapan', 'Perlengkapan rumah tangga dan kebersihan');

-- ---------------------------------------------
-- SEED: supplier contoh
-- ---------------------------------------------
INSERT IGNORE INTO suppliers (nama, alamat, telepon, email, tipe_barang) VALUES
('PT Sumber Pangan', 'Jl. Merdeka No.1',    '021-1111', 'sales@sumberpangan.id', 'MAKANAN'),
('CV Elektro Jaya',  'Jl. Sudirman No.2',   '021-2222', 'info@elektrojaya.id',   'ELEKTRONIK'),
('UD Serba Ada',     'Jl. Pasar Baru No.3', '021-3333', 'ud.serbaada@gmail.com', 'LAINNYA');
