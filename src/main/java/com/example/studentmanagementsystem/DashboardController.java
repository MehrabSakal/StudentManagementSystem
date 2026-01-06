package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Label welcomeLabel; // This links to the text on the screen

    // NEW METHOD: This receives the username
    public void setAdminName(String username) {
        // Capitalize first letter (e.g., "admin" -> "Admin")
        String formattedName = username.substring(0, 1).toUpperCase() + username.substring(1);
        welcomeLabel.setText("Welcome, " + formattedName + "!");
    }

    @FXML
    public void showAddStudent(ActionEvent event) {
        try {
            // Load the AddStudent.fxml file
            Parent view = FXMLLoader.load(getClass().getResource("AddStudent.fxml"));

            // Clear current content and add the new view
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showDepartmentData(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("DepartmentData.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RoleSelection.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void showAllStudents(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("StudentList.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showRequests(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("AdminRequests.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showGrades(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("Grades.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}