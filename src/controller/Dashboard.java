// Import required packages
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.JavaDataBaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;

public class Dashboard implements Initializable {

    // Declare required buttons
    public Button customersBtn, appointmentsBtn, reportsBtn, logoutBtn, exitBtn;

    Parent scene; // Parent object to hold scene
    Stage dashPage; // Stage object to hold main screen

    /*** Function to switch between screens based on the input
     *
     * @param event
     * @param resString
     * @throws IOException
     */
    public void switchScreen(ActionEvent event, String resString) throws IOException {
        // Get the current stage from the event
        dashPage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load the specified FXML file
        scene = FXMLLoader.load(getClass().getResource(resString));
        // Set the new scene
        dashPage.setScene(new Scene(scene));
        dashPage.centerOnScreen();
        dashPage.show();
    }

    /*** Function to switch to the customers view
     *
     * @param event
     * @throws IOException
     */
    public void onClickCustomers(ActionEvent event) throws IOException {
        switchScreen(event, "/view/Customers.fxml");
    }

    /***This is function to switch to the appointments view
     *
     * @param event
     * @throws IOException
     */
    public void onClickAppointments(ActionEvent event) throws IOException {
        switchScreen(event, "/view/Appointments.fxml");
    }

    public void onClickReports(ActionEvent event) throws IOException {
        switchScreen(event, "/view/Reports.fxml");
    }

    /***
     * clicklogout is the method to exit when clicked.
     * @param event
     * @throws IOException
     */

    public void onClickLogOut(ActionEvent event) throws IOException {
        switchScreen(event, "/view/Login.fxml");
    }

    /***
     * onexit is the method to exit the system.
     * @param event
     */
    public void onExit(ActionEvent event) {
        System.exit(0);
    }

    @Override

    /***
     * uses the controller to intialize data.
     */
    public void initialize(URL url, ResourceBundle resBundle) {
        try {
            fetchAppointmentsWithin15Mins(); // Check for upcoming appointments
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /***
     * Method used to get the upcoming methods in 15mins.
     * @throws SQLException
     */
    public void fetchAppointmentsWithin15Mins() throws SQLException {
        // Get current time and time 15 minutes from now
        LocalDateTime lstarttime = LocalDateTime.now();
        LocalDateTime lendTime = LocalDateTime.now().plusMinutes(15);
        Timestamp now = Timestamp.valueOf(lstarttime);
        Timestamp end = Timestamp.valueOf(lendTime);
        showAppointmentReminder(now, end); // Display alert for upcoming appointments
    }

    /***
     * method used to show a reminder for when the appointment is happening.
     * @param now
     * @param end
     * @throws SQLException
     */
    public void showAppointmentReminder(Timestamp now, Timestamp end) throws SQLException {

        // Prepare SQL query to get appointments within the specified time range
        PreparedStatement pstapp = JavaDataBaseConnection.getConnection()
                .prepareStatement("SELECT * " + "FROM appointments "
                        + "INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID "
                        + "WHERE Start BETWEEN ? AND ?");

        pstapp.setTimestamp(1, now); // Set start time parameter
        pstapp.setTimestamp(2, end); // Set end time parameter

        ResultSet rst = pstapp.executeQuery(); // Execute the query

        if (rst.next()) { // If there are upcoming appointments
            // Display an alert with appointment details
            DisplayAlertMessage.informationAlert("Appointment Reminder",
                    ("Appointment ID = " + rst.getInt(("Appointment_ID")) + " is within 15 minutes"),
                    ("You have an upcoming appointment with " + rst.getString("Contact_Name") + " and is a "
                            + rst.getString("Type") + " meeting. It starts at "
                            + rst.getTimestamp("Start").toLocalDateTime() + "."));
        } else {
            DisplayAlertMessage.informationAlert("Appointment Reminder", "No appointments in the next 15 minutes", "");
        }
    }
}
