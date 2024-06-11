/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicaremangementsystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Joel
 */
public class AdminDashboard extends Application {
    
   public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        // VBox layout for the overall dashboard
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // Title label
        Label title = new Label("Admin Dashboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        // Load the image
        Image adminImage = new Image("file:C:/Users/Joel/OneDrive/Desktop/CS421/admin-icon-vector.jpg");
        ImageView imageView = new ImageView(adminImage);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        // HBox layout for title and image
        HBox titleImageBox = new HBox(10, imageView, titleBox);
        titleImageBox.setAlignment(Pos.CENTER);

        // GridPane layout for buttons
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // Buttons for different functions
        Button manageAppointmentsBtn = new Button("Manage Appointments");
        Button managePatientsBtn = new Button("Manage Patients");
        Button logoutBtn = new Button("Logout");

        // Add buttons to the grid
        grid.add(manageAppointmentsBtn, 0, 0);
        grid.add(managePatientsBtn, 1, 0);

        // HBox layout for the logout button
        HBox logoutBox = new HBox(logoutBtn);
        logoutBox.setAlignment(Pos.TOP_RIGHT);

        // Add all elements to main layout
        layout.getChildren().addAll(titleImageBox, grid, logoutBox);

        // Create the scene and set the stage
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set button actions
        logoutBtn.setOnAction(e -> logout(primaryStage));
        managePatientsBtn.setOnAction(e -> openManageRecords());
        manageAppointmentsBtn.setOnAction(e -> openManageAppointments());
    }

    // logout and redirect to the login screen
    private void logout(Stage primaryStage) {
        primaryStage.close();
        CliniCareMangementSystem loginApp = new CliniCareMangementSystem();
        try {
            loginApp.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the manage records window
    private void openManageRecords() {
        ManageRecords manageRecords = new ManageRecords();
        try {
            manageRecords.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the manage appointments window
    private void openManageAppointments() {
        manageAppointments manageAppointments = new manageAppointments();
        try {
            manageAppointments.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}