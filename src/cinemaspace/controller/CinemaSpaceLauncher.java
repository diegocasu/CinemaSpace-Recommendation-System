package cinemaspace.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cinemaspace.model.CinemaSpaceArchive;
import cinemaspace.model.LocalConfigurationParameters;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;


public class CinemaSpaceLauncher extends Application {	
	@Override
	public void start(Stage stage){
		try {
			boolean configurationRetrievalSuccess = LocalConfigurationParameters.retrieveLocalConfiguration();
			
			if(!configurationRetrievalSuccess) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("There was a problem retrieving the local configuration. Please check its format");
				alert.showAndWait();
				
				System.exit(1);
			}
			
			CinemaSpaceArchive.openConnection(LocalConfigurationParameters.connectionString);
			String address = new File("target/classes/cinemaspace/view/welcome.fxml").getAbsolutePath();
			Parent root = new FXMLLoader(new File(address).toURI().toURL()).load();
			Scene sceneConnection = new Scene(root);
			stage.setTitle("CinemaSpace");
			stage.setScene(sceneConnection);
			InputStream is = new FileInputStream("target/classes/cinemaspace/view/CinemaSpaceIcon.png");
			stage.getIcons().add(new Image(is));
			stage.setOnCloseRequest((WindowEvent event)-> {CinemaSpaceArchive.closeConnection();});
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

