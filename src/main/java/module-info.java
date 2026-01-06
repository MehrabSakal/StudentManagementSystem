module com.example.studentmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.sql; // You'll need this later for database connectivity

    // Open the package so JavaFX can access your Controllers via reflection
    opens com.example.studentmanagementsystem to javafx.fxml;

    // Export the package so other modules can access your classes
    exports com.example.studentmanagementsystem;
}