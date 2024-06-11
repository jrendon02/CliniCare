package clinicaremangementsystem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joel
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientClass {
  //Atributes
  private String patientID;
  private String name;
  private int age;
  private String address;
  private String city;
  private String state;
  private String zipcode;
  private String phoneNumber;
  private String email;
  private String medicalHistory;

    // Constructs a new patient
    public PatientClass(String patientID, String name, int age, String address,
                        String city, String state, String zipcode, String phoneNumber,
                        String email, String medicalHistory) {
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.medicalHistory = medicalHistory;
    }

    // Getters and setters for all fields
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    // Saves  the patient data into the database
    public void save() {
        String sql = "INSERT INTO Patients (patientID, name, age, address, city, state, zipcode, phoneNumber, email, medicalHistory) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); 
            prepareStatement(pstmt);
            pstmt.executeUpdate();
            conn.commit(); 
        } catch (SQLException e) {
            System.out.println("Saving info failed: " + e.getMessage());
        }
    }

    private void prepareStatement(PreparedStatement pstmt) throws SQLException {
        // Set parameters 
        pstmt.setString(1, patientID);
        pstmt.setString(2, name);
        pstmt.setInt(3, age);
        pstmt.setString(4, address);
        pstmt.setString(5, city);
        pstmt.setString(6, state);
        pstmt.setString(7, zipcode);
        pstmt.setString(8, phoneNumber);
        pstmt.setString(9, email);
        pstmt.setString(10, medicalHistory);
    }

    // Updates patient's contact details to the database
    public void updateContactDetails(String address, String city, String state, String zipcode, String phoneNumber, String email) {
        String sql = "UPDATE Patients SET address = ?, city = ?, state = ?, zipcode = ?, phoneNumber = ?, email = ? WHERE patientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address);
            pstmt.setString(2, city);
            pstmt.setString(3, state);
            pstmt.setString(4, zipcode);
            pstmt.setString(5, phoneNumber);
            pstmt.setString(6, email);
            pstmt.setString(7, patientID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Updating contact details failed: " + e.getMessage());
        }
    }

    // Updates the patient's medical history
    public void updateMedicalHistory(String medicalHistory) {
        String sql = "UPDATE Patients SET medicalHistory = ? WHERE patientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, medicalHistory);
            pstmt.setString(2, patientID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Updating medical history failed: " + e.getMessage());
        }
    }
}