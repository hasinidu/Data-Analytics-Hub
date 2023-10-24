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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RetrievePostController {

	//Labels and textarea in the FXML doc
	@FXML
    private Label postIDField;

    @FXML
    private TextArea contentField;

    @FXML
    private Label authorField;

    @FXML
    private Label likesField;

    @FXML
    private Label sharesField;

    @FXML
    private Label dateTimeField;
    
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	//initialize window with the post details
	@FXML
    public void initialize() {
        String postID = DashboardController.getPostID();

        try {
            File postsFile = new File("Posts.csv");
            List<String> lines = Files.readAllLines(Path.of(postsFile.getPath()));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].equals(postID)) {
                    // Fill the fields with post details
                    postIDField.setText("Post ID : " + parts[0]);
                    contentField.setText(parts[1]);
                    authorField.setText("Author : " +parts[2]);
                    likesField.setText("Likes : " +parts[3]);
                    sharesField.setText("Shares : " +parts[4]);
                    dateTimeField.setText("Date & Time : " +parts[5]);
                    break; // Break when the matching post is found
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
