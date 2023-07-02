/**
 * JavaDataBaseConnection is a utility class that creates and manages the connection with the MySQL database.
 * It provides methods to start and close the connection, and to get the connection object.
 * The connection parameters are hardcoded in the class.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JavaDataBaseConnection {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection connection = null;
    private static PreparedStatement pst;
    private static String USERNAME = "root";
    private static String PASSWORD = "";


    /**
     * Starts the connection with the MySQL database. If the connection is
     * successful, it prints a message to the console. If the connection fails, it
     * prints an error message to the console.
     */
    public static void startConnection() {

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/client_schedule", USERNAME, PASSWORD); // reference
            // Connection
            // object
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error:" + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * This method gets the connection of the  object.
     *
     * @return The connection object.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * This method closes connection with the MySQL database. If the connection is closed,
     * it notifies the console. If the connection doesnt close, it
     * shows an error message.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}