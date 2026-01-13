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
import org.mindrot.jbcrypt.BCrypt;

public class UpdateStudentController {

    @FXML
    private TextField studentIdField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private ComboBox<String> yearBox;
    @FXML
    private ComboBox<String> departmentBox;
    @FXML
    private ComboBox<String> semesterBox; // New
    @FXML
    private ComboBox<String> genderBox;
    @FXML
    private ComboBox<String> statusBox;
    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private TextField fatherNameField; // New
    @FXML
    private TextField motherNameField; // New
    @FXML
    private TextField presentAddressField; // New
    @FXML
    private TextField permanentAddressField; // New
    @FXML
    private PasswordField passwordField; // New

    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        yearBox.setItems(FXCollections.observableArrayList("First Year", "Second Year", "Third Year", "Fourth Year"));
        semesterBox.setItems(FXCollections.observableArrayList("1st Semester", "2nd Semester"));
        genderBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        statusBox.setItems(FXCollections.observableArrayList("Enrolled", "Not Enrolled", "Graduated"));

        populateDepartmentComboBox();
    }

    private void populateDepartmentComboBox() {
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

    public void setStudentData(Student student) {
        studentIdField.setText(String.valueOf(student.getStudentId()));
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());

        yearBox.setValue(student.getYear());
        departmentBox.setValue(student.getDepartment());
        semesterBox.setValue(student.getSemester());
        genderBox.setValue(student.getGender());
        statusBox.setValue(student.getStatus());

        fatherNameField.setText(student.getFatherName());
        motherNameField.setText(student.getMotherName());
        presentAddressField.setText(student.getPresentAddress());
        permanentAddressField.setText(student.getPermanentAddress());

        if (student.getBirthDate() != null && !student.getBirthDate().isEmpty()) {
            try {
                birthDatePicker.setValue(LocalDate.parse(student.getBirthDate()));
            } catch (Exception ignored) {
            }
        }
    }

    @FXML
    public void updateStudent(ActionEvent event) {
        String password = passwordField.getText();
        boolean updatePassword = password != null && !password.isEmpty();

        StringBuilder sqlBuilder = new StringBuilder(
                "UPDATE students SET first_name = ?, last_name = ?, year = ?, department = ?, semester = ?, gender = ?, status = ?, birth_date = ?, father_name = ?, mother_name = ?, present_address = ?, permanent_address = ?");

        if (updatePassword) {
            sqlBuilder.append(", password = ?");
        }

        sqlBuilder.append(" WHERE student_id = ?");

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            pstmt.setString(1, firstNameField.getText());
            pstmt.setString(2, lastNameField.getText());
            pstmt.setString(3, yearBox.getValue());
            pstmt.setString(4, departmentBox.getValue());
            pstmt.setString(5, semesterBox.getValue());
            pstmt.setString(6, genderBox.getValue());
            pstmt.setString(7, statusBox.getValue());
            pstmt.setString(8, birthDatePicker.getValue() != null ? birthDatePicker.getValue().toString() : "");

            pstmt.setString(9, fatherNameField.getText());
            pstmt.setString(10, motherNameField.getText());
            pstmt.setString(11, presentAddressField.getText());
            pstmt.setString(12, permanentAddressField.getText());

            int paramIndex = 13;
            if (updatePassword) {
                pstmt.setString(paramIndex++, BCrypt.hashpw(password, BCrypt.gensalt()));
            }

            pstmt.setInt(paramIndex, Integer.parseInt(studentIdField.getText()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                messageLabel.setText("Update Successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
                if (updatePassword)
                    passwordField.clear();
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