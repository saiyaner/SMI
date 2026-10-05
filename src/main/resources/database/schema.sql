CREATE TABLE User (
  name VARCHAR(20),
  password VARCHAR(8),
  email VARCHAR(50),
  phone VARCHAR(15),
  address VARCHAR(50),
  role as ENUM('admin', 'pegawai', 'gudang', 'supplier', 'customer')
);

CREATE TABLE Produk (
  name_produk VARCHAR(20),
  price INT,
  stock INT,
  rating INT,
  supplier SELECT name_supplier FROM supplier;,
  category as ENUM('electronik', 'pakaian', 'makanan', 'lainnya')
);


