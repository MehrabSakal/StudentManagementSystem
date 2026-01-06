package com.example.studentmanagementsystem;

public class EditRequest {
    private int requestId;
    private int studentId;
    private String field;
    private String newValue;
    private String status;

    public EditRequest(int requestId, int studentId, String field, String newValue, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.field = field;
        this.newValue = newValue;
        this.status = status;
    }
    // Getters
    public int getRequestId() { return requestId; }
    public int getStudentId() { return studentId; }
    public String getField() { return field; }
    public String getNewValue() { return newValue; }
    public String getStatus() { return status; }
}