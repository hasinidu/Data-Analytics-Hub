package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class addpostsController {

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
			
		}
		
		
}
