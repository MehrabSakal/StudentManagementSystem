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

public class DepartmentDataController {

    @FXML private TextField codeField;
    @FXML private TextField nameField;
    @FXML private Label statusLabel;
    @FXML private TableView<Department> deptTable;
    @FXML private TableColumn<Department, String> colCode;
    @FXML private TableColumn<Department, String> colName;

    private ObservableList<Department> deptList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadDepartments();
    }

    private void loadDepartments() {
        deptList.clear();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deptList.add(new Department(rs.getString("dept_code"), rs.getString("dept_name")));
            }
            deptTable.setItems(deptList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addDepartment(ActionEvent event) {
        if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
            statusLabel.setText("Please fill all fields!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String sql = "INSERT INTO departments (dept_code, dept_name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codeField.getText());
            pstmt.setString(2, nameField.getText());
            pstmt.executeUpdate();

            statusLabel.setText("Department Added!");
            statusLabel.setStyle("-fx-text-fill: green;");

            codeField.clear();
            nameField.clear();
            loadDepartments(); // Refresh the table

        } catch (Exception e) {
            statusLabel.setText("Error: Department likely exists.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void deleteDepartment(ActionEvent event) {
        Department selected = deptTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String sql = "DELETE FROM departments WHERE dept_code = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, selected.getCode());
            pstmt.executeUpdate();
            loadDepartments();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}