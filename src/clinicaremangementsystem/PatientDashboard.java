/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicaremangementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Joel
 */
public class PatientDashboard extends Application {
     
    private Stage primaryStage;
    private ListView<String> appointmentsList = new ListView<>();
    private ComboBox<String> doctorComboBox; // ComboBox for doctors
    private ListView<String> availableTimesListView; // ListView for available times
    private ObservableList<String> availableTimes; // ObservableList for available times
    private String selectedDoctorName; // Variable to store selected doctor name
    private String selectedDoctorID; // Variable to store selected doctor ID

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setupUI();
    }

    // User interface of the patient dashboard
    private void setupUI() {
        // Title
        primaryStage.setTitle("Patient Dashboard");

        // VBox layout
        VBox layoutJR = new VBox(10);
        layoutJR.setPadding(new Insets(20));
        layoutJR.setAlignment(Pos.CENTER);

        // Doctor selection
        doctorComboBox = new ComboBox<>();
        doctorComboBox.setPromptText("Select Doctor");
        loadDoctors();

        // Appointment times list
        availableTimes = FXCollections.observableArrayList();
        availableTimesListView = new ListView<>(availableTimes);

        // Load available times when a doctor is selected
        doctorComboBox.setOnAction(e -> loadAvailableTimes(doctorComboBox.getSelectionModel().getSelectedItem()));

        // Buttons
        Button viewAppointmentsButtonJR = new Button("View Appointments");
        Button bookAppointmentButtonJR = new Button("Book Appointment");
        Button cancelAppointmentButtonJR = new Button("Cancel Appointment");
        Button logoutButton = new Button("Logout");

        // Button actions
        viewAppointmentsButtonJR.setOnAction(e -> viewAppointments());
        bookAppointmentButtonJR.setOnAction(e -> bookAppointment());
        cancelAppointmentButtonJR.setOnAction(e -> cancelAppointment());
        logoutButton.setOnAction(e -> logout());

        // Add all elements to the layout
        layoutJR.getChildren().addAll(new Label("Book an Appointment"), doctorComboBox, availableTimesListView, viewAppointmentsButtonJR, appointmentsList, bookAppointmentButtonJR, cancelAppointmentButtonJR, logoutButton);

        primaryStage.setScene(new Scene(layoutJR, 400, 400));
        primaryStage.show();
    }

    // View appointments
    private void viewAppointments() {
        ObservableList<String> items = FXCollections.observableArrayList(fetchAppointments());
        appointmentsList.setItems(items);
    }

    // Retrieve appointments from the database
    private List<String> fetchAppointments() {
        List<String> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(rs.getString("appointmentID") + " - " + rs.getString("date") + " at " + rs.getString("time") + " with " + rs.getString("doctorName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Load doctors from database
    private void loadDoctors() {
        String sql = "SELECT doctorID, name FROM Doctors";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String doctorID = rs.getString("doctorID");
                String doctorName = rs.getString("name");
                doctorComboBox.getItems().add(doctorName + " (" + doctorID + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load available times for selected doctor
    private void loadAvailableTimes(String selectedDoctor) {
        availableTimes.clear();
        selectedDoctorName = selectedDoctor.substring(0, selectedDoctor.indexOf('(')).trim();
        selectedDoctorID = selectedDoctor.substring(selectedDoctor.indexOf('(') + 1, selectedDoctor.indexOf(')')).trim();
        String sql = "SELECT availableTimes FROM Doctors WHERE doctorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, selectedDoctorID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String[] times = rs.getString("availableTimes").split(", ");
                availableTimes.addAll(times);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Book new appointments (does not work)
    private void bookAppointment() {
        String selectedTime = availableTimesListView.getSelectionModel().getSelectedItem();
        if (selectedDoctorName == null || selectedTime == null) {
            showAlert("Error", "Please select a doctor and an available time.");
            return;
        }

        String sql = "INSERT INTO Appointments (appointmentID, patientID, doctorID, doctorName, date, time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Generate a unique appointment ID
            String appointmentID = "A" + System.currentTimeMillis(); // Simple unique ID generation
            String patientID = "P001"; // Replace with the actual patient ID
            String date = "2024-06-01"; // Replace with the actual date
            String time = selectedTime;

            pstmt.setString(1, appointmentID);
            pstmt.setString(2, patientID);
            pstmt.setString(3, selectedDoctorID); // Use doctor ID
            pstmt.setString(4, selectedDoctorName); // Use doctor name
            pstmt.setString(5, date);
            pstmt.setString(6, time);
            pstmt.executeUpdate();

            showAlert("Success", "Appointment booked successfully!");

            // Optionally, refresh available times or perform other actions
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to book appointment.");
        }
    }

    // Cancel appointments
    private void cancelAppointment() {
        String selectedAppointment = appointmentsList.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null && !selectedAppointment.isEmpty()) {
            String sql = "DELETE FROM Appointments WHERE appointmentID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, selectedAppointment.split(" - ")[0]);
                pstmt.executeUpdate();
                viewAppointments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Logout 
    private void logout() {
        primaryStage.close();
        CliniCareMangementSystem loginApp = new CliniCareMangementSystem();
        try {
            loginApp.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show alert
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

// Database connection
class DatabaseConnection {
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3308/ClincCare";
        String user = "root";
        String password = "password";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}