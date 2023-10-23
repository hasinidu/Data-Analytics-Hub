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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProfileController {

	@FXML
	TextField usernameField;
	@FXML
	TextField fNameField;
	@FXML
	TextField lNameField;
	@FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordField2;
	@FXML
	private Label errorMessage;

	private Stage stage;
	private Scene scene;
	private Parent root;


	@FXML
	public void initialize() {
		//Initialize the text fields when the controller is loaded
		String loggedInUsername = LoginController.getLoggedInUsername();
		usernameField.setText(loggedInUsername);        
		//Fill other text fields
		try {
			File profilesFile = new File("Profiles.csv");
			List<String> lines = Files.readAllLines(Path.of(profilesFile.getPath()));

			for (String line : lines) {
				String[] parts = line.split(",");
				if (parts.length == 4 && parts[0].equals(loggedInUsername)) {
					fNameField.setText(parts[1]);
					lNameField.setText(parts[2]);
					break; //Break when the matching profile is found
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception
		}
	}

	//Load the Dashboard window when the back button is clicked
	public void Dashboard(ActionEvent event) throws IOException {
		//Load the dashboard
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		root = loader.load();

		//Manually call initialize method of DashboardController
		DashboardController dashboardController = loader.getController();
		dashboardController.initialize();

		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}


	//Confirm edit profile changes when clicked
	public void editProfile(ActionEvent event) throws IOException {
		//Check if the new username is unique
		String newUsername = usernameField.getText();
		if (!newUsername.equals(LoginController.getLoggedInUsername()) && usernameExists(newUsername)) {
			errorMessage.setText("Username already exists. Please choose another.");
			return;
		}

		// Update the profile with the new data, keeping the existing password
		String existingPassword = getPasswordForUsername(LoginController.getLoggedInUsername());
		String newProfileData = String.format("%s,%s,%s,%s%n", newUsername, fNameField.getText(), lNameField.getText(), existingPassword);
		System.out.println(newUsername);

		Path profilesFilePath = Path.of("Profiles.csv");

		//Update LoggedInUsername
		//System.out.println(LoginController.getLoggedInUsername());
		LoginController.setLoggedInUsername(newUsername);
		//System.out.println(newUsername);
		//System.out.println(LoginController.getLoggedInUsername());

		try {
			List<String> allLines = Files.readAllLines(profilesFilePath);
			for (int i = 0; i < allLines.size(); i++) {
				String line = allLines.get(i);
				if (line.startsWith(LoginController.getLoggedInUsername() + ",")) {
					allLines.set(i, newProfileData);
					break;
				}
			}
			Files.write(profilesFilePath, allLines);
			errorMessage.setText("Profile successfully updated!");
		} catch (IOException e) {
			errorMessage.setText("Error updating profile data.");
			e.printStackTrace();
		}
	}
	
	@FXML
    public void changePassword(ActionEvent event) throws IOException {
        String oldPassword = passwordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = newPasswordField2.getText();

        //Check if old password matches the current password
        if (!oldPassword.equals(getPasswordForUsername(LoginController.getLoggedInUsername()))) {
            errorMessage.setText("Incorrect old password. Please try again.");
            return;
        }

        //Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            errorMessage.setText("New passwords do not match. Please confirm your new password.");
            return;
        }

        //Update the password in the CSV file
        updatePassword(newPassword);

        errorMessage.setText("Password successfully changed!");
    }


	//Method to change password which is called in changePassword method
    private void updatePassword(String newPassword) {
        String username = LoginController.getLoggedInUsername();
        Path profilesFilePath = Path.of("Profiles.csv");

        try {
            List<String> allLines = Files.readAllLines(profilesFilePath);
            for (int i = 0; i < allLines.size(); i++) {
                String line = allLines.get(i);
                if (line.startsWith(username + ",")) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        parts[3] = newPassword;
                        allLines.set(i, String.join(",", parts));
                        break;
                    }
                }
            }
            Files.write(profilesFilePath, allLines);
        } catch (IOException e) {
            errorMessage.setText("Error updating password.");
            e.printStackTrace(); 
        }
    }

	//Method to get the password for a given username
	private String getPasswordForUsername(String username) {
		File profilesFile = new File("Profiles.csv");
		try {
			return Files.lines(profilesFile.toPath())
					.filter(line -> line.startsWith(username + ","))
					.findFirst()
					.map(line -> {
						String[] parts = line.split(",");
						return (parts.length == 4) ? parts[3] : "";
					})
					.orElse("");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	//Method to check if a username already exists
	private boolean usernameExists(String username) {
		File profilesFile = new File("Profiles.csv");
		try {
			return Files.lines(profilesFile.toPath()).anyMatch(line -> line.startsWith(username + ","));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
