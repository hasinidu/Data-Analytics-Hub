package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ShareChartController {

	@FXML
	private PieChart pieChart;

	private Stage stage;
	private Scene scene;
	private Parent root;

	//initialize with piechart information
	@FXML
	public void initialize() {

		//Read the shares data from the posts.csv file
		List<Integer> sharesList;
		try {
			sharesList = readSharesData();
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(Alert.AlertType.ERROR, "Error", "Failed to read shares data.");
			return;
		}

		//Categorize shares into groups
		long shares0to99 = sharesList.stream().filter(share -> share >= 0 && share <= 99).count();
		long shares100to999 = sharesList.stream().filter(share -> share >= 100 && share <= 999).count();
		long shares1000andAbove = sharesList.stream().filter(share -> share >= 1000).count();

		//Create a pie chart
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("0-99 Shares", shares0to99),
				new PieChart.Data("100-999 Shares", shares100to999),
				new PieChart.Data("1000+ Shares", shares1000andAbove)
				);

		//Set the pie chart data
		pieChart.setData(pieChartData);
		pieChart.setTitle("Share Distribution");
		
	}

	//Read shares data from the posts.csv file
	private List<Integer> readSharesData() throws IOException {
		Path postsFilePath = Path.of(Main.POSTS_FILE_PATH);
		return Files.lines(postsFilePath)
				.skip(1) // Skip header line
				.map(line -> line.split(","))
				.filter(parts -> parts.length >= 5) 
				.map(parts -> Integer.parseInt(parts[4])) //Taking shares from 5th column
				.collect(Collectors.toList());
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

	// Display an alert box
	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}



}
