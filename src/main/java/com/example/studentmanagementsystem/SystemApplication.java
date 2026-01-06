package com.example.studentmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SystemApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // --- ADD THIS LINE HERE ---
        // This forces the database to create the 'courses' table if it's missing
        DatabaseConnection.initializeDatabase();
        // --------------------------

        Parent root = FXMLLoader.load(getClass().getResource("RoleSelection.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();
    }
}