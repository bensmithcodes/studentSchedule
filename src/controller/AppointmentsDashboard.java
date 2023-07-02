package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.SQLQueries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;
import entity.AppointmentEntity;

/**
 * This class represents the controller for the Appointments page. It handles all
 * the user events that occur in the Appointments page.
 */
public class AppointmentsDashboard implements Initializable {

    // Button to return to the main menu
    public Button btnDash;

    // Table view to display appointments
    public TableView<AppointmentEntity> apTbl;

    // Radio button to display all appointments
    public RadioButton allRadio;

    // Radio button to filLbl appointments by month
    public RadioButton monRadio;

    // Radio button to filLbl appointments by week
    public RadioButton weekRadio;

    // Toggle group for the radio buttons
    public ToggleGroup reportGroup;

    // Button to add a new appointment
    public Button btncreateApp;

    // Button to delete an appointment
    public Button btnremoveApp;

    // Button to modify an appointment
    public Button btnModifyApp;

    // Table column for appointment ID
    public TableColumn<AppointmentEntity, Integer> apIdCol;

    // Table column for appointment title
    public TableColumn<AppointmentEntity, String> apTitCol;

    // Table column for appointment description
    public TableColumn<AppointmentEntity, String> apDescCol;

    // Table column for appointment location
    public TableColumn<AppointmentEntity, String> apLocCol;

    // Table column for appointment contact name
    public TableColumn<AppointmentEntity, String> apContactCol;

    // Table column for appointment type
    public TableColumn<AppointmentEntity, String> apTypeCol;

    // Table column for appointment start time
    public TableColumn<AppointmentEntity, LocalDateTime> apStartTimeCol;

    // Table column for appointment end time
    public TableColumn<AppointmentEntity, LocalDateTime> apEndTimeCol;

    // Table column for appointment customer ID
    public TableColumn<AppointmentEntity, Integer> apCustomerIdCol;

    // Table column for appointment user ID
    public TableColumn<AppointmentEntity, Integer> apUserIdCol;


    // List of appointments filtered by month
    private ObservableList<AppointmentEntity> filterByMonth = FXCollections.observableArrayList();

    // List of appointments filtered by week
    private ObservableList<AppointmentEntity> filterByWeek = FXCollections.observableArrayList();

    // The currently selected appointment
    public static AppointmentEntity selectedApp;

    // The scene for the Appointments page
    Parent scene;

    // The stage for the Appointments page
    Stage stageAppointment;

    private ToggleGroup toggleGroup;

    /**
     * This method returns the currently selected appointment.
     *
     * @return the currently selected appointment
     */
    public static AppointmentEntity getCurrentlySelectedAppointment() {
        return selectedApp;
    }

    /**
     * This method switches to a new scene.
     *
     * @param actionEvent the event that triggers the scene switch
     * @param resBundle   the location of the new scene
     * @throws IOException if an IO exception occurs
     */


    public void changeWindow(ActionEvent actionEvent, String resBundle) throws IOException {
        stageAppointment = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resBundle));
        stageAppointment.setScene(new Scene(scene));
        stageAppointment.centerOnScreen();
        stageAppointment.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resBundle) {

        try {
            getAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        apIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        apTitCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apContactCol
                .setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContactName()));
        apTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        toggleGroup = new ToggleGroup();

        allRadio.setToggleGroup(toggleGroup);
        monRadio.setToggleGroup(toggleGroup);
        weekRadio.setToggleGroup(toggleGroup);

    }


    public void onDashboard(ActionEvent event) throws IOException {
        changeWindow(event, "/view/Dashboard.fxml");
    }

    public void onScheduleAppointment(ActionEvent event) throws IOException {
        changeWindow(event, "/view/AddAppointments.fxml");
    }

    public void onDeleteAppointment(ActionEvent event) throws SQLException, IOException {

        AppointmentEntity apSelected = apTbl.getSelectionModel().getSelectedItem();

        if (apSelected == null) {
            DisplayAlertMessage.alertDisplays(9);
        } else {
            Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertDelete.setHeaderText("Confirm. Delete Appointment?");
            alertDelete.setContentText("Press OK to delete the appointment");
            Optional<ButtonType> selectOpt = alertDelete.showAndWait();

            if (selectOpt.isPresent() && selectOpt.get() == ButtonType.OK) {

                SQLQueries.deleteFromAppointmentsTable(apSelected.getAppointmentId());

                DisplayAlertMessage.informationAlert("Appointment Cancelled",
                        "Appointment ID was " + apSelected.getAppointmentId(),
                        "It was a " + apSelected.getType() + " meeting with "
                                + apSelected.getContactName());

                changeWindow(event, "/view/Appointments.fxml");
            }
        }
    }

    /***
     *  onupdate appointment method used to update the information of the appointment.
     * @param event
     * @throws IOException
     */
    public void onUpdateAppointment(ActionEvent event) throws IOException {

        selectedApp = apTbl.getSelectionModel().getSelectedItem();

        if (selectedApp == null) {
            Alert updateAlert = new Alert(Alert.AlertType.ERROR);
            updateAlert.setHeaderText("No appointment highlighted");
            updateAlert.setContentText("Please select an appointment to modify");
            updateAlert.showAndWait();
        } else {
            changeWindow(event, "/view/ModifyAppointment.fxml");
        }
    }

    /***
     * getmonths as integer method to switch the names of month into numbers of integers.
     * @param month
     * @return
     */
    public int getMonthAsInteger(String month) {
        int monthId;
        switch (month) {
            case "FEBRUARY":
                monthId = 2;
                break;
            case "MARCH":
                monthId = 3;
                break;
            case "APRIL":
                monthId = 4;
                break;
            case "MAY":
                monthId = 5;
                break;
            case "JUNE":
                monthId = 6;
                break;
            case "JULY":
                monthId = 7;
                break;
            case "AUGUST":
                monthId = 8;
                break;
            case "SEPTEMBER":
                monthId = 9;
                break;
            case "OCTOBER":
                monthId = 10;
                break;
            case "NOVEMBER":
                monthId = 11;
                break;
            case "DECEMBER":
                monthId = 12;
                break;
            default:
                monthId = 1;
        }
        return monthId;
    }


    /***
     * onAppointmentdashboard used to view the appointments on screen.
     * @param event
     * @throws IOException
     */
    public void onAppointmentsDashboard(ActionEvent event) throws IOException {
        changeWindow(event, "/view/Appointments.fxml");
    }

    public void onDefaultScreen(ActionEvent event) throws IOException {
        onAppointmentsDashboard(event);
    }

    public void searchAppointments(ActionEvent event) throws SQLException {

        getAppointments();


    }

    private void getAppointments() throws SQLException {


        if (monRadio.isSelected()) {
            apTbl.setItems(FXCollections.observableList(SQLQueries.searchAppointments(false, LocalDateTime.now().getMonth().toString().toUpperCase(), null, null, null)));

        } else if (weekRadio.isSelected()) {
            apTbl.setItems(FXCollections.observableList(SQLQueries.searchAppointments(true, null, null, null, null)));

        } else {
            apTbl.setItems(FXCollections.observableList(SQLQueries.searchAppointments(false, null, null, null, null)));

        }
    }

}
