package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentListController {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Student, Integer> colStudentId;
    @FXML
    private TableColumn<Student, String> colYear;

    // RENAMED from colCourse to colDepartment to match your change
    @FXML
    private TableColumn<Student, String> colDepartment;

    @FXML
    private TableColumn<Student, String> colFirstName;
    @FXML
    private TableColumn<Student, String> colLastName;
    @FXML
    private TableColumn<Student, String> colGender;
    @FXML
    private TableColumn<Student, String> colBirthDate;
    @FXML
    private TableColumn<Student, String> colStatus;
    @FXML
    private TableColumn<Student, String> colSemester;
    @FXML
    private TableColumn<Student, String> colFatherName;
    @FXML
    private TableColumn<Student, String> colMotherName;
    @FXML
    private TableColumn<Student, String> colPresentAddress;
    @FXML
    private TableColumn<Student, String> colPermanentAddress;

    @FXML
    private Label statusLabel;

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
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colFatherName.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        colMotherName.setCellValueFactory(new PropertyValueFactory<>("motherName"));
        colPresentAddress.setCellValueFactory(new PropertyValueFactory<>("presentAddress"));
        colPermanentAddress.setCellValueFactory(new PropertyValueFactory<>("permanentAddress"));

        setupSearchAndSort();
        refreshTable();
    }

    private void setupSearchAndSort() {
        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Student> filteredData = new FilteredList<>(studentList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(student.getStudentId()).contains(lowerCaseFilter))
                    return true; // Filter matches ID.
                if (student.getFirstName().toLowerCase().contains(lowerCaseFilter))
                    return true; // Filter matches first name.
                if (student.getLastName().toLowerCase().contains(lowerCaseFilter))
                    return true; // Filter matches last name.
                if (student.getDepartment().toLowerCase().contains(lowerCaseFilter))
                    return true; // Filter matches Department.

                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Student> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(studentTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        studentTable.setItems(sortedData);
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
                        rs.getString("department"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("birth_date"),
                        rs.getString("status"),
                        rs.getString("image"),
                        rs.getString("present_address"),
                        rs.getString("permanent_address"),
                        rs.getString("father_name"),
                        rs.getString("mother_name"),
                        rs.getString("semester")));
            }
            // studentTable.setItems(studentList); // REMOVED: Managed by SortedList in
            // initialize
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