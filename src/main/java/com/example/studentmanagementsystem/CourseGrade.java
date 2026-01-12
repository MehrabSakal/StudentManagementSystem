package com.example.studentmanagementsystem;

public class CourseGrade {
    private String courseName;
    private double credit;
    private double gpa;

    public CourseGrade(String courseName, double credit, double gpa) {
        this.courseName = courseName;
        this.credit = credit;
        this.gpa = gpa;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getCredit() {
        return credit;
    }

    public double getGpa() {
        return gpa;
    }
}
