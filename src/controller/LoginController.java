package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entity.DisplayAlertMessage;

import util.JavaDataBaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import entity.EntityUser;
import util.*;

public class LoginController implements Initializable {

	public TextField userTxt;
	public PasswordField passTxt;
	public Button loginBtn;
	public Label zoneLbl;
	public Button exitBtn;
	public Label userLbl;
	public Label passLbl;
	public static String UIdTitle, HUId, ContUId, LogTitle, LoginHeader, LoginContext;

	Parent scene;

	Stage loginStage;

	/***
	 * method that is used to validate the login and password
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	public void onLogin(ActionEvent event) throws IOException, SQLException {
		String uname = userTxt.getText();
		String pass = passTxt.getText();
		EntityUser.username = uname;

		validateUsername(uname);
		validatePassword(pass);

		if (validateUsername(uname) && validatePassword(pass)) {
			LoginDetails.saveLoggedInAttempt(uname, true, "You are now logged in.");
			getUserId(uname);

			loginStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
			scene = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
			loginStage.setScene(new Scene(scene));
			loginStage.show();
		} else {
			LoginDetails.saveLoggedInAttempt(uname, false, "Login failed, please try again.");
			DisplayAlertMessage.errorAlert(LogTitle, LoginHeader, LoginContext);
		}
	}

	/***
	 * function that is used to validate the password.
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	private boolean validatePassword(String password) throws SQLException {
		Statement statement = JavaDataBaseConnection.getConnection().createStatement();
		String sqlLogin = "SELECT Password FROM users WHERE Password ='" + password + "'";
		ResultSet rst = statement.executeQuery(sqlLogin);

		while (rst.next()) {
			if (rst.getString("Password").equals(password)) {
				return true;
			}
		}
		statement.close();
		return false;
	}

	/***
	 * function used to validate the username fomr database.
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private boolean validateUsername(String user) throws SQLException {
		Statement statement = JavaDataBaseConnection.getConnection().createStatement();
		String userName = "SELECT User_Name FROM users WHERE User_Name ='" + user + "'";
		ResultSet rst = statement.executeQuery(userName);

		while (rst.next()) {
			if (rst.getString("User_Name").equals(user)) {
				return true;
			}
		}
		statement.close();
		return false;
	}

	/***
	 * function that is used to get the userID from database.
	 * @param uname
	 * @return
	 * @throws SQLException
	 */
	public static int getUserId(String uname) throws SQLException {
		Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
		String sqlUname = "SELECT User_ID, User_Name FROM users WHERE User_Name ='" + uname + "'";
		ResultSet unrst = stmnt.executeQuery(sqlUname);

		while (unrst.next()) {
			if (unrst.getString("User_Name").equals(uname)) {
				return unrst.getInt("User_ID");
			}
		}
		stmnt.close();
		DisplayAlertMessage.errorAlert(UIdTitle, HUId, ContUId);
		return -1;
	}

	/***
	 * initialize the controller to retrieve information.
	 * @param url
	 * @param resBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resBundle) {
		ZoneId zoneUser = ZoneId.systemDefault();
		zoneLbl.setText(zoneUser.toString());

		Locale locale = Locale.getDefault();
		ResourceBundle rsb = ResourceBundle.getBundle("LanguageBundles/mchristian", locale);

		if (locale.getLanguage().equals("fr")) {
			this.userTxt.setPromptText(rsb.getString("usernameFieldPromptText"));
			this.passTxt.setPromptText(rsb.getString("passwordFieldPromptText"));
			this.userLbl.setText(rsb.getString("username"));
			this.passLbl.setText(rsb.getString("password"));
			this.loginBtn.setText(rsb.getString("loginButtonText"));
			this.exitBtn.setText(rsb.getString("exitBttnText"));
			UIdTitle = rsb.getString("titleForUserID");
			HUId = rsb.getString("headerForUserID");
			ContUId = rsb.getString("contextForUserID");
			LogTitle = rsb.getString("titleForLogin");
			LoginHeader = rsb.getString("headerForLogin");
			LoginContext = rsb.getString("contextForLogin");
		} else {
			UIdTitle = rsb.getString("titleForUserID");
			HUId = rsb.getString("headerForUserID");
			ContUId = rsb.getString("contextForUserID");
			LogTitle = rsb.getString("titleForLogin");
			LoginHeader = rsb.getString("headerForLogin");
			LoginContext = rsb.getString("contextForLogin");
		}

	}

	/***
	 * method that is used to exit out event.
	 * @param event
	 */
	@FXML
	public void onExit(ActionEvent event) {
		System.exit(0);
	}
}
