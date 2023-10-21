package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	//Static variable to store the logged-in username
	private static String loggedInUsername;
	//For the input user name and password an login
	@FXML
	TextField login_username;
	@FXML
	PasswordField login_password;
	@FXML
	private Label errorMessage;

	private Stage stage;
	private Scene scene;
	private Parent root;


	//Load the Sign up window when the sign up button is clicked
	public void SignUpWindow(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("SignUpWindow.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	//When user clicks login checks login credentials
	public void Login(ActionEvent event) throws IOException {

		String username = login_username.getText();
		String password = login_password.getText();

		//Check if the user name and password match in file
		if (credentialsMatch(username, password)) {
			//Load the dashboard
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
			root = loader.load();

			DashboardController dashboardController = loader.getController();

			//Get the combined first and last name from the profile
//			String fullName = getFullName(username);

			//Set welcome message in the dashboard
//			dashboardController.welcomeText(fullName);

			//Set the username
			loggedInUsername = username;

			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		} else {
			// Display an error message if credentials do not match
			errorMessage.setText("Invalid username or password. Please try again.");
		}
	}

	//Method to check if the user name and password match any profile
	private boolean credentialsMatch(String username, String password) {
		File profilesFile = new File("Profiles.csv");
		try {
			return Files.lines(profilesFile.toPath())
					.anyMatch(line -> {
						String[] parts = line.split(",");
						return parts.length == 4 && parts[0].equals(username) && parts[3].equals(password);
					});
		} catch (IOException e) {
			e.printStackTrace();
			//Handle the exception
			return false;
		}
	}

	//Method to get the combined first and last name from the profile to send for the welcome message
//	private String getFullName(String username) {
//		File profilesFile = new File("Profiles.csv");
//		try {
//			return Files.lines(profilesFile.toPath())
//					.filter(line -> line.startsWith(username + ","))
//					.findFirst()
//					.map(line -> {
//						String[] parts = line.split(",");
//						return (parts.length == 4) ? parts[1] + " " + parts[2] : "";
//					})
//					.orElse("");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return "";
//		}
//
//	}

	// Method to get the logged-in username
	public static String getLoggedInUsername() {
		return loggedInUsername;
	}
}
