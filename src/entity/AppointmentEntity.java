// Import necessary packages
package entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JavaDataBaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

// Define AppointmentEntity class
public class AppointmentEntity {
    // Define private instance variables
    private int appointmentId, customerId, userId;
    private String appointment_title, appointment_desc, ap_location, type;
    private LocalDateTime apstartDate;
    private LocalDateTime apEndDate;
    private String ContactName;

    // Define static ObservableLists for appointments and contacts
    public static ObservableList<AppointmentEntity> listAppointments = FXCollections.observableArrayList();
    public static ObservableList<AppointmentEntity> listContact = FXCollections.observableArrayList();

    // Define default constructor
    public AppointmentEntity() {
    }

    // Define parameterized constructor
    public AppointmentEntity(int appId, String appTitle, String appDesc, String location, String contactId, String type,
                             LocalDateTime start, LocalDateTime end, int customerId, int userId) {
        this.appointmentId = appId;
        this.appointment_title = appTitle;
        this.appointment_desc = appDesc;
        this.ap_location = location;
        this.type = type;
        this.apstartDate = start;
        this.apEndDate = end;
        this.customerId = customerId;
        this.userId = userId;
        this.ContactName = contactId;
    }

    // Define getters and setters for private instance variables
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return appointment_title;
    }

    public void setTitle(String title) {
        this.appointment_title = title;
    }

    public String getDescription() {
        return appointment_desc;
    }

    public void setDescription(String description) {
        this.appointment_desc = description;
    }

    public String getLocation() {
        return ap_location;
    }

    public void setLocation(String location) {
        this.ap_location = location;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getStart() {
        return apstartDate;
    }

    public void setStart(LocalDateTime start) {
        this.apstartDate = start;
    }

    public LocalDateTime getEnd() {
        return apEndDate;
    }

    public void setEnd(LocalDateTime end) {
        this.apEndDate = end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        this.ContactName = contactName;
    }

    public String getAppointment_title() {
        return appointment_title;
    }

    public void setAppointment_title(String appointment_title) {
        this.appointment_title = appointment_title;
    }

    public String getAppointment_desc() {
        return appointment_desc;
    }

    public void setAppointment_desc(String appointment_desc) {
        this.appointment_desc = appointment_desc;
    }

    public String getAp_location() {
        return ap_location;
    }

    public void setAp_location(String ap_location) {
        this.ap_location = ap_location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getApstartDate() {
        return apstartDate;
    }

    public void setApstartDate(LocalDateTime apstartDate) {
        this.apstartDate = apstartDate;
    }

    public LocalDateTime getApEndDate() {
        return apEndDate;
    }

    public void setApEndDate(LocalDateTime apEndDate) {
        this.apEndDate = apEndDate;
    }

    // Define method to fetch appointments from database
    public static ObservableList<AppointmentEntity> fetchAppointments() throws SQLException {

        // Create a statement
        Statement statement = JavaDataBaseConnection.getConnection().createStatement();

        // Define query to join appointments table and contacts table
        String query = "SELECT appointments.*, contacts.* FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID";

        // Execute query and store result in ResultSet
        ResultSet appRst = statement.executeQuery(query);

        // Loop through result set and create AppointmentEntity objects, adding them to
        // the list of appointments
        while (appRst.next()) {

            AppointmentEntity appointments = new AppointmentEntity(appRst.getInt("Appointment_ID"),
                    appRst.getString("Title"), appRst.getString("Description"), appRst.getString("Location"),
                    appRst.getString("Contact_Name"), appRst.getString("Type"),
                    appRst.getTimestamp("Start").toLocalDateTime(), appRst.getTimestamp("End").toLocalDateTime(),
                    appRst.getInt("Customer_ID"), appRst.getInt("User_ID"));
            listAppointments.add(appointments);
        }
        return listAppointments;
    }
}
