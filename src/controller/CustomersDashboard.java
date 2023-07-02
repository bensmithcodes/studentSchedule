package controller;

import entity.PatientEntity;
import entity.StudentEntity;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.SQLQueries;
import util.JavaDataBaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;
import entity.CustomerEntity;

public class CustomersDashboard implements Initializable {

    public TableView<CustomerEntity> tableCustomer;

    public TableColumn<CustomerEntity, Integer> CusIdCol;
    public TableColumn<CustomerEntity, String> cusNameCol;
    public TableColumn<CustomerEntity, String> cusAddressCol;
    public TableColumn<CustomerEntity, String> cusCityCol;
    public TableColumn<CustomerEntity, String> cusCountryCol;
    public TableColumn<CustomerEntity, String> cusPostalCol;
    public TableColumn<CustomerEntity, String> cusPhoneCol;

    public TableColumn<CustomerEntity, String> cusTypeCol;
    public TableColumn<StudentEntity, String> cusAcademicLevelCol;
    public TableColumn<StudentEntity, String> cusCourseCol;
    public TableColumn<PatientEntity, String> cusMedicalDeptCol;

    public TableColumn<PatientEntity, String> cusMedicalHistoryCol;


    public Button btnSaveCustomer, btnUpdateCustomer, btnRemoveCustomer, btnDash;

    private static CustomerEntity customerSelected;

    Parent scene;
    Stage customerDashStage;

    private ToggleGroup toggleGroup;
    public RadioButton allRadio;
    public RadioButton studentsRadio;
    public RadioButton patientsRadio;

    public static CustomerEntity getCurrentSelectedCustomers() {
        return customerSelected;
    }


    public void changeWindow(ActionEvent event, String resString) throws IOException {
        customerDashStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resString));
        customerDashStage.setScene(new Scene(scene));
        customerDashStage.centerOnScreen();
        customerDashStage.show();
    }

    /***
     * Save customer is method to save all the entered values into the database..
     * @param event
     * @throws IOException
     */
    public void onSaveCustomer(ActionEvent event) throws IOException {
        changeWindow(event, "/view/AddCustomers.fxml");
    }

    /***
     * This is the method to modify the values of the customer detail.
     * @param event
     * @throws IOException
     */
    public void onModifyCustomer(ActionEvent event) throws IOException {
        customerSelected = tableCustomer.getSelectionModel().getSelectedItem();

        if (customerSelected == null) {
            DisplayAlertMessage.errorAlert("ERROR", "No customer highlighted", "Please select a customer to modify");
        } else {
            changeWindow(event, "/view/ModifyCustomer.fxml");
        }
    }

    /***
     * Fetching the customer appointment records pulls from the database to be shown.
     * @param cusId
     * @return
     * @throws SQLException
     */
    public static int fetchCustomerAppointmentRecods(int cusId) throws SQLException {

        Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
        String sql = "SELECT COUNT(Appointment_ID) AS Count " + "FROM appointments "
                + "INNER JOIN customers ON customers.Customer_ID = appointments.Customer_ID "
                + "WHERE customers.Customer_ID=" + cusId;

        ResultSet rst = stmnt.executeQuery(sql);

        if (rst.next() && rst.getInt("Count") > 0) {
            DisplayAlertMessage.errorAlert("Cannot Delete CustomerEntity", "CustomerEntity has existing appointments",
                    "Please delete all appointments associated with this customer before trying to delete.");
            return -1;
        }
        return 0;
    }

    /***
     * This is the function that removes the customer element from selected appointments.
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void onRemoveCustomer(ActionEvent event) throws SQLException, IOException {
        CustomerEntity customer = tableCustomer.getSelectionModel().getSelectedItem();

        if (customer == null) {
            DisplayAlertMessage.alertDisplays(9);
        } else if (fetchCustomerAppointmentRecods(customer.getCustomerID()) == 0) {
            Alert dAlert = new Alert(Alert.AlertType.CONFIRMATION);
            dAlert.setHeaderText("Are you sure you want to delete this customer?");
            dAlert.setContentText("Deleting the customer will remove them and their appointments");
            Optional<ButtonType> checkDelOk = dAlert.showAndWait();

            if (checkDelOk.isPresent() && checkDelOk.get() == ButtonType.OK) {
                SQLQueries.deleteFromCustomersTable(customer.getCustomerID());
                DisplayAlertMessage.alertDisplays(11);
                changeWindow(event, "/view/Customers.fxml");
            }
        }
    }

    @FXML
    public void onDashBoard(ActionEvent event) throws IOException {
        changeWindow(event, "/view/Dashboard.fxml");
    }

    /***
     * To initialize all the values pertaining to the customer.
     * @param url
     * @param resBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resBundle) {
        toggleGroup = new ToggleGroup();

        allRadio.setToggleGroup(toggleGroup);
        studentsRadio.setToggleGroup(toggleGroup);
        patientsRadio.setToggleGroup(toggleGroup);


        try {
            CustomerEntity.getCustomers().clear();
            tableCustomer.setItems(CustomerEntity.getCustomers());
            tableCustomer.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        CusIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        cusAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        cusCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        cusCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        cusPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        cusPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));


        cusTypeCol.setCellValueFactory(data -> {
            if (data.getValue().getType().equals("P")) {
                return new SimpleStringProperty("Patient");
            } else if (data.getValue().getType().equals("S")) {
                return new SimpleStringProperty("Student");
            }


            if (data.getValue() instanceof StudentEntity) {
                return new SimpleStringProperty(((StudentEntity) data.getValue()).getAcademicLevel());
            } else {
                return new SimpleStringProperty("-");
            }
        });

        cusAcademicLevelCol.setCellValueFactory(data -> {
            if (data.getValue() instanceof StudentEntity) {
                return new SimpleStringProperty(((StudentEntity) data.getValue()).getAcademicLevel());
            } else {
                return new SimpleStringProperty("-");
            }
        });

        cusCourseCol.setCellValueFactory(data -> {
            if (data.getValue() instanceof StudentEntity) {
                return new SimpleStringProperty(((StudentEntity) data.getValue()).getCourse());
            } else {
                return new SimpleStringProperty("-");
            }
        });


        cusMedicalDeptCol.setCellValueFactory(data -> {
            if (data.getValue() instanceof PatientEntity) {
                return new SimpleStringProperty(((PatientEntity) data.getValue()).getMedicalDepartment());
            } else {
                return new SimpleStringProperty("-");
            }
        });


        cusMedicalHistoryCol.setCellValueFactory(data -> {
            if (data.getValue() instanceof PatientEntity) {
                return new SimpleStringProperty(((PatientEntity) data.getValue()).getMedicalHistory());
            } else {
                return new SimpleStringProperty("-");
            }
        });


    }

    public void filterCustomers(ActionEvent event) throws SQLException {

        if (allRadio.isSelected()) {
            CustomerEntity.getCustomers().clear();
            tableCustomer.setItems(CustomerEntity.getCustomers());
        } else if (patientsRadio.isSelected()) {
            tableCustomer.setItems(FXCollections.observableList(SQLQueries.getCustomersByType("P")));
        } else {
            tableCustomer.setItems(FXCollections.observableList(SQLQueries.getCustomersByType("S")));
        }

    }
}
