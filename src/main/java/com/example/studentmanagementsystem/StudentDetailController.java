package com.example.studentmanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDetailController {

    @FXML
    private Label lblId;
    @FXML
    private Label lblName;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblCourse;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblBirthDate;
    @FXML
    private Label lblStatus;

    @FXML
    private Label lblFirstSem;
    @FXML
    private Label lblSecondSem;
    @FXML
    private Label lblFinal;

    // New Fields
    @FXML
    private Label lblFather;
    @FXML
    private Label lblMother;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblSemester;
    @FXML
    private javafx.scene.image.ImageView studentImage;

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
        if ("Enrolled".equalsIgnoreCase(student.getStatus())) {
            lblStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }

        lblFather.setText(student.getFatherName() == null ? "-" : student.getFatherName());
        lblMother.setText(student.getMotherName() == null ? "-" : student.getMotherName());
        lblAddress.setText((student.getPresentAddress() == null ? "" : student.getPresentAddress()) + " / "
                + (student.getPermanentAddress() == null ? "" : student.getPermanentAddress()));
        lblSemester.setText(student.getSemester() == null ? "-" : student.getSemester());

        // Load Image
        if (student.getImage() != null && !student.getImage().isEmpty()) {
            try {
                java.io.File file = new java.io.File(student.getImage());
                if (file.exists()) {
                    // studentImage.setImage(new javafx.scene.image.Image(file.toURI().toString()));
                    // Need to add ImageView to FXML first, skipping for now if not in FXML
                }
            } catch (Exception e) {
            }
        }

        loadGrades(student.getStudentId());
    }

    private void loadGrades(int studentId) {
        String sql = "SELECT * FROM student_grades WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double first = rs.getDouble("first_sem");
                double second = rs.getDouble("second_sem");
                double finalG = rs.getDouble("final");

                lblFirstSem.setText(first == 0 ? "-" : String.valueOf(first));
                lblSecondSem.setText(second == 0 ? "-" : String.valueOf(second));
                lblFinal.setText(finalG == 0 ? "-" : String.valueOf(finalG));
            } else {
                lblFirstSem.setText("N/A");
                lblSecondSem.setText("N/A");
                lblFinal.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}