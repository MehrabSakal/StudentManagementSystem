package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class AddStudentController {

    @FXML private TextField studentIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private ComboBox<String> yearBox;
    @FXML private ComboBox<String> departmentBox; // This will now show your departments
    @FXML private ComboBox<String> genderBox;
    @FXML private ComboBox<String> statusBox;
    @FXML private DatePicker birthDatePicker;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        yearBox.setItems(FXCollections.observableArrayList("First Year", "Second Year", "Third Year", "Fourth Year"));
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        statusBox.setItems(FXCollections.observableArrayList("Enrolled", "Not Enrolled"));
        populateDepartments();
    }

    private void populateDepartments() {
        ObservableList<String> depts = FXCollections.observableArrayList();
        String sql = "SELECT dept_code FROM departments";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                depts.add(rs.getString("dept_code"));
            }
            departmentBox.setItems(depts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveStudent(ActionEvent event) {
        String idText = studentIdField.getText();
        String fName = firstNameField.getText();
        String lName = lastNameField.getText();
        String year = yearBox.getValue();
        String dept = departmentBox.getValue();
        String gender = genderBox.getValue();
        String status = statusBox.getValue();
        LocalDate birthDate = birthDatePicker.getValue();

        if (idText.isEmpty() || fName.isEmpty() || lName.isEmpty() || dept == null) {
            statusLabel.setText("Please fill all fields.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Check if ID exists (Optional safety check)
        if (isIdExists(idText)) {
            statusLabel.setText("Student ID already exists!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String sql = "INSERT INTO students(student_id, year, department, first_name, last_name, gender, birth_date, status) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(idText));
            pstmt.setString(2, year);
            pstmt.setString(3, dept); // Saves the selected department
            pstmt.setString(4, fName);
            pstmt.setString(5, lName);
            pstmt.setString(6, gender);
            pstmt.setString(7, birthDate.toString());
            pstmt.setString(8, status);

            pstmt.executeUpdate();

            statusLabel.setText("Student Added Successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();

        } catch (Exception e) {
            statusLabel.setText("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper to clear fields
    public void clearFields() {
        studentIdField.clear();
        firstNameField.clear();
        lastNameField.clear();
        departmentBox.getSelectionModel().clearSelection();
    }

    // Helper to check ID
    private boolean isIdExists(String id) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM students WHERE student_id = ?")) {
            pstmt.setInt(1, Integer.parseInt(id));
            return pstmt.executeQuery().next();
        } catch (Exception e) { return false; }
    }
}