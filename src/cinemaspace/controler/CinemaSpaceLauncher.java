package cinemaspace.controler;


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
import javafx.scene.image.Image;


public class CinemaSpaceLauncher extends Application {	
	@Override
	public void start(Stage stage){
		try {
			LocalConfigurationParameters.retrieveLocalConfiguration();
			CinemaSpaceArchive.openConnection(LocalConfigurationParameters.connectionString);
			String address = new File("target/classes/cinemaspace/view/connection.fxml").getAbsolutePath();
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

