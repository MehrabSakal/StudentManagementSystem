package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoleSelectionController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void switchToAdminLogin(ActionEvent event) throws IOException {
        // Load the Admin Login Page
        root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Admin Login");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToStudentView(ActionEvent event) throws IOException {
        // Load the new Student Login Page
        root = FXMLLoader.load(getClass().getResource("StudentLogin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Student Portal Access");
        stage.setScene(scene);
        stage.show();
    }
}