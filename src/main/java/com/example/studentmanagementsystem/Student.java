package com.example.studentmanagementsystem;

public class Student {
    private int studentId;
    private String year;
    private String department; // Renamed
    private String firstName;
    private String lastName;
    private String gender;
    private String birthDate;
    private String status;

    public Student(int studentId, String year, String department, String firstName, String lastName, String gender, String birthDate, String status) {
        this.studentId = studentId;
        this.year = year;
        this.department = department;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = status;
    }

    public int getStudentId() { return studentId; }
    public String getYear() { return year; }
    public String getDepartment() { return department; } // Renamed getter
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getGender() { return gender; }
    public String getBirthDate() { return birthDate; }
    public String getStatus() { return status; }
}