package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LikedPosts {

	@FXML
	private Label postsField;

	@FXML
	private TextField likeCount;

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

	// Read posts from the file and get the most liked posts
	private List<String> getMostLikedPosts(int numberOfPosts) {
		List<String> mostLikedPosts = new ArrayList<>();

		try {
			File postsFile = new File("Posts.csv");
			BufferedReader reader = new BufferedReader(new FileReader(postsFile));

			// Get all posts, sorted by like count in descending order
			mostLikedPosts = reader.lines()
					.filter(line -> {
						String[] parts = line.split(",");
						if (parts.length >= 4) { // Ensure there are at least 4 columns
							try {
								Integer.parseInt(parts[3]); // Check if the 4th column is a valid number
								return true;
							} catch (NumberFormatException e) {
								return false;
							}
						}
						return false;
					})
					.sorted(Comparator.comparingInt(line -> {
						String[] parts = ((String) line).split(",");
						if (parts.length >= 4) {
							return Integer.parseInt(parts[3]);
						}
						return 0;
					}).reversed())
					.limit(numberOfPosts) // Limit to the specified number of posts
					.collect(Collectors.toList());

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mostLikedPosts;
	}

	// Load Most liked posts
	public void getMostLikedPosts(ActionEvent event) throws IOException {
		// Reset error message
		errorMessage.setText("");

		// Get the number of most liked posts from the input field
		String numberOfPostsStr = likeCount.getText().trim();

		// Check if the input is a valid number
		if (!numberOfPostsStr.matches("\\d+")) {
			errorMessage.setText("Please enter a valid number.");
			return;
		}

		int numberOfPosts = Integer.parseInt(numberOfPostsStr);

		// Get the most liked posts
		List<String> mostLikedPosts = getMostLikedPosts(numberOfPosts);

		// Display most liked posts
		displayPosts(mostLikedPosts);
	}

	private void displayPosts(List<String> posts) {
		StringBuilder stringBuilder = new StringBuilder();

		if (posts.isEmpty()) {
			errorMessage.setText("No posts found with the specified like count.");
		} else {
			for (int i = 0; i < posts.size(); i++) {
				String post = posts.get(i);
				String[] parts = post.split(",");
				if (parts.length >= 6) { // Ensure there are at least 6 columns
					stringBuilder.append((i + 1)).append(". ")
					.append("ID: ").append(parts[0]).append("    ")
					.append("Content: ").append(parts[1]).append("    ")
					.append("Author: ").append(parts[2]).append("    ")
					.append("Likes: ").append(parts[3]).append("    ")
					.append("Shares: ").append(parts[4]).append("    ")
					.append("Date - Time: ").append(parts[5]).append("\n");
				}
			}
		}

		// Set the text of the Label
		postsField.setText(stringBuilder.toString());
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
