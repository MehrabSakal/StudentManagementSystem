package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RequestEditController {

    @FXML private ComboBox<String> fieldBox;
    @FXML private TextField newValueField;
    @FXML private Label statusLabel;

    private int studentId;

    public void setStudentId(int id) {
        this.studentId = id;
    }

    @FXML
    public void initialize() {
        // Students can only request to change these fields
        fieldBox.setItems(FXCollections.observableArrayList("First Name", "Last Name", "Gender", "Birth Date"));
    }

    @FXML
    public void sendRequest(ActionEvent event) {
        if (fieldBox.getValue() == null || newValueField.getText().isEmpty()) {
            statusLabel.setText("Please select a field and enter value.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String sql = "INSERT INTO edit_requests(student_id, field_to_change, new_value) VALUES(?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, fieldBox.getValue());
            pstmt.setString(3, newValueField.getText());
            pstmt.executeUpdate();

            statusLabel.setText("Request Sent! Admin will review it.");
            statusLabel.setStyle("-fx-text-fill: green;");

            newValueField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error sending request.");
        }
    }
}