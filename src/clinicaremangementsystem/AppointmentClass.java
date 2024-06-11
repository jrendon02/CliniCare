package clinicaremangementsystem;

/**
 * @author Joel
 */
public class AppointmentClass {
    private String appointmentID;
    private String patientID;
    private String doctorID; // Change to String
    private String date; 
    private String time; 
    private String diagnosis;
    private String followUpDate; 
    private String room;

    // Constructor
    public AppointmentClass(String appointmentID, String patientID, String doctorID, // Change to String
                            String date, String time, String diagnosis,
                            String followUpDate, String room) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.diagnosis = diagnosis;
        this.followUpDate = followUpDate;
        this.room = room;
    }

    // Getters
    public String getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() { // Change to String
        return doctorID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public String getRoom() {
        return room;
    }

    // Setters
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setDoctorID(String doctorID) { // Change to String
        this.doctorID = doctorID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    // Method to reschedule the appointment
    public String reschedule(String newDate, String newTime) {
        setDate(newDate);
        setTime(newTime);
        return "Appointment rescheduled to " + newDate + " at " + newTime;
    }
}