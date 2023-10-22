package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {

	private Stage stage;
	private Scene scene;
	@FXML
	Label welcomeText;

	@FXML
	public void initialize() {
		//Initialize the welcome text and other details when the controller is loaded
		String loggedInUsername = LoginController.getLoggedInUsername();

		//Set welcome message
		setWelcomeMessage(loggedInUsername);

	}

	// Method to set the welcome message based on the username
	private void setWelcomeMessage(String username) {
		try {
			File profilesFile = new File("Profiles.csv");
			List<String> lines = Files.readAllLines(Path.of(profilesFile.getPath()));

			for (String line : lines) {
				String[] parts = line.split(",");
				if (parts.length == 4 && parts[0].equals(username)) {
					String firstName = parts[1];
					String lastName = parts[2];
					//Set the welcome message with the first name and last name
					welcomeText.setText("Welcome " + firstName + " " + lastName);
					break; 
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception
		}
	}

	//Load the Login window when the LogOut button is clicked and clear Logged username
	public void logout(ActionEvent event) throws IOException {
		//Clear the logged-in username
		LoginController.setLoggedInUsername(null);

		//Load the Login window
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	//Load the Login up window when the back button is clicked
	public void EditProfile(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}


}
