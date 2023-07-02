package controller;

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
import util.JavaDataBaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;
import entity.AppointmentEntity;
import util.SQLQueries;

public class Reports implements Initializable {

    public Label filLbl;

    public Button btnDash;
    public ComboBox<String> comboMonth;
    public ComboBox<String> comboReportType;
    public TextField appNumberTxt;
    public TableView<AppointmentEntity> appointmentsTableView;

    public ComboBox<String> comboCustomers;

    public ComboBox<String> contactCombo;

    public int cusId;
    public TableColumn<AppointmentEntity, Integer> apIdCol;
    public TableColumn<AppointmentEntity, String> apTitleCol;
    public TableColumn<AppointmentEntity, Integer> userIdCol;
    public TableColumn<AppointmentEntity, Integer> cusIdCol;

    public TableColumn<AppointmentEntity, LocalDateTime> apEndTimeCol;

    public TableColumn<AppointmentEntity, LocalDateTime> apStartTimeCol;

    public TableColumn<AppointmentEntity, String> apContactCol;

    public TableColumn<AppointmentEntity, String> apTypeCol;
    public TableColumn<AppointmentEntity, String> aplocationCol;
    public TableColumn<AppointmentEntity, String> apDescCol;
    public Label aptotalByMonth;
    public Label customertotalAppointments;

    public Label lblTotalAppointments;

    private ObservableList<String> appTypes = FXCollections.observableArrayList("Doctor", "Class",
            "Exam");
    private ObservableList<String> customerList = FXCollections.observableArrayList();
    private ObservableList<String> contactList = FXCollections.observableArrayList();
    private ObservableList<AppointmentEntity> appointmentsList = FXCollections.observableArrayList();
    private ObservableList<String> months = FXCollections.observableArrayList("JANUARY", "FEBRUARY", "MARCH",
            "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
    Parent scene;
    Stage stageReports;

    /***
     * change window to go to different view.
     * @param event
     * @param resString
     * @throws IOException
     */
    public void changeWindow(ActionEvent event, String resString) throws IOException {
        stageReports = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resString));
        stageReports.setScene(new Scene(scene));
        stageReports.show();
    }

    public void onDashboard(ActionEvent event) throws IOException {
        changeWindow(event, "/view/Dashboard.fxml");
    }


    /***
     * initialize the custmers, contacts, from database.
     * @param url
     * @param resBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resBundle) {
        try {

            loadCustomers();
            loadContacts();
            setAppointmentTableData();
            apIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            apTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            apDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            aplocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            apContactCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
            apTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            apStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            apEndTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            cusIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        comboMonth.setItems(months);
        comboReportType.setItems(appTypes);

    }

    /***
     * method used to load the contacts from database.
     * @throws SQLException
     */
    public void loadContacts() throws SQLException {
        Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
        String consql = "SELECT * FROM contacts";
        ResultSet namesrst = stmnt.executeQuery(consql);

        while (namesrst.next()) {
            contactList.add(namesrst.getString("Contact_Name"));
            contactCombo.setItems(contactList);
        }
    }

    /***
     * method used to load the customers from the java database.
     * @throws SQLException
     */
    public void loadCustomers() throws SQLException {
        Statement cont = JavaDataBaseConnection.getConnection().createStatement();
        String query = "SELECT * FROM customers";
        ResultSet qrst = cont.executeQuery(query);
        while (qrst.next()) {
            customerList.add(qrst.getString("Customer_Name"));
            comboCustomers.setItems(customerList);
        }
    }

    /***
     * method used to get the months by integers.
     * @param month
     * @return
     */
    public int getMonthasInt(String month) {
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


    public void setAppointmentTableData() throws SQLException {
        String month = comboMonth.getSelectionModel().getSelectedItem();
        String type = comboReportType.getSelectionModel().getSelectedItem();
        String contact = contactCombo.getSelectionModel().getSelectedItem();
        String customer = comboCustomers.getSelectionModel().getSelectedItem();


        ObservableList<AppointmentEntity> filteredAppointments = FXCollections.observableList(SQLQueries.searchAppointments(false, month, type, contact, customer));
        appointmentsTableView.setItems(filteredAppointments);
        lblTotalAppointments.setText("Total Appointments: " + filteredAppointments.size());

    }


}
