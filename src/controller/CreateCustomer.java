package controller;


import entity.PatientEntity;
import entity.StudentEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import util.SQLQueries;
import util.CityDivisionID;
import util.JavaDataBaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;
import entity.CustomerEntity;

public class CreateCustomer implements Initializable {

    public Button btnAddCustomer, btnCancel;

    public TextField nameTxt, AddressTxt, PostalTxt, phoneTxt;

    public ComboBox<String> CityCombo;
    public TextField cusIdTxt;
    public ComboBox<String> comboCountry;

    public ComboBox<String> comboType;
    public int divIDCity;
    ObservableList<String> citiesList = FXCollections.observableArrayList();
    ObservableList<String> countriesList = FXCollections.observableArrayList("U.S", "Canada", "UK");

    ObservableList<String> typeCustomerList = FXCollections.observableArrayList("Patient", "Student");

    Parent scene;
    Stage addCustomerStage;
    public ComboBox<String> academicLevelCombo;

    public ComboBox<String> medicalDeptCombo;


    public TextField courseText;
    public TextField medicalHistoryText;

    public Label lblMedicalDept;
    public Label lblMedicalHistory;

    public Label lblAcademicLevel;
    public Label lblCourse;

    private ObservableList<String> academicLevelList = FXCollections.observableArrayList("Middle School", "High School", "Undergraduate", "Graduate", "Master's Degree", "Doctoral Degree", "Postdoctoral Research", "Continuing Education");
    private ObservableList<String> medicalDeptList = FXCollections.observableArrayList("General", "Cardiology", "Dermatology", "Endocrinology", "Gastroenterology", "Hematology", "Neurology", "Oncology", "Orthopedics", "Pediatrics", "Psychiatry");

    public void changeWindow(ActionEvent event, String resString) throws IOException {
        addCustomerStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(resString));
        addCustomerStage.setScene(new Scene(scene));
        addCustomerStage.show();
    }

    /***
     * this event is to cancel any input, and to clear the textfields.
     * @param event
     * @throws IOException
     */
    public void onCancel(ActionEvent event) throws IOException {
        Alert disAlert = new Alert(Alert.AlertType.CONFIRMATION);
        disAlert.setTitle("Cancel");
        disAlert.setHeaderText("Are you sure you want to cancel?");
        disAlert.setContentText("This will clear all text fields");
        Optional<ButtonType> selOpt = disAlert.showAndWait();

        if (selOpt.isPresent() && selOpt.get() == ButtonType.OK) {
            changeWindow(event, "/view/Customers.fxml");
        }
    }

    /***
     * function used to choose which country should be selected , and sets it with id.
     * @param event
     * @throws SQLException
     */
    public void onChooseCountry(ActionEvent event) throws SQLException {
        String selectCountry = comboCountry.getSelectionModel().getSelectedItem();
        citiesList.clear();


        if (selectCountry.equals("U.S")) {
            Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
            String sqlCountry = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1";
            ResultSet uRst = stmnt.executeQuery(sqlCountry);

            while (uRst.next()) {
                String city = uRst.getString("Division");
                citiesList.add(city);
                CityCombo.setItems(citiesList);
            }
            stmnt.close();

        } else if (selectCountry.equals("UK")) {
            Statement ukst = JavaDataBaseConnection.getConnection().createStatement();
            String ukcitiesSql = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2";
            ResultSet ukcrst = ukst.executeQuery(ukcitiesSql);

            while (ukcrst.next()) {
                String city = ukcrst.getString("Division");
                citiesList.add(city);
                CityCombo.setItems(citiesList);
            }
            ukst.close();

        } else {
            Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
            String citiesSql = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3";
            ResultSet canrst = stmnt.executeQuery(citiesSql);

            while (canrst.next()) {
                String city = canrst.getString("Division");
                citiesList.add(city);
                CityCombo.setItems(citiesList);
            }
            stmnt.close();
        }
    }

    /***
     * onAddcustomer used to add the customer to the database. Collects its Id and following information.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void onAddCustomer(ActionEvent event) throws IOException, SQLException {

        int cusId = 0;
        for (CustomerEntity customerEntity : CustomerEntity.getCustomers()) {
            if (customerEntity.getCustomerID() > cusId) {
                cusId = customerEntity.getCustomerID();
            }
        }
        String customerName = nameTxt.getText();
        String customerAddress = AddressTxt.getText();
        String customerPos = PostalTxt.getText();
        String customerPhone = phoneTxt.getText();
        String customerCity = CityCombo.getSelectionModel().getSelectedItem();
        String customerCountry = comboCountry.getSelectionModel().getSelectedItem();


        if (verifyType() && verifyNotNullName(customerName) && verifyAddressNotNull(customerAddress) && verifyNotNullPostal(customerPos)
                && verifyEmptyPhone(customerPhone) && verifyCountry(customerCountry) && verifyCityNotNull(customerCity)) {

            String type = comboType.getSelectionModel().getSelectedItem();


            if (type.equals("Patient") && verifyMedicalDepartment() && verifyEmptyMedicalHistory()) {

                PatientEntity patient = new PatientEntity(customerName, customerAddress, customerCity, customerPos, customerPhone, customerCountry);
                patient.setMedicalDepartment(medicalDeptCombo.getSelectionModel().getSelectedItem());
                patient.setMedicalHistory(medicalHistoryText.getText());
                SQLQueries.insertIntoCustomersTable(patient, CityDivisionID.divisionID);

            } else if (type.equals("Student") && verifyAcademicLevel() && verifyEmptyCourse()) {
                StudentEntity student = new StudentEntity(customerName, customerAddress, customerCity, customerPos, customerPhone, customerCountry);
                student.setAcademicLevel(academicLevelCombo.getSelectionModel().getSelectedItem());
                student.setCourse(courseText.getText());
                SQLQueries.insertIntoCustomersTable(student, CityDivisionID.divisionID);
            }


            DisplayAlertMessage.alertDisplays(6);
            changeWindow(event, "/view/customers.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboCountry.setItems(countriesList);
        comboType.setItems(typeCustomerList);
        academicLevelCombo.setItems(academicLevelList);
        medicalDeptCombo.setItems(medicalDeptList);

        medicalDeptCombo.setDisable(true);
        academicLevelCombo.setDisable(true);
        medicalHistoryText.setDisable(true);
        courseText.setDisable(true);

        lblMedicalDept.setStyle("-fx-text-fill: grey;");
        lblAcademicLevel.setStyle("-fx-text-fill: grey;");
        lblMedicalHistory.setStyle("-fx-text-fill: grey;");
        lblCourse.setStyle("-fx-text-fill: grey;");

    }

    /***
     * method used to select the certain city.
     * @param event
     * @throws SQLException
     */
    public void onSelectCity(ActionEvent event) throws SQLException {
        String city = CityCombo.getSelectionModel().getSelectedItem();

        getCitiesByRegion(city);
        CityDivisionID.divisionID = divIDCity;
        System.out.println(divIDCity);
    }

    public void onSelectType(ActionEvent event) {
        String type = comboType.getSelectionModel().getSelectedItem();

        if (type.equals("Patient")) {

            medicalDeptCombo.setDisable(false);
            academicLevelCombo.setDisable(true);
            medicalHistoryText.setDisable(false);
            courseText.setDisable(true);

            lblMedicalDept.setStyle("-fx-text-fill: #3f31ac;");
            lblAcademicLevel.setStyle("-fx-text-fill: grey;");
            lblMedicalHistory.setStyle("-fx-text-fill: #3f31ac;");
            lblCourse.setStyle("-fx-text-fill: grey;");

        } else if (type.equals("Student")) {
            medicalDeptCombo.setDisable(true);
            academicLevelCombo.setDisable(false);
            medicalHistoryText.setDisable(true);
            courseText.setDisable(false);

            lblMedicalDept.setStyle("-fx-text-fill: grey;");
            lblAcademicLevel.setStyle("-fx-text-fill: #3f31ac;");
            lblMedicalHistory.setStyle("-fx-text-fill: grey;");
            lblCourse.setStyle("-fx-text-fill: #3f31ac;");

        }


    }


    /***
     * method get cities by region to select the certain cities by its region.
     * @param divId
     * @throws SQLException
     */
    public void getCitiesByRegion(String divId) throws SQLException {
        Statement constmnt = JavaDataBaseConnection.getConnection().createStatement();
        String query = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + divId + "'";
        ResultSet rst = constmnt.executeQuery(query);

        while (rst.next()) {
            divIDCity = rst.getInt("Division_ID");
        }
    }

    /***
     * methods used to make sure that the name is not null
     * @param name
     * @return
     */
    public boolean verifyNotNullName(String name) {
        if (nameTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(1);
            return false;
        }
        return true;
    }

    /***
     * method to verify if the address is not null.
     * @param address
     * @return
     */
    public boolean verifyAddressNotNull(String address) {
        if (AddressTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(2);
            return false;
        }
        return true;
    }

    /***
     * method to verify if the postal is not null
     * @param postalCode
     * @return
     */
    public boolean verifyNotNullPostal(String postalCode) {
        if (PostalTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(4);
            return false;
        }
        return true;
    }

    /***
     * method to verify if the phone is empty.
     */

    public boolean verifyEmptyPhone(String phone) {
        if (phoneTxt.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(5);
            return false;
        }
        return true;
    }

    /***
     * method to verify the country.
     * @param country
     * @return
     */
    public boolean verifyCountry(String country) {
        if (comboCountry.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(10);
            return false;
        }
        return true;
    }

    public boolean verifyEmptyCourse() {
        if (courseText.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(33);
            return false;
        }
        return true;
    }

    public boolean verifyEmptyMedicalHistory() {
        if (medicalHistoryText.getText().isEmpty()) {
            DisplayAlertMessage.alertDisplays(34);
            return false;
        }
        return true;
    }


    public boolean verifyType() {
        if (comboType.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(30);
            return false;
        }
        return true;
    }


    public boolean verifyAcademicLevel() {
        if (academicLevelCombo.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(31);
            return false;
        }
        return true;
    }

    public boolean verifyMedicalDepartment() {
        if (medicalDeptCombo.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(32);
            return false;
        }
        return true;
    }


    /***
     * method to verify if the city is not null
     * @param city
     * @return
     */
    public boolean verifyCityNotNull(String city) {
        if (CityCombo.getSelectionModel().getSelectedItem() == null) {
            DisplayAlertMessage.alertDisplays(3);
            return false;
        }
        return true;
    }
}
