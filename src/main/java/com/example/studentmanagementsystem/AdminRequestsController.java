package com.example.studentmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminRequestsController {

    @FXML private TableView<EditRequest> requestTable;
    @FXML private TableColumn<EditRequest, Integer> colId;
    @FXML private TableColumn<EditRequest, Integer> colStudentId;
    @FXML private TableColumn<EditRequest, String> colField;
    @FXML private TableColumn<EditRequest, String> colNewValue;
    @FXML private TableColumn<EditRequest, String> colStatus;
    @FXML private Label statusLabel;

    private ObservableList<EditRequest> requestList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colField.setCellValueFactory(new PropertyValueFactory<>("field"));
        colNewValue.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadPendingRequests();
    }

    private void loadPendingRequests() {
        requestList.clear();
        String sql = "SELECT * FROM edit_requests WHERE status = 'Pending'";
        try (Connection conn = DatabaseConnection.connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {

            while (rs.next()) {
                requestList.add(new EditRequest(
                        rs.getInt("request_id"),
                        rs.getInt("student_id"),
                        rs.getString("field_to_change"),
                        rs.getString("new_value"),
                        rs.getString("status")
                ));
            }
            requestTable.setItems(requestList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void approveRequest(ActionEvent event) {
        EditRequest req = requestTable.getSelectionModel().getSelectedItem();
        if (req == null) return;

        // 1. Determine which DB column to update
        String dbColumn = "";
        switch (req.getField()) {
            case "First Name": dbColumn = "first_name"; break;
            case "Last Name": dbColumn = "last_name"; break;
            case "Gender": dbColumn = "gender"; break;
            case "Birth Date": dbColumn = "birth_date"; break;
            default: return; // Invalid field
        }

        String updateStudentSql = "UPDATE students SET " + dbColumn + " = ? WHERE student_id = ?";
        String updateRequestSql = "UPDATE edit_requests SET status = 'Approved' WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false); // Transaction

            // Update Real Student Data
            try (PreparedStatement pstmt1 = conn.prepareStatement(updateStudentSql)) {
                pstmt1.setString(1, req.getNewValue());
                pstmt1.setInt(2, req.getStudentId());
                pstmt1.executeUpdate();
            }

            // Mark Request as Approved
            try (PreparedStatement pstmt2 = conn.prepareStatement(updateRequestSql)) {
                pstmt2.setInt(1, req.getRequestId());
                pstmt2.executeUpdate();
            }

            conn.commit();
            statusLabel.setText("Approved & Updated!");
            loadPendingRequests(); // Refresh table

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void rejectRequest(ActionEvent event) {
        EditRequest req = requestTable.getSelectionModel().getSelectedItem();
        if (req == null) return;

        String sql = "UPDATE edit_requests SET status = 'Rejected' WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, req.getRequestId());
            pstmt.executeUpdate();

            statusLabel.setText("Request Rejected.");
            loadPendingRequests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}