package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddpostsController {

	@FXML
    private TextField postIDField;

    @FXML
    private TextArea contentField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField likesField;

    @FXML
    private TextField sharesField;

    @FXML
    private TextField dateTimeField;

    @FXML
    private Label errorMessage;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
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
		
		public void addPost(ActionEvent event) throws IOException {
		    try {
		        //Validate input fields
		        if (!validateFields()) {
		            return;
		        }

		        //Check if the post ID is unique
		        if (!isPostIDUnique(postIDField.getText())) {
		            errorMessage.setText("Post ID must be unique.");
		            return;
		        }


		        //Create a string representing the new post
		        String newPost = String.format("%s,%s,%s,%s,%s,%s%n", postIDField.getText(),
		                contentField.getText(), authorField.getText(), likesField.getText(),
		                sharesField.getText(), dateTimeField.getText());

		        //Append the new post to the "Posts.csv" file
		        Path postsFilePath = Path.of("Posts.csv");
		        Files.writeString(postsFilePath, newPost, StandardOpenOption.APPEND);

		     // Display success alert
		        showAlert(AlertType.WARNING, "Success", "Post added");
		    } catch (IOException e) {
		        errorMessage.setText("Error adding post.");
		        e.printStackTrace();
		    }
		}
		
		private boolean validateFields() {
	        //Validate likes and shares fields as non-negative integers
	        if (!isValidNonNegativeInteger(likesField.getText()) || !isValidNonNegativeInteger(sharesField.getText())) {
	            errorMessage.setText("Likes and shares must be positive numbers.");
	            return false;
	        }

	        //Validate date-time format
	        if (!isValidDateTimeFormat(dateTimeField.getText())) {
	            errorMessage.setText("Invalid date-time format. Use DD/MM/YYYY HH:MM.");
	            return false;
	        }

	        return true;
	    }

	    private boolean isValidNonNegativeInteger(String value) {
	        try {
	            int intValue = Integer.parseInt(value);
	            return intValue >= 0;
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }

	    private boolean isValidDateTimeFormat(String value) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
	        dateFormat.setLenient(false);

	        try {
	            Date date = dateFormat.parse(value);
	            return date != null;
	        } catch (ParseException e) {
	            return false;
	        }
	    }

	    private boolean isPostIDUnique(String postID) throws IOException {
	        File postsFile = new File("Posts.csv");
	        List<String> lines = Files.readAllLines(postsFile.toPath());

	        for (String line : lines) {
	            String[] parts = line.split(",");
	            if (parts.length > 0 && parts[0].equals(postID)) {
	                return false; 
	                //If Post ID is not unique
	            }
	        }

	        return true; 
	        //If Post ID is unique
	    }
	    
	  //Method to display an alert box
		private void showAlert(AlertType alertType, String title,String content) {
		    Alert alert = new Alert(alertType);
		    alert.setTitle(title);
		    alert.setHeaderText(null);
		    alert.setContentText(content);
		    alert.showAndWait();
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

		
}
