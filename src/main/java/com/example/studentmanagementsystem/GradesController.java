package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GradesController {

    @FXML private TextField idField;
    @FXML private TextField yearField;
    @FXML private TextField deptField; // Renamed from courseField
    @FXML private TextField firstSemField;
    @FXML private TextField secondSemField;
    @FXML private Label statusLabel;

    @FXML private TableView<StudentGrade> gradesTable;
    @FXML private TableColumn<StudentGrade, Integer> colId;
    @FXML private TableColumn<StudentGrade, String> colYear;
    @FXML private TableColumn<StudentGrade, String> colDept; // Renamed
    @FXML private TableColumn<StudentGrade, Double> colFirst;
    @FXML private TableColumn<StudentGrade, Double> colSecond;
    @FXML private TableColumn<StudentGrade, Double> colFinal;

    private ObservableList<StudentGrade> gradeList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colDept.setCellValueFactory(new PropertyValueFactory<>("department")); // Look for getDepartment()
        colFirst.setCellValueFactory(new PropertyValueFactory<>("firstSem"));
        colSecond.setCellValueFactory(new PropertyValueFactory<>("secondSem"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));

        loadGrades();

        // Listener: When row clicked, fill the form
        gradesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(String.valueOf(newSelection.getStudentId()));
                yearField.setText(newSelection.getYear());
                deptField.setText(newSelection.getDepartment());

                // Show grades if they exist (not 0.0)
                firstSemField.setText(newSelection.getFirstSem() == 0 ? "" : String.valueOf(newSelection.getFirstSem()));
                secondSemField.setText(newSelection.getSecondSem() == 0 ? "" : String.valueOf(newSelection.getSecondSem()));
            }
        });
    }

    private void loadGrades() {
        gradeList.clear();

        // FIXED SQL: Fetch 'department' directly from students table
        String sql = "SELECT s.student_id, s.year, s.department, " +
                "g.first_sem, g.second_sem, g.final " +
                "FROM students s " +
                "LEFT JOIN student_grades g ON s.student_id = g.student_id";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                gradeList.add(new StudentGrade(
                        rs.getInt("student_id"),
                        rs.getString("year"),
                        rs.getString("department"), // Simple string now
                        rs.getDouble("first_sem"),
                        rs.getDouble("second_sem"),
                        rs.getDouble("final")
                ));
            }
            gradesTable.setItems(gradeList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateGrade(ActionEvent event) {
        if (idField.getText().isEmpty() || firstSemField.getText().isEmpty() || secondSemField.getText().isEmpty()) {
            statusLabel.setText("Select student & enter grades.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            double first = Double.parseDouble(firstSemField.getText());
            double second = Double.parseDouble(secondSemField.getText());
            double finalGrade = (first + second) / 2.0;

            String sql = "INSERT OR REPLACE INTO student_grades (student_id, first_sem, second_sem, final) VALUES (?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.setDouble(2, first);
                pstmt.setDouble(3, second);
                pstmt.setDouble(4, finalGrade);

                pstmt.executeUpdate();

                statusLabel.setText("Grades Updated!");
                statusLabel.setStyle("-fx-text-fill: green;");
                loadGrades();
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Grades must be numbers.");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clearFields() {
        idField.clear();
        yearField.clear();
        deptField.clear();
        firstSemField.clear();
        secondSemField.clear();
        statusLabel.setText("");
    }
}