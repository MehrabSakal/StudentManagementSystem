package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDashboardController {

    @FXML private Label welcomeLabel;

    // Profile Labels
    @FXML private Label lblId;
    @FXML private Label lblName;
    @FXML private Label lblDept; // Added
    @FXML private Label lblYear;
    @FXML private Label lblGender;
    @FXML private Label lblBirthDate;
    @FXML private Label lblStatus;

    // Grade Labels (Replaces ListView)
    @FXML private Label lblFirstSem;
    @FXML private Label lblSecondSem;
    @FXML private Label lblFinal;

    private int currentStudentId;

    public void setStudentSession(int studentId, String name) {
        this.currentStudentId = studentId;
        welcomeLabel.setText("Welcome, " + name);

        loadStudentProfile();
        loadStudentGrades(); // New method
    }

    private void loadStudentProfile() {
        // Fetch 'department' instead of course
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, currentStudentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lblId.setText(String.valueOf(rs.getInt("student_id")));
                lblName.setText(rs.getString("first_name") + " " + rs.getString("last_name"));

                // Display Department
                lblDept.setText(rs.getString("department"));

                lblYear.setText(rs.getString("year"));
                lblGender.setText(rs.getString("gender"));
                lblBirthDate.setText(rs.getString("birth_date"));

                String status = rs.getString("status");
                lblStatus.setText(status);

                if ("Enrolled".equalsIgnoreCase(status)) {
                    lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                } else {
                    lblStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentGrades() {
        // Fetch grades from student_grades table
        String sql = "SELECT first_sem, second_sem, final FROM student_grades WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, currentStudentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lblFirstSem.setText(String.valueOf(rs.getDouble("first_sem")));
                lblSecondSem.setText(String.valueOf(rs.getDouble("second_sem")));
                lblFinal.setText(String.valueOf(rs.getDouble("final")));
            } else {
                // If no grades found yet
                lblFirstSem.setText("N/A");
                lblSecondSem.setText("N/A");
                lblFinal.setText("N/A");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openRequestForm(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RequestEdit.fxml"));
            Parent root = loader.load();

            RequestEditController controller = loader.getController();
            controller.setStudentId(currentStudentId);

            Stage stage = new Stage();
            stage.setTitle("Request Correction");
            stage.setScene(new Scene(root));
            stage.show();

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
}