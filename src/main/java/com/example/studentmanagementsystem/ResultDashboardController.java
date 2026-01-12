package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ResultDashboardController {

    @FXML
    public void showCalculateResult(ActionEvent event) {
        try {
            // Placeholder: Load CalculateResult.fxml (To be created)
            Parent view = FXMLLoader.load(getClass().getResource("CalculateResult.fxml"));
            // We need to access the main content area from Dashboard to replace view
            // But here we are inside the sub-view.
            // Strategy: DashboardController manages the view.

            // ALTERNATIVE: Use the same strategy as DashboardController - replacing content
            // of current AnchorPane?
            // No, ResultDashboard is likely loaded INTO Dashboard.mainContent.
            // To replace it, we can get the scene's root or pass the parent container.

            // SIMPLER: DashboardController has a static reference or singleton ?? No.

            // For now, let's assume we replace the *content* of the current AnchorPane
            // (which is this view)
            // But catching the parent AnchorPane is tricky without reference.

            // BETTER APPROACH:
            // Load the new FXML and set it as children of the PARENT of this node.

            AnchorPane parent = (AnchorPane) ((javafx.scene.Node) event.getSource()).getScene().lookup("#mainContent");
            if (parent != null) {
                parent.getChildren().clear();
                parent.getChildren().add(view);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showStudentsResult(ActionEvent event) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("StudentResultList.fxml"));
            AnchorPane parent = (AnchorPane) ((javafx.scene.Node) event.getSource()).getScene().lookup("#mainContent");
            if (parent != null) {
                parent.getChildren().clear();
                parent.getChildren().add(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
