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
public class AdminClass {
    private String adminUserID;
    private String password;
    private LoginClass loginDetails;

    public AdminClass(String adminUserID, String password) {
        this.adminUserID = adminUserID;
        this.password = password;
        this.loginDetails = new LoginClass(adminUserID, password); 
    }
    
    // This will use the getLogin method from the LoginClass
    public boolean login() {
        return loginDetails.getLogin();
    }
    
    // This will use the getLogout method from the LoginClass
    public void logout() {
        loginDetails.getLogout();
    }
    
    //Implement log viewing functionality
    public String viewLogs() {
        return "Logs viewed";
    }
    
    // functionality to view all appointments
    public String viewAllAppointments() {
        return "All appointments viewed";
    }
    
    // update medical records
    public String updateMedicalRecords() {
        return "Medical records updated";
    }
    
    // resets an account
    public String resetAccount() {
        return "Account reset";
    }

    // Getters and setters for adminUserID and password
    public String getAdminUserID() {
        return adminUserID;
    }

    public void setAdminUserID(String adminUserID) {
        this.adminUserID = adminUserID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}