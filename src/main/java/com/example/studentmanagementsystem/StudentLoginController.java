package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentLoginController {

    @FXML private TextField studentIdField;
    @FXML private Label errorLabel;

    @FXML
    public void loginStudent(ActionEvent event) {
        String idText = studentIdField.getText();

        if (idText.isEmpty()) {
            errorLabel.setText("Please enter your Student ID.");
            return;
        }

        // Check DB for Student ID
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(idText));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Login Success: Get student details
                int dbId = rs.getInt("student_id");
                String dbName = rs.getString("first_name");

                // Load Student Dashboard
                openStudentDashboard(event, dbId, dbName);
            } else {
                errorLabel.setText("Student ID not found.");
            }

        } catch (NumberFormatException e) {
            errorLabel.setText("ID must be a number.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Database error.");
        }
    }

    private void openStudentDashboard(ActionEvent event, int studentId, String name) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDashboard.fxml"));
        Parent root = loader.load();

        // Pass ID to the dashboard so it knows whose data to show
        StudentDashboardController controller = loader.getController();
        controller.setStudentSession(studentId, name);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Student Dashboard - " + name);
        stage.show();
    }

    @FXML
    public void goBackToRoleSelection(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RoleSelection.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}