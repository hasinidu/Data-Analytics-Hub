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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RetrievePostController {

	//textfields and textarea in the FXML doc
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
                    postIDField.setText(parts[0]);
                    contentField.setText(parts[1]);
                    authorField.setText(parts[2]);
                    likesField.setText(parts[3]);
                    sharesField.setText(parts[4]);
                    dateTimeField.setText(parts[5]);
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
		
		public void RetrievePost(ActionEvent event) throws IOException {
			
		}
	
}
