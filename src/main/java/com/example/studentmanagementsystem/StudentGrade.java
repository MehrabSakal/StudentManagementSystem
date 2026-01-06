package com.example.studentmanagementsystem;

public class StudentGrade {
    private int studentId;
    private String year;
    private String department; // Changed from course
    private Double firstSem;
    private Double secondSem;
    private Double finalGrade;

    public StudentGrade(int studentId, String year, String department, Double firstSem, Double secondSem, Double finalGrade) {
        this.studentId = studentId;
        this.year = year;
        this.department = department;
        this.firstSem = firstSem;
        this.secondSem = secondSem;
        this.finalGrade = finalGrade;
    }

    public int getStudentId() { return studentId; }
    public String getYear() { return year; }
    public String getDepartment() { return department; } // Updated Getter
    public Double getFirstSem() { return firstSem; }
    public Double getSecondSem() { return secondSem; }
    public Double getFinalGrade() { return finalGrade; }
}