package com.example.studentmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:student_data.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void initializeDatabase() {
        // 1. Students Table (Added 'department' column back)
        String sqlStudents = "CREATE TABLE IF NOT EXISTS students (\n"
                + " student_id integer PRIMARY KEY,\n"
                + " password text,\n"
                + " year text,\n"
                + " department text,\n"
                + " first_name text NOT NULL,\n"
                + " last_name text NOT NULL,\n"
                + " gender text,\n"
                + " birth_date text,\n"
                + " status text,\n"
                + " image text,\n"
                + " present_address text,\n"
                + " permanent_address text,\n"
                + " father_name text,\n"
                + " mother_name text,\n"
                + " semester text\n"
                + ");";

        // 2. Departments Table (Renamed from courses)
        String sqlDepartments = "CREATE TABLE IF NOT EXISTS departments (\n"
                + " dept_code text PRIMARY KEY,\n"
                + " dept_name text NOT NULL\n"
                + ");";

        // 3. Grades Table
        String sqlGrades = "CREATE TABLE IF NOT EXISTS student_grades (\n"
                + " student_id integer PRIMARY KEY,\n"
                + " first_sem double,\n"
                + " second_sem double,\n"
                + " final double,\n"
                + " FOREIGN KEY (student_id) REFERENCES students(student_id)\n"
                + ");";

        // 4. Edit Requests
        String sqlRequests = "CREATE TABLE IF NOT EXISTS edit_requests (\n"
                + " request_id integer PRIMARY KEY,\n"
                + " student_id integer,\n"
                + " field_to_change text,\n"
                + " new_value text,\n"
                + " status text DEFAULT 'Pending',\n"
                + " FOREIGN KEY (student_id) REFERENCES students(student_id)\n"
                + ");";

        // 5. Course Grades (Detailed Result)
        String sqlCourseGrades = "CREATE TABLE IF NOT EXISTS student_course_grades (\n"
                + " id integer PRIMARY KEY AUTOINCREMENT,\n"
                + " student_id integer,\n"
                + " semester text,\n"
                + " course_name text,\n"
                + " credit double,\n"
                + " gpa double,\n"
                + " FOREIGN KEY (student_id) REFERENCES students(student_id)\n"
                + ");";

        try (Connection conn = connect();
                java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sqlStudents);
            stmt.execute(sqlDepartments);
            stmt.execute(sqlGrades);
            stmt.execute(sqlRequests);
            stmt.execute(sqlCourseGrades);

            // Migration for existing tables - Try to add columns if they don't exist
            String[] migrations = {
                    "ALTER TABLE students ADD COLUMN image text;",
                    "ALTER TABLE students ADD COLUMN present_address text;",
                    "ALTER TABLE students ADD COLUMN permanent_address text;",
                    "ALTER TABLE students ADD COLUMN father_name text;",
                    "ALTER TABLE students ADD COLUMN mother_name text;",
                    "ALTER TABLE students ADD COLUMN semester text;",
                    "ALTER TABLE students ADD COLUMN password text;"
            };

            for (String migration : migrations) {
                try {
                    stmt.execute(migration);
                } catch (SQLException ignored) {
                    // Column likely already exists
                }
            }

            System.out.println("Database initialized: Single Department Mode.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}