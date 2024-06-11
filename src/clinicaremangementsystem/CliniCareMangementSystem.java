/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicaremangementsystem;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.Alert;

/**
 *
 * @author Joel
 */
public class CliniCareMangementSystem extends Application {
    private Stage primaryStage; 
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  
        primaryStage.setTitle("CliniCare - Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Welcome Title
        Text sceneTitle = new Text("Welcome to CliniCare");
        grid.add(sceneTitle, 0, 0, 2, 1);

        // Create labels and text field for userID and password
        Label userIdLabel = new Label("User ID:");
        TextField userIdTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        grid.add(userIdLabel, 0, 1);
        grid.add(userIdTextField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        // Sign-in button
        Button loginButton = new Button("Sign in");
        loginButton.setOnAction(e -> {
            if (authenticateUser(userIdTextField.getText(), passwordField.getText())) {
                System.out.println("Login Successful");
                String role = getUserRole(userIdTextField.getText());
                if (role.equals("admin")) {
                    openAdminDashboard();
                } else {
                    openPatientDashboard();
                }
            } else {
                System.out.println("Login Failed");
                showAlert("Login Failed", "Invalid UserID or Password.");
            }
        });
        HBox hbLoginBtn = new HBox(10);
        hbLoginBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbLoginBtn.getChildren().add(loginButton);
        grid.add(hbLoginBtn, 1, 4);

        // Hyperlink for new users to register
        Hyperlink newUsersLink = new Hyperlink("New User? Create an account.");
        newUsersLink.setOnAction(e -> openRegistrationForm());
        grid.add(newUsersLink, 1, 5);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Authenticate user with database
    private boolean authenticateUser(String userID, String password) {
        String sql = "SELECT * FROM login WHERE userID = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.out.println("Error connecting to the database");
            ex.printStackTrace();
            return false;
        }
    }

    // Get user role from the database
    private String getUserRole(String userID) {
        String sql = "SELECT role FROM login WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException ex) {
            System.out.println("Error connecting to the database");
            ex.printStackTrace();
        }
        return "patient";
    }

    // Open patient dashboard upon successful login
    private void openPatientDashboard() {
        PatientDashboard dashboard = new PatientDashboard();
        try {
            dashboard.start(new Stage());
            primaryStage.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Open admin dashboard upon successful login
    private void openAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard();
        try {
            adminDashboard.start(new Stage());
            primaryStage.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // registration form for new patients
    private void openRegistrationForm() {
        Stage registerStage = new Stage();
        registerStage.setTitle("Registration");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Form field for new patients
        addFormField(grid, "User ID:", 0, false);
        addFormField(grid, "Name:", 1, false);
        addFormField(grid, "Email:", 2, false);
        addFormField(grid, "Age:", 3, false);
        addFormField(grid, "Address:", 4, false);
        addFormField(grid, "City:", 5, false);
        addFormField(grid, "State:", 6, false);
        addFormField(grid, "Zip Code:", 7, false);
        addFormField(grid, "Phone Number:", 8, false);
        addFormField(grid, "Password:", 9, true);

        // Register button
        Button submitButton = new Button("Register");
        submitButton.setOnAction(e -> {
            System.out.println("Registration Submitted");
            registerStage.close();
        });
        grid.add(submitButton, 1, 10);

        Scene scene = new Scene(grid, 350, 500);
        registerStage.setScene(scene);
        registerStage.show();
    }

    // Form field utility method
    private void addFormField(GridPane grid, String label, int row, boolean isPassword) {
        Label fieldLabel = new Label(label);
        TextField textField;
        if (isPassword) {
            textField = new PasswordField();
        } else {
            textField = new TextField();
        }
        grid.add(fieldLabel, 0, row);
        grid.add(textField, 1, row);
    }

    // Show alert for errors 
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}