package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DashboardController {

	//Static variable to store the input post ID in the dashboard
	private static String inputPostID;

	private Stage stage;
	private Scene scene;
	@FXML
	Label welcomeText;
	@FXML
	TextField postIDField;
	@FXML
	private Label errorMessage;
	@FXML
	private Label upgradeLabel;
	@FXML
	private Label vip;
	@FXML
	private Button upgradeButton;

	@FXML
	public void initialize() {
		//Initialize the welcome text and other details when the controller is loaded
		String loggedInUsername = LoginController.getLoggedInUsername();

		//Set welcome message
		setWelcomeMessage(loggedInUsername);
		
		// Check if the user is a VIP
	    boolean isVIP = checkVIPStatus(loggedInUsername);

	    // If the user is a VIP, hide the upgradeLabel and upgradebutton
	    if (isVIP) {
	        upgradeLabel.setOpacity(0);
	        upgradeButton.setOpacity(0);
	        vip.setText("VIP");
	    }

	}
	
	// Method to check if the user is a VIP
	private boolean checkVIPStatus(String username) {
	    try {
	        File profilesFile = new File(Main.PROFILES_FILE_PATH);
	        List<String> lines = Files.readAllLines(Path.of(profilesFile.getPath()));

	        for (String line : lines) {
	            String[] parts = line.split(",");
	            if (parts.length == 5 && parts[0].equals(username) && "vip".equalsIgnoreCase(parts[4])) {
	                return true; // User is a VIP
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Handle the exception
	    }

	    return false; // User is not a VIP
	}

	// Method to set the welcome message based on the username
	private void setWelcomeMessage(String username) {
		try {
			File profilesFile = new File(Main.PROFILES_FILE_PATH);
			List<String> lines = Files.readAllLines(Path.of(profilesFile.getPath()));

			for (String line : lines) {
				String[] parts = line.split(",");
				if (parts.length == 5 && parts[0].equals(username)) {
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


	// Load the Login window when the LogOut button is clicked and clear Logged username
	public void logout(ActionEvent event) throws IOException {
		// Display a confirmation dialog
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Logout Confirmation");
		alert.setHeaderText("Are you sure you want to log out?");
		alert.setContentText("Click OK to confirm.");

		// Get the user's choice
		ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

		// If the user clicks OK, proceed with logout
		if (result == ButtonType.OK) {
			// Clear the logged-in username
			LoginController.setLoggedInUsername(null);

			// Load the Login window
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		// If the user clicks Cancel, do nothing
	}

	//Load the edit profile window 
	public void editProfile(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	//Load the add post window 
	public void addPost(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("AddPost.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	//Load the retrieve post window 
	public void RetrievePost(ActionEvent event) throws IOException {

		//Get the post ID from the TextField
		String postID = postIDField.getText();

		//Check if the post ID exists or display an error
		if (isPostIDValid(postID)) {
			//Set the postID
			setpostID(postID);

			//Load the Retrieve post window
			Parent root = FXMLLoader.load(getClass().getResource("RetrievePost.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();

		}else {
			//Display an error message
			errorMessage.setText("Post ID does not exist");
		}

	}

	//Load the retrieve post window 
	public void LikedPosts(ActionEvent event) throws IOException {

		//Load the liked post window
		Parent root = FXMLLoader.load(getClass().getResource("LikedPosts.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	//Load the retrieve post window 
	public void RemovePost(ActionEvent event) throws IOException {

		//Get the post ID from the TextField
		String postID = postIDField.getText();

		//Check if the post ID exists or display an error
		if (isPostIDValid(postID)) {
			//Set the postID
			setpostID(postID);

			//Remove the post from the csv file
			if (removePostFromCSV(postID)) {
				// Display success alert
				showAlert(AlertType.WARNING, "Success", "Post Removed Successfully");
			} else {
				// Display an error message
				errorMessage.setText("Failed to remove the post. Please try again.");
			}

		}else {
			//Display an error message
			errorMessage.setText("Post ID does not exist");
		}
	}


	// Export the post to a specified location
	public void ExportPost(ActionEvent event) throws IOException {
		//Get the post ID from the TextField
		String postID = postIDField.getText();

		//Check if the post ID exists or display an error
		if (isPostIDValid(postID)) {
			//Set the postID
			setpostID(postID);

			//Create a FileChooser
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Export Post");
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

			//Show save file dialog
			File file = fileChooser.showSaveDialog(stage);

			if (file != null) {
				//Export the post to the specified file
				if (exportPostToCSV(postID, file)) {
					// Display success alert
					showAlert(AlertType.WARNING, "Success", "Post Exported Successfully");
				} else {
					//Display an error message
					errorMessage.setText("Failed to export the post. Please try again.");
				}
			}
		} else {
			//Display an error message
			errorMessage.setText("Post ID does not exist");
		}
	}

	private boolean exportPostToCSV(String postID, File file) {
		try {
			File postsFile = new File(Main.POSTS_FILE_PATH);

			//Create a temporary list to store the lines of the post to be exported
			List<String> postLines = new ArrayList<>();

			//Add the header row to the list
			postLines.add("ID,content,author,likes,shares,date-time");

			try (BufferedReader reader = new BufferedReader(new FileReader(postsFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					//Add the line if it contains the post ID to be exported
					if (parts.length > 0 && parts[0].equals(postID)) {
						postLines.add(line);
					}
				}
			}

			//Write the post lines to the specified file
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (String postLine : postLines) {
					writer.write(postLine);
					writer.newLine();
				}
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//Method to remove the post from the CSV file
	private boolean removePostFromCSV(String postID) {
		try {
			File postsFile = new File(Main.POSTS_FILE_PATH);

			//Create a temporary list to store lines without the post to be removed
			List<String> updatedLines = new ArrayList<>();

			try (BufferedReader reader = new BufferedReader(new FileReader(postsFile))) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(",");
					//Skip the line if it contains the post ID to be removed
					if (parts.length > 0 && parts[0].equals(postID)) {
						continue;
					}
					updatedLines.add(line);
				}
			}

			//Write the updated lines directly to the original file
			Files.write(postsFile.toPath(), updatedLines, StandardCharsets.UTF_8);

			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void upgradeProfile(ActionEvent event) throws IOException {
	    String loggedInUsername = LoginController.getLoggedInUsername();

	    //Check if the user is already a VIP
	    if (checkVIPStatus(loggedInUsername)) {
	        // If already a VIP, do nothing
	        return;
	    }

	    //Display a confirmation dialog
	    Alert confirmUpgrade = new Alert(AlertType.CONFIRMATION);
	    confirmUpgrade.setTitle("Upgrade Confirmation");
	    confirmUpgrade.setHeaderText("Would you like to subscribe to the application for a monthly fee of $0?");
	    confirmUpgrade.setContentText("Click OK to confirm.");

	    //Get the user's choice
	    ButtonType result = confirmUpgrade.showAndWait().orElse(ButtonType.CANCEL);

	    //If the user clicks OK, proceed with the upgrade
	    if (result == ButtonType.OK) {
	        //Update the user's profile with VIP status
	        Path profilesFilePath = Path.of(Main.PROFILES_FILE_PATH);

	        try {
	            List<String> allLines = Files.readAllLines(profilesFilePath);

	            for (int i = 0; i < allLines.size(); i++) {
	                String line = allLines.get(i);
	                String[] parts = line.split(",");
	                if (parts.length == 5 && parts[0].equals(loggedInUsername)) {

	                    // Update the line to include VIP status
	                    parts = new String[]{parts[0], parts[1], parts[2], parts[3], "vip"};
	                    allLines.set(i, String.join(",", parts));
	                    break;
	                }
	            }

	            Files.write(profilesFilePath, allLines);
	        } catch (IOException e) {
	            errorMessage.setText("Error updating profile. Please try again.");
	            e.printStackTrace();
	            // Handle the exception
	        }


	        //Display an informational alert
	        Alert infoAlert = new Alert(AlertType.INFORMATION);
	        infoAlert.setTitle("VIP Subscription Success");
	        infoAlert.setHeaderText(null);
	        infoAlert.setContentText("Please log out and log in again to access VIP functionalities.");
	        infoAlert.showAndWait();
	    }
	    //If the user clicks Cancel, do nothing
	}


	//method to check if post ID exists in file
	private boolean isPostIDValid(String postID) {
		try {
			File postsFile = new File(Main.POSTS_FILE_PATH);
			List<String> lines = Files.readAllLines(Path.of(postsFile.getPath()));

			for (String line : lines) {
				String[] parts = line.split(",");
				if (parts.length > 0 && parts[0].equals(postID)) {
					return true; //Post ID exists
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			//Handle the exception
		}

		return false;
	}

	//Method to get the input postID
	public static String getPostID() {
		return inputPostID;
	}

	//Method to Set the input postID
	public static void setpostID(String postID) {
		inputPostID = postID;
	}

	//Method to display an alert box
	private void showAlert(AlertType alertType, String title,String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	


}
