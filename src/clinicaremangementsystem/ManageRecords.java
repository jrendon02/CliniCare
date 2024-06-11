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
public class ManageRecords extends Application {
    
    private TableView<PatientClass> tableView;
    private ObservableList<PatientClass> patientList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manage Patient Records");

        // Create the table view and columns
        tableView = new TableView<>();
        TableColumn<PatientClass, String> idColumn = new TableColumn<>("Patient ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));

        TableColumn<PatientClass, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PatientClass, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<PatientClass, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<PatientClass, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<PatientClass, String> cityColumn = new TableColumn<>("City");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        TableColumn<PatientClass, String> stateColumn = new TableColumn<>("State");
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

        TableColumn<PatientClass, String> zipCodeColumn = new TableColumn<>("Zip Code");
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipcode"));

        TableColumn<PatientClass, String> phoneNumberColumn = new TableColumn<>("Phone Number");
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<PatientClass, String> medicalHistoryColumn = new TableColumn<>("Medical History");
        medicalHistoryColumn.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));

        tableView.getColumns().addAll(idColumn, nameColumn, emailColumn, ageColumn, addressColumn, cityColumn, stateColumn, zipCodeColumn, phoneNumberColumn, medicalHistoryColumn);

        // Load data into the table
        patientList = FXCollections.observableArrayList();
        loadPatientData();

        tableView.setItems(patientList);

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search by Name");
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterPatientList(newValue));

        // Buttons for update, delete, and back
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button backBtn = new Button("Back");

        // Update button action
        updateBtn.setOnAction(e -> openUpdatePatientWindow());

        // Delete button action
        deleteBtn.setOnAction(e -> confirmAndDeletePatient());

        // Back button action
        backBtn.setOnAction(e -> goBack(primaryStage));

        // Layout for buttons
        HBox buttonBox = new HBox(10, updateBtn, deleteBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        // Main layout
        VBox layout = new VBox(10, searchBar, tableView, buttonBox);
        layout.setPadding(new Insets(10));

        // Create the scene and set the stage
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Load patient data from the database
    private void loadPatientData() {
        String sql = "SELECT * FROM Patients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet results = pstmt.executeQuery()) {

            while (results.next()) {
                patientList.add(new PatientClass(
                        results.getString("patientID"),
                        results.getString("name"),
                        results.getInt("age"),
                        results.getString("address"),
                        results.getString("city"),
                        results.getString("state"),
                        results.getString("zipcode"),
                        results.getString("phoneNumber"),
                        results.getString("email"),
                        results.getString("medicalHistory") 
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Filter the patient list based on search input
    private void filterPatientList(String searchText) {
        ObservableList<PatientClass> filteredList = FXCollections.observableArrayList();
        for (PatientClass patient : patientList) {
            if (patient.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(patient);
            }
        }
        tableView.setItems(filteredList);
    }

    // Open the update patient window
    private void openUpdatePatientWindow() {
        PatientClass selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            Stage updateStage = new Stage();
            updateStage.setTitle("Update Patient");

            GridPane grid = new GridPane();
            grid.setPadding(new Insets(10));
            grid.setVgap(10);
            grid.setHgap(10);
            grid.setAlignment(Pos.CENTER);

            // Labels and text fields for patient details
            Label idLabel = new Label("Patient ID:");
            TextField idField = new TextField(selectedPatient.getPatientID());
            idField.setDisable(true);
            Label nameLabel = new Label("Name:");
            TextField nameField = new TextField(selectedPatient.getName());
            Label ageLabel = new Label("Age:");
            TextField ageField = new TextField(String.valueOf(selectedPatient.getAge()));
            Label addressLabel = new Label("Address:");
            TextField addressField = new TextField(selectedPatient.getAddress());
            Label cityLabel = new Label("City:");
            TextField cityField = new TextField(selectedPatient.getCity());
            Label stateLabel = new Label("State:");
            TextField stateField = new TextField(selectedPatient.getState());
            Label zipCodeLabel = new Label("Zip Code:");
            TextField zipCodeField = new TextField(selectedPatient.getZipcode());
            Label phoneNumberLabel = new Label("Phone Number:");
            TextField phoneNumberField = new TextField(selectedPatient.getPhoneNumber());
            Label emailLabel = new Label("Email:");
            TextField emailField = new TextField(selectedPatient.getEmail());
            Label medicalHistoryLabel = new Label("Medical History:");
            TextField medicalHistoryField = new TextField(selectedPatient.getMedicalHistory());

            // Save button
            Button saveBtn = new Button("Save");
            saveBtn.setOnAction(e -> {
                selectedPatient.setName(nameField.getText());
                selectedPatient.setAge(Integer.parseInt(ageField.getText()));
                selectedPatient.setAddress(addressField.getText());
                selectedPatient.setCity(cityField.getText());
                selectedPatient.setState(stateField.getText());
                selectedPatient.setZipcode(zipCodeField.getText());
                selectedPatient.setPhoneNumber(phoneNumberField.getText());
                selectedPatient.setEmail(emailField.getText());
                selectedPatient.setMedicalHistory(medicalHistoryField.getText());

                // Confirmation dialog
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation Dialog");
                confirmationAlert.setHeaderText("Update Patient");
                confirmationAlert.setContentText("Are you sure you want to update the patient information?");

                if (confirmationAlert.showAndWait().get() == ButtonType.OK) {
                    updatePatientInDatabase(selectedPatient);
                    updateStage.close();
                }
            });

            grid.add(idLabel, 0, 0);
            grid.add(idField, 1, 0);
            grid.add(nameLabel, 0, 1);
            grid.add(nameField, 1, 1);
            grid.add(ageLabel, 0, 2);
            grid.add(ageField, 1, 2);
            grid.add(addressLabel, 0, 3);
            grid.add(addressField, 1, 3);
            grid.add(cityLabel, 0, 4);
            grid.add(cityField, 1, 4);
            grid.add(stateLabel, 0, 5);
            grid.add(stateField, 1, 5);
            grid.add(zipCodeLabel, 0, 6);
            grid.add(zipCodeField, 1, 6);
            grid.add(phoneNumberLabel, 0, 7);
            grid.add(phoneNumberField, 1, 7);
            grid.add(emailLabel, 0, 8);
            grid.add(emailField, 1, 8);
            grid.add(medicalHistoryLabel, 0, 9);
            grid.add(medicalHistoryField, 1, 9);
            grid.add(saveBtn, 1, 10);

            Scene scene = new Scene(grid, 400, 500);
            updateStage.setScene(scene);
            updateStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Patient Selected");
            alert.setContentText("Please select a patient in the table.");
            alert.showAndWait();
        }
    }

    // Update patient information in the database
    private void updatePatientInDatabase(PatientClass patient) {
        String sql = "UPDATE Patients SET name = ?, age = ?, address = ?, city = ?, state = ?, zipcode = ?, phoneNumber = ?, email = ?, medicalHistory = ? WHERE patientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getAddress());
            pstmt.setString(4, patient.getCity());
            pstmt.setString(5, patient.getState());
            pstmt.setString(6, patient.getZipcode());
            pstmt.setString(7, patient.getPhoneNumber());
            pstmt.setString(8, patient.getEmail());
            pstmt.setString(9, patient.getMedicalHistory());
            pstmt.setString(10, patient.getPatientID());
            pstmt.executeUpdate();

            // Refresh the table view
            tableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Confirm and delete patient
    private void confirmAndDeletePatient() {
        PatientClass selectedPatient = tableView.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Delete Patient");
            alert.setContentText("Are you sure you want to delete patient " + selectedPatient.getName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                deletePatient(selectedPatient);
            }
        } else {
            System.out.println("No patient selected.");
        }
    }

    // Delete patient
    private void deletePatient(PatientClass patient) {
        String sql = "DELETE FROM Patients WHERE patientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getPatientID());
            pstmt.executeUpdate();
            patientList.remove(patient); 
            System.out.println("Deleted patient: " + patient.getName());

            // Insert log entry
            insertLog("Deleted patient: " + patient.getName());
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

    // back to the admin dashboard
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