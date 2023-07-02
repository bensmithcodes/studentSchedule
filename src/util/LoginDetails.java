/**

This class is responsible for saving login attempts details into a text file
*/
package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

public class LoginDetails {
	/** saving details */
	private static final String FILE_NAME = "login_activity.txt";

	/**
	 * this method saves the details of a logged in attempt into a text file.
	 * 
	 * @param username The username attempted to log in.
	 * @param loggedIn True if the login attempt was successful, False if it failed.
	 * @param message  A message describing the result of the login attempt.
	 */
	public static void saveLoggedInAttempt(String username, boolean loggedIn, String message) {
		try (FileWriter fwriter = new FileWriter(FILE_NAME, true);
				BufferedWriter buffWriter = new BufferedWriter(fwriter);
				PrintWriter printWriter = new PrintWriter(buffWriter)) {
			printWriter.println("Username " + username + " was a" + (loggedIn ? " success!" : " failure.") + " "
					+ message + " " + Instant.now().toString());
		} catch (IOException e) {
			System.out.println("Log In Error: " + e.getMessage());
		}
	}
}
