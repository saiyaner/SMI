package controller;

import model.*;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class DashboardController {
    private Database database;
    private Inventory inventory;

    public DashboardController(Database database) {
        this.database = database;
        this.inventory = database.getInventory();
    }

    @FXML
    private Label lblTotalProduk;

    @FXML
    private Label lblTotalStok;

    @FXML
    private Label lblTotalKategori;

    @FXML
    private Label lblTotalSupplier;

    @FXML
    private Label lblLowStock;

    @FXML
    private TableView<String> tblProduk;

    @FXML
    public void initialize() {
        // OOP: Polymorphism - menampilkan produk yang berbeda tapi menggunakan method yang sama
        ObservableList<Product> products = FXCollections.observableArrayList(database.getAllProducts());
        // Convert products to strings using polymorphism
        ObservableList<String> productStrings = FXCollections.observableArrayList();
        for (Product p : products) {
            productStrings.add(p.getProductInfo());
        }
        tblProduk.setItems(productStrings);

        // Dashboard statistics
        updateStatistics();
    }

    private void updateStatistics() {
        // Encapsulation: mengambil data dari inventory
        int totalProduk = inventory.getProductCount();
        int totalStok = inventory.getTotalStock();

        lblTotalProduk.setText(String.valueOf(totalProduk));
        lblTotalStok.setText(String.valueOf(totalStok));

        // Count categories and suppliers from database
        lblTotalKategori.setText("0"); // TODO: get from DB
        lblTotalSupplier.setText("0"); // TODO: get from DB

        // Low stock products
        List<Product> lowStock = inventory.getLowStockProducts(10);
        lblLowStock.setText(String.valueOf(lowStock.size()) + " produk stok rendah");
    }

    @FXML
    public void handleStockIn(ActionEvent event) {
        // Open Stock In dialog
    }

    @FXML
    public void handleStockOut(ActionEvent event) {
        // Open Stock Out dialog
    }

    @FXML
    public void handleProducts(ActionEvent event) {
        // Open Product management
    }

    @FXML
    public void handleCategories(ActionEvent event) {
        // Open Category management
    }

    @FXML
    public void handleSuppliers(ActionEvent event) {
        // Open Supplier management
    }

    @FXML
    public void handleStockHistory(ActionEvent event) {
        // Open Stock History
    }

    @FXML
    public void handleReports(ActionEvent event) {
        // Open Reports
    }
}