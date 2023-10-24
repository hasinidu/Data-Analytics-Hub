package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import javafx.scene.control.TextField;
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
	public void initialize() {
		//Initialize the welcome text and other details when the controller is loaded
		String loggedInUsername = LoginController.getLoggedInUsername();

		//Set welcome message
		setWelcomeMessage(loggedInUsername);

	}

	// Method to set the welcome message based on the username
	private void setWelcomeMessage(String username) {
		try {
			File profilesFile = new File(Main.PROFILES_FILE_PATH);
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
	public void RemovePost(ActionEvent event) throws IOException {

		//Get the post ID from the TextField
		String postID = postIDField.getText();

		//Check if the post ID exists or display an error
				if (isPostIDValid(postID)) {
					//Set the postID
					setpostID(postID);
					
					//Remove the post from the csv file
					if (removePostFromCSV(postID)) {
		                // Display a success message or perform other actions
		                System.out.println("Post removed successfully!");
		            } else {
		                // Display an error message
		                errorMessage.setText("Failed to remove the post. Please try again.");
		            }
					
				}else {
					//Display an error message
					errorMessage.setText("Post ID does not exist");
				}
	}

	//method to check if post ID exists in file
	private boolean isPostIDValid(String postID) {
		try {
			File postsFile = new File("Posts.csv");
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

	// Method to remove the post from the CSV file
    private boolean removePostFromCSV(String postID) {
        try {
            File postsFile = new File(Main.POSTS_FILE_PATH);
            File tempFile = new File("temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(postsFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Skip the line if it contains the post ID to be removed
                if (parts.length > 0 && parts[0].equals(postID)) {
                    continue;
                }
                writer.write(line + "\n");
            }

            reader.close();
            writer.close();

            // Replace the original file with the updated file
            if (tempFile.renameTo(postsFile)) {
                return true;
            } else {
                // Failed to rename the file
                System.out.println("Failed to rename the file.");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	//Method to get the input postID
	public static String getPostID() {
		return inputPostID;
	}

	//Method to Set the input postID
	public static void setpostID(String postID) {
		inputPostID = postID;
	}


}
