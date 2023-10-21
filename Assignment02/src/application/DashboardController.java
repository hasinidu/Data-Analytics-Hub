package application;

import java.io.IOException;

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

	public void welcomeText(String username) {
		welcomeText.setText("Welcome " + username);
	}

	//Load the Login up window when the back button is clicked
	public void LoginWindow(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
}
