package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.SQLQueries;
import util.*;
import util.BuisnessHoursTime;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;

public class CreateAppointment implements Initializable {
    @FXML
    public Button backBtn, btnDash, btnAddApp;
    @FXML
    public TextField cusIdTxt, apTitleTxt;
    @FXML
    public ComboBox<LocalTime> comboEndTime;
    @FXML
    public ComboBox<LocalTime> comboStartTime;
    @FXML
    public DatePicker pickDate;
    @FXML
    public ComboBox<String> comboCustomer;
    @FXML
    public TextField descriptionTxt;
    @FXML
    public ComboBox<String> comboContact;
    @FXML
    public ComboBox<Integer> comboUserId;
    @FXML
    public TextField locationTxt;
    @FXML
    public TextField cusIdtxt;
    @FXML
    public ComboBox<String> comboApType;
    private int contact_id;

    public int obtainContactId() {
        return contact_id;
    }

    public void setContactId(int contactId) {
        this.contact_id = contactId;
    }

    public static ObservableList<String> existingCustomers = FXCollections.observableArrayList();
    public static ObservableList<String> existingContactNames = FXCollections.observableArrayList();
    public static ObservableList<Integer> existingUsersList = FXCollections.observableArrayList();
    private ObservableList<String> typeAppointmentList = FXCollections.observableArrayList("Doctor", "Class",
            "Exam");


    Parent scene;
    Stage createAppStage;

    /***
     * initialize method to connect to the database, and to get information such as contanct name and ID #.
     * @param url
     * @param resBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resBundle) {

        existingCustomers.clear();
        existingContactNames.clear();
        existingUsersList.clear();

        BuisnessHoursTime timestart = new BuisnessHoursTime();


        comboStartTime.setItems(timestart.generateTimeList());
        comboStartTime.getSelectionModel().selectFirst();

        BuisnessHoursTime timeEnd = new BuisnessHoursTime();
        ObservableList<LocalTime> endTimeList = timeEnd.generateTimeList();
        //endTimeList.add(LocalTime.of(0, 0));

        comboEndTime.setItems(endTimeList);
        comboEndTime.getSelectionModel().selectFirst();

        comboApType.setItems(typeAppointmentList);

        try {
            Statement javCon = JavaDataBaseConnection.getConnection().createStatement();
            String customerQuery = "SELECT * FROM customers";
            ResultSet rst = javCon.executeQuery(customerQuery);

            while (rst.next()) {
                existingCustomers.add(rst.getString("Customer_Name"));
                comboCustomer.setItems(existingCustomers);
            }
            javCon.close();

            Statement conn = JavaDataBaseConnection.getConnection().createStatement();
            String querycontacts = "SELECT * FROM contacts";
            ResultSet conrst = conn.executeQuery(querycontacts);

            while (conrst.next()) {
                existingContactNames.add(conrst.getString("Contact_Name"));
                comboContact.setItems(existingContactNames);
            }
            conn.close();

            existingUsersList.clear();

            Statement connn = JavaDataBaseConnection.getConnection().createStatement();
            String queryusers = "SELECT * FROM users";
            ResultSet usersrst = connn.executeQuery(queryusers);

            while (usersrst.next()) {

                existingUsersList.add(usersrst.getInt("User_ID"));
                comboUserId.setItems(existingUsersList);
            }
            connn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /***
     * this function is used to go to the previous section of the views.
     * @param event
     * @throws IOException
     */
    @FXML
    public void onBackButton(ActionEvent event) throws IOException {
        Alert dalert = new Alert(Alert.AlertType.CONFIRMATION);
        dalert.setTitle("Cancel");
        dalert.setHeaderText("Want To Go Back?");
        dalert.setContentText("your data will be lost, and will clear the text");
        Optional<ButtonType> selOpt = dalert.showAndWait();

        if (selOpt.isPresent() && selOpt.get() == ButtonType.OK) {
            changeWindow(event, "/view/Appointments.fxml");
        }
    }

    @FXML
    public void onDashboard(ActionEvent event) throws IOException {
        Alert dAlert = new Alert(Alert.AlertType.CONFIRMATION);
        dAlert.setTitle("Cancel");
        dAlert.setHeaderText("Confirm Go to dashboard?");
        dAlert.setContentText("This will clear all text fields and your data will be lost");
        Optional<ButtonType> selOpt = dAlert.showAndWait();

        if (selOpt.isPresent() && selOpt.get() == ButtonType.OK) {
            changeWindow(event, "/view/Dashboard.fxml");
        }
    }

    /***
     * this method is used to check the entered appointment times. boolean statements used to see if it is conflicting with any other.
     * @param timeStart
     * @param timeEnd
     * @return
     */
    public boolean verifyAppointmentTime(Timestamp timeStart, Timestamp timeEnd) {

        boolean isEndsBeforeStarts = timeEnd.before(timeStart);
        boolean areEndsStartsSame = timeEnd.equals(timeStart);

        if (isEndsBeforeStarts) {
            DisplayAlertMessage.alertDisplays(24);
            return false;
        } else if (areEndsStartsSame) {
            DisplayAlertMessage.alertDisplays(25);
            return false;
        }

        ZonedDateTime syszoneTime = ZonedDateTime.of(timeStart.toLocalDateTime(), ZoneId.systemDefault());
        ZonedDateTime endzoneTime = ZonedDateTime.of(timeEnd.toLocalDateTime(), ZoneId.systemDefault());

        ZonedDateTime utcZoneStartTime = ZonedDateTime.ofInstant(syszoneTime.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime utcZoneEndTime = ZonedDateTime.ofInstant(endzoneTime.toInstant(), ZoneId.of("UTC"));

        Timestamp utcStartTime = Timestamp.valueOf(utcZoneStartTime.toLocalDateTime());
        Timestamp utcEndTime = Timestamp.valueOf(utcZoneEndTime.toLocalDateTime());

        try {
            Statement con = JavaDataBaseConnection.getConnection().createStatement();

            String sqlVAppointments = "SELECT * " + "FROM scheduleapp.appointments " + "WHERE ('" + syszoneTime
                    + "' BETWEEN Start AND End " + "OR '" + endzoneTime + "' BETWEEN Start AND End)";

            ResultSet rst = con.executeQuery(sqlVAppointments);

            if (rst.next()) {
                DisplayAlertMessage.alertDisplays(26);
                return false;
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return true;
    }

    /***
     * this method is used to add the appointment if already doesnt exist in database.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onAddButton(ActionEvent event) throws IOException, SQLException {

        try {
            String tit = apTitleTxt.getText();
            String desc = descriptionTxt.getText();
            String loc = locationTxt.getText();
            int contactId = contact_id;
            String apType = comboApType.getSelectionModel().getSelectedItem();
            int customerId = Integer.parseInt(cusIdtxt.getText());
            int uId = comboUserId.getSelectionModel().getSelectedItem();
            LocalDate date = pickDate.getValue();
            LocalTime startTime = comboStartTime.getSelectionModel().getSelectedItem();
            LocalTime endTime = comboEndTime.getSelectionModel().getSelectedItem();
            Timestamp timestampStartTime = Timestamp.valueOf(LocalDateTime.of(date, startTime));
            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(date, endTime));

            boolean validHours = BuisnessHoursTime.validateBuisnessHours(date, startTime, endTime, ZoneId.systemDefault());

            if (checkTitleisNotEmpty(tit) && checkDescriptionisNotEmpty(desc)
                    && checkTypeisNotEmpty(apType) && checkLocationisNotEmpty(loc)
                    && checkStartTimeisNotEmpty(timestampStartTime) && checkEndTime(endTimestamp)
                    && checkDateIsNotEmpty(date) && checkCustomerIdisNotEmpty(customerId)
                    && checkContactisNotEmpty(contact_id) && checkUserIdisNotEmpty(uId)
                    && verifyAppointmentTime(timestampStartTime, endTimestamp) && !validHours) {

                SQLQueries.insertAppointment(tit, desc, loc, apType, timestampStartTime, endTimestamp,
                        customerId, uId, contactId);
                DisplayAlertMessage.alertDisplays(23);
                changeWindow(event, "/view/Appointments.fxml");
            }

        } catch (Exception ex) {
            if (cusIdtxt.getText() == null) {
                DisplayAlertMessage.alertDisplays(20);
            } else if (comboUserId.getSelectionModel().getSelectedItem() == null) {
                DisplayAlertMessage.alertDisplays(21);
            } else if (pickDate.getValue() == null) {
                DisplayAlertMessage.alertDisplays(17);
            } else if (comboStartTime.getSelectionModel().getSelectedItem() == null) {
                DisplayAlertMessage.alertDisplays(18);
            } else if (comboEndTime.getValue() == null) {
                DisplayAlertMessage.alertDisplays(19);
            } else if (comboCustomer.getValue() == null) {
                DisplayAlertMessage.alertDisplays(20);
            }
        }
    }

    /***
     * onselect customer gets the information from database about the customer/ the name and id
     * @param event
     * @throws SQLException
     */
    @FXML
    public void onSelectCustomer(ActionEvent event) throws SQLException {
        String cname = comboCustomer.getSelectionModel().getSelectedItem();
        Statement conn = JavaDataBaseConnection.getConnection().createStatement();
        String custSql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + cname + "'";
        ResultSet rst = conn.executeQuery(custSql);

        while (rst.next()) {
            cusIdtxt.setText(String.valueOf(rst.getInt("Customer_ID")));
        }
        conn.close();
    }

    public void changeWindow(ActionEvent event, String resourcesString) throws IOException {
        createAppStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resourcesString));
        createAppStage.setScene(new Scene(scene));
        createAppStage.show();
    }

    /***
     * onselect contact gets the information about the contact, the name and id.
     * @param event
     * @throws SQLException
     */
    @FXML
    public void onSelectContact(ActionEvent event) throws SQLException {
        String conName = comboContact.getSelectionModel().getSelectedItem();

        Statement con = JavaDataBaseConnection.getConnection().createStatement();
        String contSql = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + conName + "'";
        ResultSet rst = con.executeQuery(contSql);

        while (rst.next()) {
            int conId = rst.getInt("Contact_ID");
            setContactId(conId);
        }
        con.close();
    }

    /***
     * method that is used to see if the title is not null, and to return false if so.
     * @param title
     * @return
     */
    public boolean checkTitleisNotEmpty(String title) {
        if (apTitleTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(13);
            return false;
        }
        return true;
    }

    /***
     * method that is used to see if description is not null, and to return false if so.
     * @param description
     * @return
     */
    public boolean checkDescriptionisNotEmpty(String description) {
        if (descriptionTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(14);
            return false;
        }
        return true;
    }

    /***
     * method that is used to determine if the type is not null
     * @param type
     * @return
     */
    public boolean checkTypeisNotEmpty(String type) {
        if (comboApType.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(16);
            return false;
        }
        return true;
    }

    /***
     * method that is used to determine if the location is not empty.
     * @param loc
     * @return
     */
    public boolean checkLocationisNotEmpty(String loc) {
        if (locationTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(15);
            return false;
        }
        return true;
    }

    public boolean checkStartTimeisNotEmpty(Timestamp start) {
        if (comboStartTime.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(18);
            return false;
        }
        return true;
    }

    /***
     * checkendtime method that is used to determine if it is not null.
     * @param endTime
     * @return
     */
    public boolean checkEndTime(Timestamp endTime) {
        if (comboEndTime.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(19);
            return false;
        }
        return true;
    }

    /***
     * method to verify if the date is null
     * @param date
     * @return
     */
    public boolean checkDateIsNotEmpty(LocalDate date) {
        if (pickDate.getValue() == null) {
            DisplayAlertMessage.alertDisplays(17);
            return false;
        }
        return true;
    }

    public boolean checkCustomerIdisNotEmpty(int customerId) {
        if (comboCustomer.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(20);
            return false;
        }
        return true;
    }

    /***
     * check user id method that is used to determine  if the id is not null.
     * @param uId
     * @return
     */
    public boolean checkUserIdisNotEmpty(int uId) {
        if (comboUserId.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(21);
            return false;
        }
        return true;
    }

    /***
     * method to determine if the contact is not empty.
     * @param contact
     * @return
     */
    public boolean checkContactisNotEmpty(int contact) {
        if (comboContact.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(22);
            return false;
        }
        return true;
    }
}