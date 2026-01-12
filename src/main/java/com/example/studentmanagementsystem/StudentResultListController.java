package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentResultListController {

    @FXML
    private TableView<StudentGrade> resultsTable;
    @FXML
    private TableColumn<StudentGrade, Integer> colId;
    @FXML
    private TableColumn<StudentGrade, String> colDepartment;
    @FXML
    private TableColumn<StudentGrade, String> colYear;
    @FXML
    private TableColumn<StudentGrade, Double> colFirstSem;
    @FXML
    private TableColumn<StudentGrade, Double> colSecondSem;
    @FXML
    private TableColumn<StudentGrade, Double> colFinal;

    private ObservableList<StudentGrade> resultList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colFirstSem.setCellValueFactory(new PropertyValueFactory<>("firstSem"));
        colSecondSem.setCellValueFactory(new PropertyValueFactory<>("secondSem"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));

        loadResults();
    }

    private void loadResults() {
        resultList.clear();
        String sql = "SELECT s.student_id, s.year, s.department, " +
                "g.first_sem, g.second_sem, g.final " +
                "FROM students s " +
                "JOIN student_grades g ON s.student_id = g.student_id"; // JOIN vs LEFT JOIN? JOIN shows only those with
                                                                        // results.

        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultList.add(new StudentGrade(
                        rs.getInt("student_id"),
                        rs.getString("year"),
                        rs.getString("department"),
                        rs.getDouble("first_sem"),
                        rs.getDouble("second_sem"),
                        rs.getDouble("final")));
            }
            resultsTable.setItems(resultList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
