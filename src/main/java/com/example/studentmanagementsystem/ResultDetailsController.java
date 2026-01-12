package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResultDetailsController {

    @FXML
    private Label semesterLabel;
    @FXML
    private TableView<CourseGrade> detailsTable;
    @FXML
    private TableColumn<CourseGrade, String> colCourse;
    @FXML
    private TableColumn<CourseGrade, Double> colCredit;
    @FXML
    private TableColumn<CourseGrade, Double> colGpa;

    private ObservableList<CourseGrade> list = FXCollections.observableArrayList();

    public void setDetails(int studentId, String semester) {
        semesterLabel.setText(semester + " Result Details");

        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colGpa.setCellValueFactory(new PropertyValueFactory<>("gpa"));

        loadData(studentId, semester);
    }

    private void loadData(int studentId, String semester) {
        list.clear();
        String sql = "SELECT course_name, credit, gpa FROM student_course_grades WHERE student_id = ? AND semester = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setString(2, semester);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new CourseGrade(
                        rs.getString("course_name"),
                        rs.getDouble("credit"),
                        rs.getDouble("gpa")));
            }
            detailsTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
