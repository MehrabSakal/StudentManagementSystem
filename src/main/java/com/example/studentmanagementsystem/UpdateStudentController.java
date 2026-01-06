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

public class UpdateStudentController {

    @FXML private TextField studentIdField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private ComboBox<String> yearBox;
    @FXML private ComboBox<String> departmentBox; // RENAMED
    @FXML private ComboBox<String> genderBox;
    @FXML private ComboBox<String> statusBox;
    @FXML private DatePicker birthDatePicker;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        yearBox.setItems(FXCollections.observableArrayList("First Year", "Second Year", "Third Year", "Fourth Year"));
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        statusBox.setItems(FXCollections.observableArrayList("Enrolled", "Not Enrolled", "Graduated"));

        populateDepartmentComboBox();
    }

    private void populateDepartmentComboBox() {
        ObservableList<String> depts = FXCollections.observableArrayList();
        // UPDATED SQL: SELECT FROM departments table
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

    // Called by the List Controller to fill the form
    public void setStudentData(Student student) {
        studentIdField.setText(String.valueOf(student.getStudentId()));
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());

        yearBox.setValue(student.getYear());
        departmentBox.setValue(student.getDepartment()); // UPDATED getter
        genderBox.setValue(student.getGender());
        statusBox.setValue(student.getStatus());

        if (student.getBirthDate() != null && !student.getBirthDate().isEmpty()) {
            birthDatePicker.setValue(LocalDate.parse(student.getBirthDate()));
        }
    }

    @FXML
    public void updateStudent(ActionEvent event) {
        // UPDATED SQL: Update 'department' column instead of 'course'
        String sql = "UPDATE students SET first_name = ?, last_name = ?, year = ?, department = ?, gender = ?, status = ?, birth_date = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstNameField.getText());
            pstmt.setString(2, lastNameField.getText());
            pstmt.setString(3, yearBox.getValue());
            pstmt.setString(4, departmentBox.getValue()); // Get value from new box
            pstmt.setString(5, genderBox.getValue());
            pstmt.setString(6, statusBox.getValue());
            pstmt.setString(7, birthDatePicker.getValue().toString());

            pstmt.setInt(8, Integer.parseInt(studentIdField.getText()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                messageLabel.setText("Update Successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("Error: ID not found.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error updating data: " + e.getMessage());
        }
    }
}