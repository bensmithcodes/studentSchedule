/**
 * This class contains static methods for executing SQL queries to interact with a database.
 */
package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import entity.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

public class SQLQueries {
    /**
     * Inserts a new customer record into the customers table.
     *
     * @param customer   the customer
     * @param divisionID the ID of the division associated with the customer's
     *                   location
     * @return the number of rows affected by the SQL statement
     * @throws SQLException if an error occurs while executing the SQL statement
     */
    //public static int insertIntoCustomersTable(String customerName, String address, String Phone, String postalCode,
    //		int divisionID) throws SQLException {
    public static int insertIntoCustomersTable(CustomerEntity customer, int divisionID) throws SQLException {
        String sql = "";
        PreparedStatement pst = null;
        if (customer instanceof PatientEntity) {
            sql = "INSERT INTO customers (Customer_Name, Address, Phone, Postal_Code, Division_ID, Type,Medical_Department, Medical_History) VALUES(?,?,?,?,?,?,?,?)";
            pst = JavaDataBaseConnection.getConnection().prepareStatement(sql);
            pst.setString(6, "P");
            pst.setString(7, ((PatientEntity) customer).getMedicalDepartment());
            pst.setString(8, ((PatientEntity) customer).getMedicalHistory());

        } else if (customer instanceof StudentEntity) {
            sql = "INSERT INTO customers (Customer_Name, Address, Phone, Postal_Code, Division_ID,Type,Academic_Level, Course) VALUES(?,?,?,?,?,?,?,?)";
            pst = JavaDataBaseConnection.getConnection().prepareStatement(sql);
            pst.setString(6, "S");
            pst.setString(7, ((StudentEntity) customer).getAcademicLevel());
            pst.setString(8, ((StudentEntity) customer).getCourse());
        }


        pst.setString(1, customer.getCustomerName());
        pst.setString(2, customer.getAddress());
        pst.setString(3, customer.getPhoneNumber());
        pst.setString(4, customer.getPostalCode());
        pst.setInt(5, divisionID);

        int rpst = pst.executeUpdate();

        pst.close();
        return rpst;
    }

    /**
     * Updates an existing customer record in the customers table.
     *
     * @param customerID   the ID of the customer record to update
     * @param customerName the updated name of the customer
     * @param address      the updated address of the customer
     * @param postalCode   the updated postal code of the customer's location
     * @param Phone        the updated phone number of the customer
     * @param divisionID   the updated ID of the division associated with the
     *                     customer's location
     * @return the number of rows affected by the SQL statement
     * @throws SQLException if an error occurs while executing the SQL statement
     */
    public static int updateCustomersTable(int customerID, String customerName, String address, String postalCode,
                                           String Phone, int divisionID) throws SQLException {

        String sqlCustomerUpdate = "UPDATE customers SET Customer_Name=?, Address=?, Phone=?, Postal_Code=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement pst = JavaDataBaseConnection.getConnection().prepareStatement(sqlCustomerUpdate);

        pst.setInt(6, customerID);
        pst.setString(1, customerName);
        pst.setString(2, address);
        pst.setString(3, Phone);
        pst.setString(4, postalCode);
        pst.setInt(5, divisionID);

        int chkUpdates = pst.executeUpdate();

        if (chkUpdates == 1) {
            DisplayAlertMessage.alertDisplays(7);
        }
        pst.close();
        return chkUpdates;
    }

    /**
     * Deletes a customer record from the customers table.
     *
     * @param customerID the ID of the customer record to delete
     * @return the number of rows affected by the SQL statement
     * @throws SQLException if an error occurs while executing the SQL statement
     */
    public static int deleteFromCustomersTable(int customerID) throws SQLException {

        String sqlDel = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement dpst = JavaDataBaseConnection.getConnection().prepareStatement(sqlDel);

        dpst.setInt(1, customerID);

        int delRows = dpst.executeUpdate();

        dpst.close();
        return delRows;
    }

    /**
     * Deletes an appointment record from the appointments table.
     *
     * @param appointmentId the ID of the appointment record to delete
     * @return the number of rows affected by the SQL
     * @throws SQLException if there is an error with the SQL query or connection
     */
    public static int deleteFromAppointmentsTable(int appointmentId) throws SQLException {

        String sqlApDel = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement dapst = JavaDataBaseConnection.getConnection().prepareStatement(sqlApDel);

        dapst.setInt(1, appointmentId);

        int aprowsdeleted = dapst.executeUpdate();

        dapst.close();
        return aprowsdeleted;
    }

    public static int insertAppointment(String title, String description, String location, String type, Timestamp start,
                                        Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String sqlAppointments = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = JavaDataBaseConnection.getConnection().prepareStatement(sqlAppointments);

        pst.setString(1, title);
        pst.setString(2, description);
        pst.setString(3, location);
        pst.setString(4, type);
        pst.setTimestamp(5, start);
        pst.setTimestamp(6, end);
        pst.setInt(7, customerId);
        pst.setInt(8, userId);
        pst.setInt(9, contactId);

        int rows = pst.executeUpdate();

        pst.close();
        return rows;
    }


    /***
     * method used to update the current appointment.
     * @param appointmentID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @param contactId
     * @return
     * @throws SQLException
     */
    public static int updateAppointment(int appointmentID, String title, String description, String location,
                                        String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId)
            throws SQLException {

        String sqlUApp = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, "
                + "Customer_ID=?, User_ID=?, Contact_ID=? " + "WHERE Appointment_ID=?";

        PreparedStatement pst = JavaDataBaseConnection.getConnection().prepareStatement(sqlUApp);

        pst.setInt(10, appointmentID);
        pst.setString(1, title);
        pst.setString(2, description);
        pst.setString(3, location);
        pst.setString(4, type);
        pst.setTimestamp(5, start);
        pst.setTimestamp(6, end);
        pst.setInt(7, customerId);
        pst.setInt(8, userId);
        pst.setInt(9, contactId);

        int uprows = pst.executeUpdate();

        pst.close();
        return uprows;
    }

    public static List<AppointmentEntity> searchAppointments(boolean thisWeek, String month, String type, String contactName, String customerName) throws SQLException {
        String sqlAppointments = "select a.Appointment_ID, a.Title, a.Description, a.Location, a.Type, a.Start, a.End, a.Customer_ID," +
                "cus.Customer_Name, a.User_ID, a.Contact_ID, con.Contact_Name from  appointments a,  customers cus, contacts con where  a.Customer_ID=cus.Customer_ID and a.Contact_ID=con.Contact_ID ";

        if (thisWeek) {
            sqlAppointments = sqlAppointments + " and ( YEARWEEK(a.Start) = YEARWEEK(CURDATE()) or  YEARWEEK(a.End) = YEARWEEK(CURDATE()) ) ";

        }

        if (month != null) {
            sqlAppointments = sqlAppointments + " and ( UPPER(MONTHNAME(STR_TO_DATE(a.Start, '%Y-%m-%d %H:%i:%s'))) = '" + month + "' or " +
                    " UPPER(MONTHNAME(STR_TO_DATE(a.End, '%Y-%m-%d %H:%i:%s'))) = '" + month + "' ) ";
        }
        if (type != null) {
            sqlAppointments = sqlAppointments + " and a.Type Like '%" + type + "%' ";
        }
        if (contactName != null) {
            sqlAppointments = sqlAppointments + " and con.Contact_Name Like '%" + contactName + "%' ";

        }
        if (customerName != null) {
            sqlAppointments = sqlAppointments + " and cus.Customer_Name Like '%" + customerName + "%' ";

        }


        Statement st = JavaDataBaseConnection.getConnection().createStatement();

        ResultSet resultSet = st.executeQuery(sqlAppointments);

        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
        while (resultSet.next()) {
            AppointmentEntity appointmentEntity = new AppointmentEntity();
            appointmentEntity.setAppointmentId(resultSet.getInt("Appointment_ID"));
            appointmentEntity.setTitle(resultSet.getString("Title"));
            appointmentEntity.setDescription(resultSet.getString("Description"));
            appointmentEntity.setLocation(resultSet.getString("Location"));
            appointmentEntity.setType(resultSet.getString("Type"));
            appointmentEntity.setStart(resultSet.getTimestamp("Start").toLocalDateTime());
            appointmentEntity.setEnd(resultSet.getTimestamp("End").toLocalDateTime());
            appointmentEntity.setCustomerId(resultSet.getInt("Customer_ID"));
            appointmentEntity.setUserId(resultSet.getInt("User_ID"));
            appointmentEntity.setContactName(resultSet.getString("Contact_Name"));
            appointmentEntityList.add(appointmentEntity);
        }

        System.out.println(appointmentEntityList.size());
        return appointmentEntityList;
    }


    public static List<CustomerEntity> getCustomersByType(String type) throws SQLException {
        List<CustomerEntity> customersList = new ArrayList<>();

        // Create a statement to execute SQL queries
        Statement statement = JavaDataBaseConnection.getConnection().createStatement();
        // Define SQL query to retrieve all customers
        String cusSql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, countries.Country, first_level_divisions.*"
                + ", customers.Type, customers.Academic_Level, customers.Course, customers.Medical_Department, customers.Medical_History  "
                + " FROM customers "
                + "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID "
                + "INNER JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID " +
                " where Type='" + type + "'";

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
