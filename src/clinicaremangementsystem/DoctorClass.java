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
public class DoctorClass {
    public class Doctor {
    private String doctorID;
    private String name;
    private String availableTimes; 

    public Doctor(String doctorID, String name) {
        this.doctorID = doctorID;
        this.name = name;
        this.availableTimes = ""; 
    }

    // Retrieves  doctor's schedule
    public String getSchedule() {
        return availableTimes.isEmpty() ? "No available times set." : availableTimes;
    }

    // Sets the doctor's availability for a specific time
    public void setAvailability(String time, boolean isAvailable) {
        availableTimes += time + ": " + (isAvailable ? "Available" : "Not Available") + "\n";
    }

    // Assigns a patient to the doctor
    public String assignPatient(String patientID) {
        return "Patient with ID " + patientID + " assigned to Doctor " + name;
    }

    // Getters and setters
    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
}
