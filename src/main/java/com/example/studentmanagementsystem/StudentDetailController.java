package com.example.studentmanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentDetailController {

    @FXML private Label lblId;
    @FXML private Label lblName;
    @FXML private Label lblYear;
    @FXML private Label lblCourse;
    @FXML private Label lblGender;
    @FXML private Label lblBirthDate;
    @FXML private Label lblStatus;

    public void setStudentData(Student student) {
        // ERROR FIX: Use getStudentId() instead of getId()
        lblId.setText(String.valueOf(student.getStudentId()));

        // Combine First and Last name
        lblName.setText(student.getFirstName() + " " + student.getLastName());

        // Set new fields
        lblYear.setText(student.getYear());
        lblCourse.setText(student.getDepartment());
        lblGender.setText(student.getGender());
        lblBirthDate.setText(student.getBirthDate());
        lblStatus.setText(student.getStatus());

        // Optional: Change status color
        if("Enrolled".equalsIgnoreCase(student.getStatus())) {
            lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }
}