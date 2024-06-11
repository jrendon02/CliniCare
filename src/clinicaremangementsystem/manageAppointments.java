/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicaremangementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Joel
 */
public class manageAppointments extends Application {
    
 private TableView<AppointmentClass> tableView;
 private ObservableList<AppointmentClass> appointmentList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manage Appointments");

        // Create the table view and columns
        tableView = new TableView<>();
        TableColumn<AppointmentClass, String> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        TableColumn<AppointmentClass, String> patientIDColumn = new TableColumn<>("Patient ID");
        patientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

        TableColumn<AppointmentClass, String> doctorIDColumn = new TableColumn<>("Doctor ID");
        doctorIDColumn.setCellValueFactory(new PropertyValueFactory<>("doctorID"));

        TableColumn<AppointmentClass, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<AppointmentClass, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<AppointmentClass, String> diagnosisColumn = new TableColumn<>("Diagnosis");
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));

        TableColumn<AppointmentClass, String> followUpDateColumn = new TableColumn<>("Follow Up Date");
        followUpDateColumn.setCellValueFactory(new PropertyValueFactory<>("followUpDate"));

        TableColumn<AppointmentClass, String> roomColumn = new TableColumn<>("Room");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));

        tableView.getColumns().addAll(idColumn, patientIDColumn, doctorIDColumn, dateColumn, timeColumn, diagnosisColumn, followUpDateColumn, roomColumn);

        // Load data into the table
        appointmentList = FXCollections.observableArrayList();
        loadAppointmentData();

        tableView.setItems(appointmentList);

        // Buttons for update, delete, and back
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button backBtn = new Button("Back");

        // Update button action
        updateBtn.setOnAction(e -> openUpdateAppointmentWindow());

        // Delete button action
        deleteBtn.setOnAction(e -> confirmAndDeleteAppointment());

        // Back button action
        backBtn.setOnAction(e -> goBack(primaryStage));

        // Layout for buttons
        HBox buttonBox = new HBox(10, updateBtn, deleteBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Main layout
        VBox layout = new VBox(10, tableView, buttonBox);
        layout.setPadding(new Insets(10));

        // Create the scene and set the stage
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Load appointment data from the database
    private void loadAppointmentData() {
        String sql = "SELECT * FROM Appointments";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet results = pstmt.executeQuery()) {

            while (results.next()) {
                appointmentList.add(new AppointmentClass(
                        results.getString("appointmentID"),
                        results.getString("patientID"),
                        results.getString("doctorID"), // Change to getString
                        results.getString("date"),
                        results.getString("time"),
                        results.getString("diagnosis"),
                        results.getString("followUpDate"),
                        results.getString("room")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Open the update appointment window
    private void openUpdateAppointmentWindow() {
        AppointmentClass selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Stage updateStage = new Stage();
            updateStage.setTitle("Update Appointment");

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10));
            grid.setVgap(10);
            grid.setHgap(10);
            grid.setAlignment(Pos.CENTER);

            // Labels and text fields for appointment details
            Label appointmentIDLabel = new Label("Appointment ID:");
            TextField appointmentIDField = new TextField(selectedAppointment.getAppointmentID());
            appointmentIDField.setDisable(true);
            Label patientIDLabel = new Label("Patient ID:");
            TextField patientIDField = new TextField(selectedAppointment.getPatientID());
            Label doctorIDLabel = new Label("Doctor ID:");
            TextField doctorIDField = new TextField(selectedAppointment.getDoctorID()); // Change to String
            Label dateLabel = new Label("Date:");
            TextField dateField = new TextField(selectedAppointment.getDate());
            Label timeLabel = new Label("Time:");
            TextField timeField = new TextField(selectedAppointment.getTime());
            Label diagnosisLabel = new Label("Diagnosis:");
            TextField diagnosisField = new TextField(selectedAppointment.getDiagnosis());
            Label followUpDateLabel = new Label("Follow Up Date:");
            TextField followUpDateField = new TextField(selectedAppointment.getFollowUpDate());
            Label roomLabel = new Label("Room:");
            TextField roomField = new TextField(selectedAppointment.getRoom());

            // Save button
            Button saveBtn = new Button("Save");
            saveBtn.setOnAction(e -> {
                selectedAppointment.setPatientID(patientIDField.getText());
                selectedAppointment.setDoctorID(doctorIDField.getText()); // Change to String
                selectedAppointment.setDate(dateField.getText());
                selectedAppointment.setTime(timeField.getText());
                selectedAppointment.setDiagnosis(diagnosisField.getText());
                selectedAppointment.setFollowUpDate(followUpDateField.getText());
                selectedAppointment.setRoom(roomField.getText());

                // Confirm dialog
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation Dialog");
                confirmationAlert.setHeaderText("Update Appointment");
                confirmationAlert.setContentText("Are you sure you want to update the appointment information?");

                if (confirmationAlert.showAndWait().get() == ButtonType.OK) {
                    updateAppointmentInDatabase(selectedAppointment);
                    updateStage.close();
                }
            });

            grid.add(appointmentIDLabel, 0, 0);
            grid.add(appointmentIDField, 1, 0);
            grid.add(patientIDLabel, 0, 1);
            grid.add(patientIDField, 1, 1);
            grid.add(doctorIDLabel, 0, 2);
            grid.add(doctorIDField, 1, 2);
            grid.add(dateLabel, 0, 3);
            grid.add(dateField, 1, 3);
            grid.add(timeLabel, 0, 4);
            grid.add(timeField, 1, 4);
            grid.add(diagnosisLabel, 0, 5);
            grid.add(diagnosisField, 1, 5);
            grid.add(followUpDateLabel, 0, 6);
            grid.add(followUpDateField, 1, 6);
            grid.add(roomLabel, 0, 7);
            grid.add(roomField, 1, 7);
            grid.add(saveBtn, 1, 8);

            Scene scene = new Scene(grid, 400, 500);
            updateStage.setScene(scene);
            updateStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment in the table.");
            alert.showAndWait();
        }
    }

    // Update appointment information in the database
    private void updateAppointmentInDatabase(AppointmentClass appointment) {
        String sql = "UPDATE Appointments SET patientID = ?, doctorID = ?, date = ?, time = ?, diagnosis = ?, followUpDate = ?, room = ? WHERE appointmentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, appointment.getPatientID());
            pstmt.setString(2, appointment.getDoctorID()); // Change to String
            pstmt.setString(3, appointment.getDate());
            pstmt.setString(4, appointment.getTime());
            pstmt.setString(5, appointment.getDiagnosis());
            pstmt.setString(6, appointment.getFollowUpDate());
            pstmt.setString(7, appointment.getRoom());
            pstmt.setString(8, appointment.getAppointmentID());
            pstmt.executeUpdate();

            // Refresh the table view
            tableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Confirm and delete appointment
    private void confirmAndDeleteAppointment() {
        AppointmentClass selectedAppointment = tableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Appointment");
            alert.setContentText("Are you sure you want to delete this appointment?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                deleteAppointment(selectedAppointment);
            }
        } else {
            System.out.println("No appointment selected.");
        }
    }

    // Delete appointment
    private void deleteAppointment(AppointmentClass appointment) {
        String sql = "DELETE FROM Appointments WHERE appointmentID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, appointment.getAppointmentID());
            pstmt.executeUpdate();
            appointmentList.remove(appointment); 
            System.out.println("Deleted appointment: " + appointment.getAppointmentID());

            // Insert log entry
            insertLog("Deleted appointment: " + appointment.getAppointmentID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertLog(String action) {
        String sql = "INSERT INTO Logs (action) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // back to admin dashboard
    private void goBack(Stage primaryStage) {
        primaryStage.close(); 
        AdminDashboard adminDashboard = new AdminDashboard();
        try {
            adminDashboard.start(new Stage()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}