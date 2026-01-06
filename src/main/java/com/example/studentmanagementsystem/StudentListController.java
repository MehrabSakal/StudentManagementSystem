package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentListController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> colStudentId;
    @FXML private TableColumn<Student, String> colYear;

    // RENAMED from colCourse to colDepartment to match your change
    @FXML private TableColumn<Student, String> colDepartment;

    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableColumn<Student, String> colLastName;
    @FXML private TableColumn<Student, String> colGender;
    @FXML private TableColumn<Student, String> colBirthDate;
    @FXML private TableColumn<Student, String> colStatus;

    @FXML private Label statusLabel;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map these to the getters in Student.java
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department")); // Look for getDepartment()
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        refreshTable();
    }

    @FXML
    public void refreshTable() {
        studentList.clear();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                studentList.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("year"),
                        rs.getString("department"), // Fetch the new simple department column
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("birth_date"),
                        rs.getString("status")
                ));
            }
            studentTable.setItems(studentList);
            statusLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteStudent(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a student first!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, selected.getStudentId());
            pstmt.executeUpdate();

            statusLabel.setText("Deleted Successfully.");
            statusLabel.setStyle("-fx-text-fill: green;");
            refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Deletion Failed.");
        }
    }

    @FXML
    public void editStudent(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a student to edit!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateStudent.fxml"));
            Parent root = loader.load();

            UpdateStudentController controller = loader.getController();
            controller.setStudentData(selected);

            Stage stage = new Stage();
            stage.setTitle("Update Student");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}