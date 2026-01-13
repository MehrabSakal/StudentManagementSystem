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

    @FXML
    private Label lblTotalStudents;
    @FXML
    private Label lblEnrolled;
    @FXML
    private Label lblRequests;
    @FXML
    private Label lblMale;
    @FXML
    private Label lblFemale;

    @FXML
    public void initialize() {
        loadDashboardStatistics();
    }

    private void loadDashboardStatistics() {
        String sqlTotal = "SELECT COUNT(*) FROM students";
        String sqlEnrolled = "SELECT COUNT(*) FROM students WHERE status = 'Enrolled'";
        String sqlRequests = "SELECT COUNT(*) FROM edit_requests WHERE status = 'Pending'";
        String sqlMale = "SELECT COUNT(*) FROM students WHERE gender = 'Male'";
        String sqlFemale = "SELECT COUNT(*) FROM students WHERE gender = 'Female'";

        try (java.sql.Connection conn = DatabaseConnection.connect();
                java.sql.Statement stmt = conn.createStatement()) {

            // Total Students
            try (java.sql.ResultSet rs = stmt.executeQuery(sqlTotal)) {
                if (rs.next()) {
                    lblTotalStudents.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Enrolled Students
            try (java.sql.ResultSet rs = stmt.executeQuery(sqlEnrolled)) {
                if (rs.next()) {
                    lblEnrolled.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Pending Requests
            try (java.sql.ResultSet rs = stmt.executeQuery(sqlRequests)) {
                if (rs.next()) {
                    lblRequests.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Male Students
            try (java.sql.ResultSet rs = stmt.executeQuery(sqlMale)) {
                if (rs.next()) {
                    lblMale.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Female Students
            try (java.sql.ResultSet rs = stmt.executeQuery(sqlFemale)) {
                if (rs.next()) {
                    lblFemale.setText(String.valueOf(rs.getInt(1)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // NEW METHOD: This receives the username
    public void setAdminName(String username) {
        // Capitalize first letter (e.g., "admin" -> "Admin")
        String formattedName = username.substring(0, 1).toUpperCase() + username.substring(1);
        welcomeLabel.setText("Welcome, " + formattedName + "!");

        // Refresh stats on login as well
        loadDashboardStatistics();
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    @FXML
    public void showResults(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("ResultDashboard.fxml"));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}