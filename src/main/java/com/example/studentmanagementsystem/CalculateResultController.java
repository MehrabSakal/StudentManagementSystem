package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CalculateResultController {

    @FXML
    private TextField studentIdField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField departmentField; // New
    @FXML
    private ComboBox<String> semesterComboBox;
    @FXML
    private VBox coursesContainer;
    @FXML
    private Label resultLabel;
    @FXML
    private Label statusLabel;

    private List<HBox> courseRows = new ArrayList<>();

    @FXML
    public void initialize() {
        semesterComboBox.setItems(FXCollections.observableArrayList("1st Semester", "2nd Semester"));

        // Add one initial row
        addCourseRow(null);

        // Auto-fill listener
        studentIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                yearField.clear();
                departmentField.clear();
                return;
            }

            try {
                int id = Integer.parseInt(newValue);
                fetchStudentDetails(id);
            } catch (NumberFormatException e) {
                // Ignore while typing
            }
        });
    }

    private void fetchStudentDetails(int studentId) {
        String sql = "SELECT year, department FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                yearField.setText(rs.getString("year"));
                departmentField.setText(rs.getString("department"));
                statusLabel.setText("");
            } else {
                yearField.clear();
                departmentField.clear();
                statusLabel.setText("Student ID not found.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addCourseRow(ActionEvent event) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        TextField courseName = new TextField();
        courseName.setPromptText("Course Name");
        courseName.setPrefWidth(200);

        TextField credit = new TextField();
        credit.setPromptText("Credit");
        credit.setPrefWidth(80);

        TextField gpa = new TextField();
        gpa.setPromptText("GPA");
        gpa.setPrefWidth(80);

        row.getChildren().addAll(courseName, credit, gpa);
        coursesContainer.getChildren().add(row);
        courseRows.add(row);
    }

    @FXML
    public void calculateAndSave(ActionEvent event) {
        if (studentIdField.getText().isEmpty() || semesterComboBox.getValue() == null) {
            statusLabel.setText("Please enter Student ID and Semester.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdField.getText());
            String year = yearField.getText(); // Optional validation

            double totalPoints = 0;
            double totalCredits = 0;

            for (HBox row : courseRows) {
                TextField creditField = (TextField) row.getChildren().get(1);
                TextField gpaField = (TextField) row.getChildren().get(2);

                if (!creditField.getText().isEmpty() && !gpaField.getText().isEmpty()) {
                    double credit = Double.parseDouble(creditField.getText());
                    double gpa = Double.parseDouble(gpaField.getText());

                    totalPoints += (credit * gpa);
                    totalCredits += credit;
                }
            }

            if (totalCredits == 0) {
                statusLabel.setText("Total credits cannot be zero.");
                return;
            }

            double gpa = totalPoints / totalCredits;
            resultLabel.setText(String.format("GPA: %.2f", gpa));

            saveToDatabase(studentId, semesterComboBox.getValue(), gpa);

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid ID, Credit, or GPA format.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void saveToDatabase(int studentId, String semester, double gpa) {
        // Logic: specific to existing student_grades table designed in GradesController
        // Table: student_grades (student_id, first_sem, second_sem, final)

        String column = semester.equals("1st Semester") ? "first_sem" : "second_sem";

        // First check if record exists
        String checkSql = "SELECT student_id FROM student_grades WHERE student_id = ?";
        String insertSql = "INSERT INTO student_grades (student_id, " + column + ", final) VALUES (?, ?, 0.0)";
        String updateSql = "UPDATE student_grades SET " + column + " = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect()) {

            // Check existence
            boolean exists = false;
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setInt(1, studentId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next())
                        exists = true;
                }
            }

            if (exists) {
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setDouble(1, gpa);
                    pstmt.setInt(2, studentId);
                    pstmt.executeUpdate();
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    pstmt.setInt(1, studentId);
                    pstmt.setDouble(2, gpa);
                    pstmt.executeUpdate();
                }
            }

            // === NEW: Save Detailed Course Grades ===
            // 1. Delete existing details for this student & semester to avoid duplicates
            String deleteDetailsSql = "DELETE FROM student_course_grades WHERE student_id = ? AND semester = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteDetailsSql)) {
                deleteStmt.setInt(1, studentId);
                deleteStmt.setString(2, semester);
                deleteStmt.executeUpdate();
            }

            // 2. Insert new details
            String insertDetailSql = "INSERT INTO student_course_grades (student_id, semester, course_name, credit, gpa) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement detailStmt = conn.prepareStatement(insertDetailSql)) {
                for (HBox row : courseRows) {
                    TextField courseField = (TextField) row.getChildren().get(0);
                    TextField creditField = (TextField) row.getChildren().get(1);
                    TextField gpaField = (TextField) row.getChildren().get(2);

                    if (!courseField.getText().isEmpty() && !creditField.getText().isEmpty()
                            && !gpaField.getText().isEmpty()) {

                        detailStmt.setInt(1, studentId);
                        detailStmt.setString(2, semester);
                        detailStmt.setString(3, courseField.getText());
                        detailStmt.setDouble(4, Double.parseDouble(creditField.getText()));
                        detailStmt.setDouble(5, Double.parseDouble(gpaField.getText()));

                        detailStmt.addBatch();
                    }
                }
                detailStmt.executeBatch();
            }
            // ========================================

            // Recalculate Final if both exist and update it?
            // Optional enhancement.

            statusLabel.setText("Saved Successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Database Error.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
