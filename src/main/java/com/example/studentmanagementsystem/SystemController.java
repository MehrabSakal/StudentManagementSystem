package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SystemController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void loginAdmin(ActionEvent event) throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        // SIMPLE VALIDATION
        if(user.equals("Mehrab Hossen Sakal") && pass.equals("sakal123")) {

            // --- MODIFIED SECTION START ---

            // 1. Create a loader instance (instead of using the static load method)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));

            // 2. Load the view
            Parent root = loader.load();

            // 3. Get the controller associated with the Dashboard.fxml
            DashboardController dashboardController = loader.getController();

            // 4. Pass the username to the dashboard
            dashboardController.setAdminName(user);

            // --- MODIFIED SECTION END ---

            // 5. Show the new scene
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Admin Dashboard");
            stage.setScene(scene);
            stage.show();

        } else {
            errorLabel.setText("Invalid Credentials!");
        }
    }

    @FXML
    public void goBackToRoleSelection(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RoleSelection.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}