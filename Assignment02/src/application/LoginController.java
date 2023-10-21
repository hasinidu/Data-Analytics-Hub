package application;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	//For the input user name and password an login
	@FXML
	TextField login_username;
	@FXML
	TextField login_password;
	
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
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
		root = loader.load();
		
		DashboardController dashboardcontroller = loader.getController();
		dashboardcontroller.welcomeText(username);

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
}
