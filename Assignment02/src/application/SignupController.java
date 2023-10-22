package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {

	@FXML
	private TextField usernameField;
	@FXML
	private TextField fNameField;
	@FXML
	private TextField lNameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label errorMessage;

	private Stage stage;
	private Scene scene;

	//Load the Login up window when the back button is clicked or profile is created
	public void LoginWindow(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	//Sign up
	public void Signup(ActionEvent event) throws IOException {

		String username = usernameField.getText();
		String fName = fNameField.getText();
		String lName = lNameField.getText();
		String password = passwordField.getText();

		//Check if the user name already exists
		if (usernameExists(username)) {
			errorMessage.setText("Username already exists. Please choose another Username.");

			return;
		}

		//Store the new profile in Profiles.csv
		String profileData = String.format("%s,%s,%s,%s%n", username, fName, lName, password);
		Path profilesFilePath = Path.of("Profiles.csv");

		try {
			Files.writeString(profilesFilePath, profileData, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
			

			// Display success alert
	        showAlert(AlertType.INFORMATION, "Profile Successfully Created", "Congratulations!",
	                "Your profile has been successfully created.");

	        // Clear fields and go back to login
	        clearFields();
	        LoginWindow(event);
	        
		} catch (IOException e) {
			errorMessage.setText("Error storing profile data.");
			e.printStackTrace(); 
			//Handle the exception
		}
	}

	//Method to check if user name already exists
	private boolean usernameExists(String username) {
		File profilesFile = new File("Profiles.csv");
		try {
			return Files.lines(profilesFile.toPath())
					.anyMatch(line -> line.startsWith(username + ","));
		} catch (IOException e) {
			e.printStackTrace(); 
			//Handle the exception
			return false;
		}
	}
	
	//Method to display an alert box
	private void showAlert(AlertType alertType, String title, String header, String content) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    alert.showAndWait();
	}

	//Method to clear input fields and error message
	private void clearFields() {
		usernameField.clear();
		fNameField.clear();
		lNameField.clear();
		passwordField.clear();
		errorMessage.setText("");
	}

}
