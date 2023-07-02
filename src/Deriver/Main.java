package Deriver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.JavaDataBaseConnection;

import java.sql.SQLException;

/**
 * This class is the main entry point for the Application. It launches the
 * JavaFX Application and initializes the database connection.
 */
public class Main extends Application {

    /**
     * The start method is called by the JavaFX Application thread and initializes
     * the primary Stage.
     *
     * @param primaryStage The primary Stage of the Application.
     * @throws Exception Thrown if there is an error while loading the Login.fxml
     *                   file.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        primaryStage.setTitle("Appointment Booking System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * This is the main method, and is the starting point for the  program. Initializes the
     * connection of the database, and runs the JavaFX Application.
     *
     * @param args The command-line arguments passed to the Java program. Not mandatory
     * @throws SQLException Thrown if there is an error while initializing the
     *                      database connection.
     */
    public static void main(String[] args) throws SQLException {
        JavaDataBaseConnection.startConnection();
        launch(args);
        JavaDataBaseConnection.closeConnection();
    }
}
