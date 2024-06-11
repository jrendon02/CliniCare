/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicaremangementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Joel
 */
    public class LoginClass {
    private String userID;
    private String password;
    private PatientClass patientDetails;  // Composition

    public LoginClass(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }
    
    //inherit patient class
    public LoginClass(String userID, String password, PatientClass patientDetails) {
        this(userID, password);  
        this.patientDetails = patientDetails;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public PatientClass getPatientDetails() {
        return patientDetails;
    }

    public boolean getLogin() {
        // verify userID and password against database
        String sql = "SELECT * FROM login WHERE userID = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, this.userID);
            pstmt.setString(2, this.password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
    }

    public void getLogout() {
        
    }
}