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
    private String image;
    private String presentAddress;
    private String permanentAddress;
    private String fatherName;
    private String motherName;
    private String semester;

    public Student(int studentId, String year, String department, String firstName, String lastName, String gender,
            String birthDate, String status, String image, String presentAddress, String permanentAddress,
            String fatherName, String motherName, String semester) {
        this.studentId = studentId;
        this.year = year;
        this.department = department;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = status;
        this.image = image;
        this.presentAddress = presentAddress;
        this.permanentAddress = permanentAddress;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.semester = semester;
    }

    // Existing Constructor overload for backward compatibility (optional but good)
    public Student(int studentId, String year, String department, String firstName, String lastName, String gender,
            String birthDate, String status) {
        this(studentId, year, department, firstName, lastName, gender, birthDate, status, null, null, null, null, null,
                null);
    }

    public int getStudentId() {
        return studentId;
    }

    public String getYear() {
        return year;
    }

    public String getDepartment() {
        return department;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public String getSemester() {
        return semester;
    }
}