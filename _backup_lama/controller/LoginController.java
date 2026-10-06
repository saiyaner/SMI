package controller;

import model.User;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private Stage stage;
    private Scene dashboardScene;
    private Database database;

    public LoginController(Stage stage, Scene dashboardScene, Database database) {
        this.stage = stage;
        this.dashboardScene = dashboardScene;
        this.database = database;
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username dan password wajib diisi");
            alert.showAndWait();
            return;
        }

        boolean loggedIn = database.login(username, password);
        if (loggedIn) {
            stage.setScene(dashboardScene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Login gagal: username atau password salah");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        stage.close();
    }
}