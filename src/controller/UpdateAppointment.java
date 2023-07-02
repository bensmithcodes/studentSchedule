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
import util.JavaDataBaseConnection;
import util.BuisnessHoursTime;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

import entity.DisplayAlertMessage;
import entity.AppointmentEntity;

public class UpdateAppointment implements Initializable {
	@FXML
	public Button backBtn, dashBtn, updateBtn;
	@FXML
	public ComboBox<LocalTime> EndTimeCombo, comboStartTime;
	@FXML
	public DatePicker pickDate;
	@FXML
	public TextField apIdTxt, locationIdTxt, desTxt, titleTxt;
	@FXML
	public ComboBox<String> customerCombo;
	@FXML
	public ComboBox<Integer> userIdCombo;
	@FXML
	public ComboBox<String> comboContact;
	@FXML
	public TextField cusIdTxt;
	@FXML
	public ComboBox<String> typeCombo;
	private int cusId;
	private static AppointmentEntity appSelected;

	public void setContactId(int contactId) {
		this.cusId = contactId;
	}

	public static ObservableList<String> existingCustomers = FXCollections.observableArrayList();
	public static ObservableList<String> contactNames = FXCollections.observableArrayList();
	public static ObservableList<Integer> userIds = FXCollections.observableArrayList();
	private ObservableList<String> apTypes = FXCollections.observableArrayList("Doctor", "Class",
			"Exam");

	Parent scene;
	Stage updateAppointment;

	public void changeWindow(ActionEvent event, String resString) throws IOException {
		updateAppointment = (Stage) ((Button) event.getSource()).getScene().getWindow();
		scene = FXMLLoader.load(getClass().getResource(resString));
		updateAppointment.setScene(new Scene(scene));
		updateAppointment.show();
	}

	/***
	 * function that is used to go back to previous view.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void onBackButton(ActionEvent event) throws IOException {
		Alert dAlert = new Alert(Alert.AlertType.CONFIRMATION);
		dAlert.setTitle("Cancel");
		dAlert.setHeaderText("Confirm, Go Back?");
		dAlert.setContentText("Nothing will be saved");
		Optional<ButtonType> selOpt = dAlert.showAndWait();

		if (selOpt.isPresent() && selOpt.get() == ButtonType.OK) {
			changeWindow(event, "/view/Appointments.fxml");
		}
	}

	@FXML
	private void onDashboard(ActionEvent event) throws IOException {
		Alert dalert = new Alert(Alert.AlertType.CONFIRMATION);
		dalert.setTitle("Cancel");
		dalert.setHeaderText("Confirm that you Want To Go the Dashboard?");
		dalert.setContentText("Data will not be saved");
		Optional<ButtonType> selOpt = dalert.showAndWait();

		if (selOpt.isPresent() && selOpt.get() == ButtonType.OK) {
			changeWindow(event, "/view/Dashboard.fxml");
		}
	}

	/***
	 * method that is used to update an appointment.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void onUpdateAppointment(ActionEvent event) throws IOException {

		try {
			int customerId = Integer.parseInt(cusIdTxt.getText());
			int userId = userIdCombo.getSelectionModel().getSelectedItem();
			int appointmentId = appSelected.getAppointmentId();
			String appointmentTITLE = titleTxt.getText();
			String desc = desTxt.getText();
			String loction = locationIdTxt.getText();
			String type = typeCombo.getSelectionModel().getSelectedItem();
			LocalDate localeDate = pickDate.getValue();
			LocalTime start_time = comboStartTime.getSelectionModel().getSelectedItem();
			LocalTime end_time = EndTimeCombo.getSelectionModel().getSelectedItem();
			Timestamp starttimestamp = Timestamp.valueOf(LocalDateTime.of(localeDate, start_time));
			Timestamp etimestamp = Timestamp.valueOf(LocalDateTime.of(localeDate, end_time));

			boolean istimeValid = BuisnessHoursTime.validateBuisnessHours(localeDate, start_time, end_time,
					ZoneId.systemDefault());

			if (verifyTitleisNotEmpty(appointmentTITLE) && verifyDescriptionisNotEmpty(desc)
					&& verifyTypeisNotEmpty(type) && verifyLocationNotEmpty(loction)
					&& verifyStartTimeisNotNull(starttimestamp) && verifyEndTimeisNotNull(etimestamp)
					&& verifyDateisNotEmpty(localeDate) && verifyCustomerisNotEmpty(customerId)
					&& verifyContactisNotEmpty(cusId) && verifyUserIdisNotEmpty(userId)
					&& validateAppointmentsTiming(starttimestamp, etimestamp) && !istimeValid) {

				DisplayAlertMessage.alertDisplays(23);
				SQLQueries.updateAppointment(appointmentId, appointmentTITLE, desc, loction, type, starttimestamp,
						etimestamp, customerId, userId, cusId);

				changeWindow(event, "/view/Appointments.fxml");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			if (cusIdTxt.getText() == null) {
				DisplayAlertMessage.alertDisplays(20);
			} else if (userIdCombo.getSelectionModel().getSelectedItem() == null) {
				DisplayAlertMessage.alertDisplays(21);
			} else if (pickDate.getValue() == null) {
				DisplayAlertMessage.alertDisplays(17);
			} else if (comboStartTime.getSelectionModel().getSelectedItem() == null) {
				DisplayAlertMessage.alertDisplays(18);
			} else if (EndTimeCombo.getValue() == null) {
				DisplayAlertMessage.alertDisplays(19);
			} else if (customerCombo.getValue() == null) {
				DisplayAlertMessage.alertDisplays(20);
			}
		}
	}

	/***
	 * function that is used to choose a certain customer.
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	public void onChooseCustomer(ActionEvent event) throws SQLException {
		String customerName = customerCombo.getSelectionModel().getSelectedItem();

		Statement statment = JavaDataBaseConnection.getConnection().createStatement();
		String sql = "SELECT Customer_ID FROM customers WHERE Customer_Name='" + customerName + "'";
		ResultSet rst = statment.executeQuery(sql);

		while (rst.next()) {
			cusIdTxt.setText(String.valueOf(rst.getInt("Customer_ID")));
		}
		statment.close();
	}

	/***
	 * method that is used to initialize controller.
	 * @param url
	 * @param resBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle resBundle) {

		appSelected = AppointmentsDashboard.getCurrentlySelectedAppointment();

		apIdTxt.setText(String.valueOf(appSelected.getAppointmentId()));
		titleTxt.setText(appSelected.getTitle());
		locationIdTxt.setText(appSelected.getLocation());
		desTxt.setText(appSelected.getDescription());
		typeCombo.setValue(appSelected.getType());
		comboContact.setValue(appSelected.getContactName());

		pickDate.setValue(appSelected.getStart().toLocalDate());
		comboStartTime.setValue(appSelected.getStart().toLocalTime());
		EndTimeCombo.setValue((appSelected.getEnd().toLocalTime()));
		cusIdTxt.setText(String.valueOf(appSelected.getCustomerId()));
		userIdCombo.setValue(appSelected.getUserId());
		try {
			retrieveContactID();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		BuisnessHoursTime start_time = new BuisnessHoursTime();
		comboStartTime.setItems(start_time.generateTimeList());

		BuisnessHoursTime end_time = new BuisnessHoursTime();
		ObservableList<LocalTime> eTimeList = end_time.generateTimeList();
		eTimeList.add(LocalTime.of(0, 0));

		EndTimeCombo.setItems(eTimeList);
		typeCombo.setItems(apTypes);

		existingCustomers.clear();
		contactNames.clear();
		userIds.clear();

		try {
			Statement conn = JavaDataBaseConnection.getConnection().createStatement();
			String sql = "SELECT * FROM customers WHERE Customer_ID=" + appSelected.getCustomerId();
			ResultSet rst = conn.executeQuery(sql);

			if (rst.next()) {
				customerCombo.setValue(rst.getString("Customer_Name"));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		try {
			Statement cusst = JavaDataBaseConnection.getConnection().createStatement();
			String sqlCustomers = "SELECT * FROM customers";
			ResultSet rst = cusst.executeQuery(sqlCustomers);

			while (rst.next()) {
				existingCustomers.add(rst.getString("Customer_Name"));
				customerCombo.setItems(existingCustomers);
			}
			cusst.close();

			Statement conforContacts = JavaDataBaseConnection.getConnection().createStatement();
			String sqlConStatement = "SELECT * FROM contacts";
			ResultSet contRst = conforContacts.executeQuery(sqlConStatement);

			while (contRst.next()) {
				contactNames.add(contRst.getString("Contact_Name"));
				comboContact.setItems(contactNames);
			}
			conforContacts.close();

			Statement dbcon = JavaDataBaseConnection.getConnection().createStatement();
			String userSql = "SELECT * FROM users";
			ResultSet usrst = dbcon.executeQuery(userSql);

			while (usrst.next()) {
				userIds.add(usrst.getInt("User_ID"));
				userIdCombo.setItems(userIds);
			}
			dbcon.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/***
	 * method to make sure that the start and end times dont conflict.
	 * @param starttime
	 * @param end
	 * @return
	 */
	public boolean validateAppointmentsTiming(Timestamp starttime, Timestamp end) {

		boolean startbeforeend = end.before(starttime);
		boolean startendsame = end.equals(starttime);

		if (startbeforeend) {
			DisplayAlertMessage.alertDisplays(24);
			return false;
		} else if (startendsame) {
			DisplayAlertMessage.alertDisplays(25);
			return false;
		}
		ZonedDateTime sysZoneTime = ZonedDateTime.of(starttime.toLocalDateTime(), ZoneId.systemDefault());
		ZonedDateTime endZonesys = ZonedDateTime.of(end.toLocalDateTime(), ZoneId.systemDefault());

		try {
			Statement conn = JavaDataBaseConnection.getConnection().createStatement();

			String appsSql = "SELECT * " + "FROM appointments " + "WHERE ('" + sysZoneTime + "' BETWEEN Start AND End "
					+ "OR '" + endZonesys + "' BETWEEN Start AND End) " + "AND Appointment_ID !=" + apIdTxt.getText();

			ResultSet rst = conn.executeQuery(appsSql);

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
	 * method used to make sure title is not empty.
	 * @param title
	 * @return
	 */
	public boolean verifyTitleisNotEmpty(String title) {
		if (titleTxt.getText().isEmpty()) {
			DisplayAlertMessage.alertDisplays(13);
			return false;
		}
		return true;
	}

	/***
	 * function used to make sure description isn't empty.
	 * @param desc
	 * @return
	 */
	public boolean verifyDescriptionisNotEmpty(String desc) {
		if (desTxt.getText().isEmpty()) {
			DisplayAlertMessage.alertDisplays(14);
			return false;
		}
		return true;
	}

	/***
	 * function used to make sure the type is not empty.
	 * @param type
	 * @return
	 */
	public boolean verifyTypeisNotEmpty(String type) {
		if (typeCombo.getSelectionModel().getSelectedItem() == null) {
			DisplayAlertMessage.alertDisplays(16);
			return false;
		}
		return true;
	}

	/***
	 * method that is used to verify that the location is not empty.
	 * @param loc
	 * @return
	 */
	public boolean verifyLocationNotEmpty(String loc) {
		if (locationIdTxt.getText().isEmpty()) {
			DisplayAlertMessage.alertDisplays(15);
			return false;
		}
		return true;
	}

	public boolean verifyStartTimeisNotNull(Timestamp start) {
		if (comboStartTime.getSelectionModel().getSelectedItem() == null) {
			DisplayAlertMessage.alertDisplays(18);
			return false;
		}
		return true;
	}

	/***
	 * function that is used to verify the time is not null.
	 * @param endtime
	 * @return
	 */
	public boolean verifyEndTimeisNotNull(Timestamp endtime) {
		if (EndTimeCombo.getSelectionModel().getSelectedItem() == null) {
			DisplayAlertMessage.alertDisplays(19);
			return false;
		}
		return true;
	}

	/***
	 * function that is used to verify date isnt null.
	 * @param date
	 * @return
	 */
	public boolean verifyDateisNotEmpty(LocalDate date) {
		if (pickDate.getValue() == null) {
			DisplayAlertMessage.alertDisplays(17);
			return false;
		}
		return true;
	}

	/***
	 * function that is used to verify custer isnt null
	 *
	 *
	 * @param cid
	 * @return
	 */
	public boolean verifyCustomerisNotEmpty(int cid) {
		if (customerCombo.getSelectionModel().getSelectedItem() == null) {
			DisplayAlertMessage.alertDisplays(20);
			return false;
		}
		return true;
	}

	/***
	 * function that is used to verify userid is not null..
	 * @param userId
	 * @return
	 */
	public boolean verifyUserIdisNotEmpty(int userId) {
		if (userIdCombo.getSelectionModel().getSelectedItem() == null) {
			DisplayAlertMessage.alertDisplays(21);
			return false;
		}
		return true;
	}

	public boolean verifyContactisNotEmpty(int contact) {
		if (comboContact.getSelectionModel().getSelectedItem() == null) {
			DisplayAlertMessage.alertDisplays(22);
			return false;
		}
		return true;
	}

	/***
	 * function to retrieve the contact id.
	 * @throws SQLException
	 */
	public void retrieveContactID() throws SQLException {
		String contacName = comboContact.getSelectionModel().getSelectedItem();

		Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
		String query = "SELECT Contact_ID FROM contacts WHERE Contact_Name='" + contacName + "'";
		ResultSet rst = stmnt.executeQuery(query);

		while (rst.next()) {
			int cId = rst.getInt("Contact_ID");
			setContactId(cId);
		}
		stmnt.close();
	}

	/***
	 * method to select the contact.
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	private void onSelectContact(ActionEvent event) throws SQLException {
		retrieveContactID();
	}
}
