package entity;

import javafx.scene.control.Alert;

public class DisplayAlertMessage {

	/**
	 * Displays an error alert with the given title, header, and content text.
	 *
	 * @param alertTitle   the title of the alert
	 * @param header       the header text of the alert
	 * @param alertContext the content text of the alert
	 */
	public static void errorAlert(String alertTitle, String header, String alertContext) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(alertTitle);
		alert.setHeaderText(header);
		alert.setContentText(alertContext);
		alert.showAndWait();
	}

	/**
	 * Displays an information alert with the given title, header, and content text.
	 *
	 * @param infoTitle the title of the alert
	 * @param header    the header text of the alert
	 * @param context   the content text of the alert
	 */
	public static void informationAlert(String infoTitle, String header, String context) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(infoTitle);
		alert.setHeaderText(header);
		alert.setContentText(context);
		alert.showAndWait();
	}

	/**
	 * Displays an alert with the appropriate title, header text, and content text
	 * based on the given alert number.
	 *
	 * @param alertNumber the number corresponding to the alert to be displayed
	 */
	public static void alertDisplays(int alertNumber) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);

		switch (alertNumber) {


			case 1:
				alert.setTitle("Error");
				alert.setHeaderText("Please fill in all required fields");
				alert.setContentText("Field name is empty");
				alert.showAndWait();
				break;
			case 2:
				alert.setTitle("Error");
				alert.setHeaderText("Please fill in all required fields");
				alert.setContentText("Address field is empty");
				alert.showAndWait();
				break;
			case 3:
				alert.setTitle("Error");
				alert.setHeaderText("Please fill in all required fields");
				alert.setContentText("City must be selected");
				alert.showAndWait();
				break;
			case 4:
				alert.setTitle("Error");
				alert.setHeaderText("Please fill in all required fields");
				alert.setContentText("Postal code field is empty");
				alert.showAndWait();
				break;
			case 5:
				alert.setTitle("Error");
				alert.setHeaderText("Please fill in all required fields");
				alert.setContentText("Phone number field is empty");
				alert.showAndWait();
				break;
			case 6:
				alert.setTitle("Saved CustomerEntity");
				alert.setHeaderText("CustomerEntity Saved to the Database");
				alert.setContentText("New customer has been saved");
				alert.showAndWait();
				break;
			case 7:
				alert.setTitle("Modified CustomerEntity");
				alert.setHeaderText("CustomerEntity Modified ");
				alert.setContentText("CustomerEntity has been modified and saved");
				alert.showAndWait();
				break;
			case 8:
				alert.setTitle("Deleted CustomerEntity");
				alert.setHeaderText("Are you sure you want to delete this customer?");
				alert.setContentText("Deleting the customer will remove them and their appointments");
				alert.showAndWait();
				break;
			case 9:
				alert.setTitle("No Highlighted CustomerEntity");
				alert.setHeaderText("CustomerEntity was not highlighted");
				alert.setContentText("Please highlight a customer");
				alert.showAndWait();
				break;
			case 10:
				alert.setTitle("No Country Selected");
				alert.setHeaderText("Country field cannot me empty");
				alert.setContentText("Please select a country");
				alert.showAndWait();
				break;
			case 11:
				alert.setTitle("CustomerEntity Deleted");
				alert.setHeaderText("CustomerEntity was deleted from the the database successfully.");
				alert.setContentText("All customer information and appointments were deleted.");
				alert.showAndWait();
				break;
			case 12:
				alert.setTitle("EntityUser Id Not Found");
				alert.setHeaderText("Couldn't find User_Id from that username");
				alert.setContentText("Please enter a valid username");
				alert.showAndWait();
				break;
			case 13:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Title field is empty.");
				alert.showAndWait();
				break;
			case 14:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Description field is empty.");
				alert.showAndWait();
				break;
			case 15:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Location field is empty.");
				alert.showAndWait();
				break;
			case 16:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Type field is empty.");
				alert.showAndWait();
				break;
			case 17:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Date picker is empty.");
				alert.showAndWait();
				break;
			case 18:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Start time field is empty.");
				alert.showAndWait();
				break;
			case 19:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("End time field is empty.");
				alert.showAndWait();
				break;
			case 20:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Existing customer field is empty.");
				alert.showAndWait();
				break;
			case 21:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("EntityUser ID field is empty.");
				alert.showAndWait();
				break;
			case 22:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Contact is empty.");
				alert.showAndWait();
				break;
			case 23:
				alert.setTitle("Saved Appointment");
				alert.setHeaderText("Appointment Saved to the Database");
				alert.setContentText("New Appointment has been saved");
				alert.showAndWait();
				break;
			case 24:
				alert.setTitle("Error");
				alert.setHeaderText("Appointment Cannot End Before It Starts");
				alert.setContentText("End time must be after start time.");
				alert.showAndWait();
				break;
			case 25:
				alert.setTitle("Error");
				alert.setHeaderText("Appointment Cannot Start At the Same Time It Ends.");
				alert.setContentText("Please select a end time that is after the start time.");
				alert.showAndWait();
				break;
			case 26:
				alert.setTitle("Error");
				alert.setHeaderText("Appointment Collision");
				alert.setContentText("A customer already has an appointment with this time. Please select another time.");
				alert.showAndWait();
				break;
			case 28:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Fields cannot be empty");
				alert.showAndWait();
				break;
			case 29:
				alert.setTitle("Error");
				alert.setHeaderText("Appointment Outside of Business Hours");
				alert.setContentText("Appointment must be within business hours.");
				alert.showAndWait();
				break;

			case 30:
				alert.setTitle("No Type Selected");
				alert.setHeaderText("Type field cannot me empty");
				alert.setContentText("Please select a type");
				alert.showAndWait();
				break;

			case 31:
				alert.setTitle("No Academic Level Selected");
				alert.setHeaderText("Academic Level field cannot me empty");
				alert.setContentText("Please select an Academic Level");
				alert.showAndWait();
				break;

			case 32:
				alert.setTitle("No Medical Department Selected");
				alert.setHeaderText("Medical Department field cannot me empty");
				alert.setContentText("Please select a Medical Department");
				alert.showAndWait();
				break;

			case 33:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Course field is empty.");
				alert.showAndWait();
				break;

			case 34:
				alert.setTitle("Error");
				alert.setHeaderText("All Fields Required");
				alert.setContentText("Medical History field is empty.");
				alert.showAndWait();
				break;

		}
	}
}
