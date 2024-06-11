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
public class MedicalRecordsClass {
    private String recordID;
    private String patientID;
    private String doctorID;
    private String diagnosis;
    private String treatment;
    private String notes;
    
    // Constructor initializes the medical record.
    public MedicalRecordsClass(String recordID, String patientID, String doctorID,
                               String diagnosis, String treatment, String notes) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    // Method to get the medical record
    public String getRecord() {
        // Logic to return the medical record details
        return "RecordID: " + recordID + 
               ", PatientID: " + patientID + 
               ", DoctorID: " + doctorID + 
               ", Diagnosis: " + diagnosis + 
               ", Treatment: " + treatment + 
               ", Notes: " + notes;
    }

    //Updates the medical record with new details.
    public String updateRecord(String diagnosis, String treatment, String notes) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;

        
        return "Medical record updated for RecordID: " + recordID;
    }

    // Retrieves all medical records for a patient.
    public String retrieveRecords() {
        return getRecord();
    }

    
}
