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
import java.io.IOException;
import java.net.URL;

import util.*;
import java.sql.*;
import java.util.*;

import entity.*;

public class UpdateCustomer implements Initializable {

	@FXML
	public Button btnUpdate, btnCancel;

	public TextField nameTxt, addressTxt, postalTxt, phoneTxt, cidTxt;
	public ComboBox<String> cityCombo, countryCombo;

	public int cityDivId;

	ObservableList<String> cities_List = FXCollections.observableArrayList();

	ObservableList<String> countries_list = FXCollections.observableArrayList("U.S", "Canada", "UK");

	CustomerEntity customerSelected;

	Parent scene;

	Stage updateCustomerStage;

	@Override
	public void initialize(URL url, ResourceBundle resBundle) {
		countryCombo.setItems(countries_list);
		customerSelected = CustomersDashboard.getCurrentSelectedCustomers();

		cidTxt.setText(String.valueOf(customerSelected.getCustomerID()));
		nameTxt.setText(customerSelected.getCustomerName());
		addressTxt.setText(customerSelected.getAddress());
		postalTxt.setText(customerSelected.getPostalCode());
		phoneTxt.setText(customerSelected.getPhoneNumber());
		countryCombo.setValue(customerSelected.getCountry());
		cityCombo.setValue(customerSelected.getCity());
	}

	public void changeWindow(ActionEvent event, String resString) throws IOException {
		updateCustomerStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		scene = FXMLLoader.load(getClass().getResource(resString));
		updateCustomerStage.setScene(new Scene(scene));
		updateCustomerStage.show();
	}

	/***
	 * Update method is used to update the customer information.
	 * @param event
	 * @throws IOException
	 * @throws SQLException
	 */
	@FXML
	public void onUpdate(ActionEvent event) throws IOException, SQLException {
		String customerName = nameTxt.getText();
		String customerAddress = addressTxt.getText();
		String customerPostalCode = postalTxt.getText();
		String customerPhone = phoneTxt.getText();

		SQLQueries.updateCustomersTable(customerSelected.getCustomerID(), customerName, customerAddress,
				customerPostalCode, customerPhone, CityDivisionID.divisionID);
		changeWindow(event, "/view/Customers.fxml");

	}

	/***
	 * used to cancel the function
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void onCancel(ActionEvent event) throws IOException {
		Alert alertCancel = new Alert(Alert.AlertType.CONFIRMATION);
		alertCancel.setTitle("Cancel");
		alertCancel.setHeaderText("Are you sure you want to cancel?");
		alertCancel.setContentText("This will not change any information");
		Optional<ButtonType> selectCancel = alertCancel.showAndWait();

		if (selectCancel.isPresent() && selectCancel.get() == ButtonType.OK) {
			changeWindow(event, "/view/Customers.fxml");
		}
	}

	/***
	 * choose city combo to select the city
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	public void onChooseCity(ActionEvent event) throws SQLException {
		String citySelected = cityCombo.getSelectionModel().getSelectedItem();
		getCityDivisionId(citySelected);
		CityDivisionID.divisionID = cityDivId;
	}

	/***
	 * method used to select the city division.
	 * @param selectedCity
	 * @throws SQLException
	 */
	public void getCityDivisionId(String selectedCity) throws SQLException {
		Statement statement = JavaDataBaseConnection.getConnection().createStatement();
		String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + selectedCity + "'";
		ResultSet rst = statement.executeQuery(sql);

		while (rst.next()) {
			cityDivId = rst.getInt("Division_ID");
		}
	}

	/***
	 * function used to choose the certain country.
	 * @param event
	 * @throws SQLException
	 */
	public void onChooseCountry(ActionEvent event) throws SQLException {
		String countrySelect = countryCombo.getSelectionModel().getSelectedItem();
		cities_List.clear();
		if (countrySelect.equals("U.S")) {
			Statement statement = JavaDataBaseConnection.getConnection().createStatement();
			String sqlCities = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1";
			ResultSet rst = statement.executeQuery(sqlCities);

			while (rst.next()) {
				String city = rst.getString("Division");
				cities_List.add(city);
				cityCombo.setItems(cities_List);
			}
			statement.close();

		} else if (countrySelect.equals("UK")) {
			Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
			String sqlCities = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2";
			ResultSet rst = stmnt.executeQuery(sqlCities);

			while (rst.next()) {
				String city = rst.getString("Division");
				cities_List.add(city);
				cityCombo.setItems(cities_List);
			}
			stmnt.close();

		} else {
			Statement stmnt = JavaDataBaseConnection.getConnection().createStatement();
			String sqlCities = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3";
			ResultSet rst = stmnt.executeQuery(sqlCities);

			while (rst.next()) {
				String city = rst.getString("Division");
				cities_List.add(city);
				cityCombo.setItems(cities_List);
			}
			stmnt.close();
		}
	}
}
