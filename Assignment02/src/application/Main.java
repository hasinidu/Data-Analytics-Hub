package application;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

	public static final String PROFILES_FILE_PATH = "Profiles.csv";
	public static final String POSTS_FILE_PATH = "Posts.csv";
	
	@Override
	public void start(Stage primaryStage) {
		try {

			//Check if the "Profiles.csv" file exists
			File profilesFile = new File(PROFILES_FILE_PATH);

			if (!profilesFile.exists()) {
				//Create the file if the file doesn't exist
				try {
					profilesFile.createNewFile();
					System.out.println("Profiles.csv file created.");
				} catch (IOException e) {
					System.err.println("Error creating Profiles.csv file: " + e.getMessage());
					
				}
			}
			
			//Check if the "Posts.csv" file exists
            File postsFile = new File(POSTS_FILE_PATH);

            if (!postsFile.exists()) {
                //Create the file if the file doesn't exist
                try {
                    postsFile.createNewFile();
                    System.out.println("Posts.csv file created.");
                } catch (IOException e) {
                    System.err.println("Error creating Posts.csv file: " + e.getMessage());
                  
                }
            }

			//Load the main Login window when the application starts
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
