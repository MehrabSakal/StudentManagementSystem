package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class RequestEditController {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtDepartment;
    @FXML
    private TextField txtYear;
    @FXML
    private TextField txtSemester;
    @FXML
    private TextField txtGender;
    @FXML
    private TextField txtBirthDate;
    @FXML
    private TextField txtFatherName;
    @FXML
    private TextField txtMotherName;
    @FXML
    private TextField txtPresentAddress;
    @FXML
    private TextField txtPermanentAddress;

    @FXML
    private Label statusLabel;

    private int studentId;
    private Map<String, String> originalData = new HashMap<>();

    public void setStudentId(int id) {
        this.studentId = id;
        loadStudentDetails();
    }

    private void loadStudentDetails() {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Load data into fields and store original values
                fillField(txtFirstName, "first_name", rs);
                fillField(txtLastName, "last_name", rs);
                fillField(txtDepartment, "department", rs);
                fillField(txtYear, "year", rs);
                fillField(txtSemester, "semester", rs);
                fillField(txtGender, "gender", rs);
                fillField(txtBirthDate, "birth_date", rs);
                fillField(txtFatherName, "father_name", rs);
                fillField(txtMotherName, "mother_name", rs);
                fillField(txtPresentAddress, "present_address", rs);
                fillField(txtPermanentAddress, "permanent_address", rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error loading details.");
        }
    }

    private void fillField(TextField field, String colName, ResultSet rs) throws Exception {
        String val = rs.getString(colName);
        if (val == null)
            val = "";
        field.setText(val);
        originalData.put(colName, val);
    }

    @FXML
    public void sendRequest(ActionEvent event) {
        int changeCount = 0;

        try (Connection conn = DatabaseConnection.connect()) {

            changeCount += checkAndSend(conn, txtFirstName, "first_name", "First Name");
            changeCount += checkAndSend(conn, txtLastName, "last_name", "Last Name");
            changeCount += checkAndSend(conn, txtDepartment, "department", "Department");
            changeCount += checkAndSend(conn, txtYear, "year", "Year Level");
            changeCount += checkAndSend(conn, txtSemester, "semester", "Semester");
            changeCount += checkAndSend(conn, txtGender, "gender", "Gender");
            changeCount += checkAndSend(conn, txtBirthDate, "birth_date", "Birth Date");
            changeCount += checkAndSend(conn, txtFatherName, "father_name", "Father's Name");
            changeCount += checkAndSend(conn, txtMotherName, "mother_name", "Mother's Name");
            changeCount += checkAndSend(conn, txtPresentAddress, "present_address", "Present Address");
            changeCount += checkAndSend(conn, txtPermanentAddress, "permanent_address", "Permanent Address");

            if (changeCount > 0) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Success! Sent " + changeCount + " update request(s).");
            } else {
                statusLabel.setStyle("-fx-text-fill: orange;");
                statusLabel.setText("No changes detected.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Error sending requests.");
        }
    }

    private int checkAndSend(Connection conn, TextField field, String dbCol, String displayField) throws Exception {
        String newVal = field.getText().trim();
        String oldVal = originalData.get(dbCol);

        // If value has changed
        if (!newVal.equals(oldVal)) {
            String sql = "INSERT INTO edit_requests(student_id, field_to_change, new_value) VALUES(?,?,?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, studentId);
                pstmt.setString(2, displayField); // Store readable name or db col? Using readable for Admin
                                                  // convenience.
                pstmt.setString(3, newVal);
                pstmt.executeUpdate();
                return 1;
            }
        }
        return 0;
    }
}