// Import necessary packages
package entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.JavaDataBaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Define the CustomerEntity class
public class CustomerEntity {

    // Declare instance variables for CustomerEntity class
    private int customer_Id;
    private String cusName, address, city, postalCode, contactno, country, type;
    // Declare a static observable list to store all customers
    public static ObservableList<CustomerEntity> customersList = FXCollections.observableArrayList();

    // Constructor method for CustomerEntity class
    public CustomerEntity(int cusId, String cusName, String cusAddress, String city, String postalCode, String phoneNo,
                          String country, String type) {
        // Initialize instance variables with passed in values
        this.customer_Id = cusId;
        this.cusName = cusName;
        this.address = cusAddress;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.contactno = phoneNo;
        this.type = type;
    }

    public CustomerEntity(String cusName, String cusAddress, String city, String postalCode, String phoneNo,
                          String country, String type) {

        this.cusName = cusName;
        this.address = cusAddress;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
        this.contactno = phoneNo;
        this.type = type;
    }


    public int getCustomerID() {
        return customer_Id;
    }

    public void setCustomerID(int customerID) {
        this.customer_Id = customerID;
    }

    public String getCustomerName() {
        return cusName;
    }


    public void setCustomerName(String customerName) {
        this.cusName = customerName;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getPostalCode() {
        return postalCode;
    }


    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public String getPhoneNumber() {
        return contactno;
    }


    public void setContactNumber(String phno) {
        this.contactno = phno;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCustomer_Id() {
        return customer_Id;
    }

    public void setCustomer_Id(int customer_Id) {
        this.customer_Id = customer_Id;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    /***
     * method to get all the customers from the following database.
     * @return
     * @throws SQLException
     */
    public static ObservableList<CustomerEntity> getCustomers() throws SQLException {

        // Create a statement to execute SQL queries
        Statement statement = JavaDataBaseConnection.getConnection().createStatement();
        // Define SQL query to retrieve all customers
        String cusSql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country, first_level_divisions.*"
                + ", customers.Type, customers.Academic_Level, customers.Course, customers.Medical_Department, customers.Medical_History  "
                + " FROM customers "
                + "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID "
                + "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID";

        // Execute the SQL query and store the result in a ResultSet object
        ResultSet rst = statement.executeQuery(cusSql);


        while (rst.next()) {
            if (rst.getString("Type").equals("S")) {
                StudentEntity student = new StudentEntity(rst.getInt("Customer_ID"), rst.getString("Customer_Name"),
                        rst.getString("Address"), rst.getString("Division"), rst.getString("Postal_Code"),
                        rst.getString("Phone"), rst.getString("Country"));
                student.setCourse(rst.getString("Course"));
                student.setAcademicLevel(rst.getString("Academic_Level"));
                customersList.add(student);
            } else if (rst.getString("Type").equals("P")) {
                PatientEntity patient = new PatientEntity(rst.getInt("Customer_ID"), rst.getString("Customer_Name"),
                        rst.getString("Address"), rst.getString("Division"), rst.getString("Postal_Code"),
                        rst.getString("Phone"), rst.getString("Country"));
                patient.setMedicalHistory(rst.getString("Medical_History"));
                patient.setMedicalDepartment(rst.getString("Medical_Department"));
                customersList.add(patient);
            }


        }
        return customersList;
    }
}
