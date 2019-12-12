package interfaceFXML;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.CinemaSpaceArchive;
import main.java.LocalConfigurationParameters;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class CinemaSpaceLauncher extends Application {	
	@Override
	public void start(Stage stage){
		try {
			LocalConfigurationParameters.retrieveLocalConfiguration();
			CinemaSpaceArchive.openConnection(LocalConfigurationParameters.connectionString);
			Parent root = FXMLLoader.load(getClass().getResource("connection.fxml"));
			Scene sceneConnection = new Scene(root);
			stage.setTitle("CinemaSpace");
			stage.setScene(sceneConnection);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("CinemaSpaceIcon.png")));
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

