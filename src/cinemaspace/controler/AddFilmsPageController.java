package cinemaspace.controler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cinemaspace.model.CastMember;
import cinemaspace.model.CinemaSpaceArchive;
import cinemaspace.model.CrewMember;
import cinemaspace.model.Film;
import cinemaspace.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddFilmsPageController {
	//Switch page controller
	private Parent root;
	private Stage stage;
	private User user;
	
	//FXML elements
	@FXML private TextField fileNameField;
	@FXML private Button addFilmsButton;
	@FXML private Button uploadFileButton;
	@FXML private Button confirmButton;
	
	public AddFilmsPageController() {
		super();
	}
	
	@FXML protected void initialize() {
	}
	
	@FXML protected void handleHomeButtonAction (ActionEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/home.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			HomePageController controller = load.<HomePageController>getController();
			controller.homePage();
			if(user != null) {
				controller.initUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@FXML protected void handleHighestRatedButtonAction (ActionEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/home.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			HomePageController controller = load.<HomePageController>getController();
			controller.highestRated();
			if(user != null) {
				controller.initUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleMostPopularButtonAction (ActionEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/home.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			HomePageController controller = load.<HomePageController>getController();
			controller.mostPopular();
			if(user != null) {
				controller.initUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@FXML protected void handleAddFilmsButtonAction (ActionEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/addFilms.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			AddFilmsPageController controller = load.<AddFilmsPageController>getController();
			if(user != null) {
				controller.initUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@FXML protected void handleDisconnectionButtonAction (ActionEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/welcome.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@FXML protected void handleUploadFileButtonAction (ActionEvent event) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
		chooser.getExtensionFilters().add(extFilter);
		File file = chooser.showOpenDialog(stage);
		fileNameField.setText(file.getAbsolutePath());
		
	}

	@FXML protected void handleConfirmAddFilmsButtonAction (ActionEvent event) {
		List<Film> films = new ArrayList<Film>();
		int i = 0;
		try {
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode node = mapper.readTree(new File(fileNameField.getText()));
	        Iterator<JsonNode> nodeFilms = node.iterator();
	        while (nodeFilms.hasNext()) {
	        	i++;
	        	JsonNode film = nodeFilms.next();	        
	        	
	        	double budget = film.get("budget").doubleValue();
	        	
	        	List<String> genres = new ArrayList<String>();
            	Iterator<JsonNode> genresElements = film.get("genres").iterator();
            	while(genresElements.hasNext()) {
            		genres.add(genresElements.next().textValue());
            	}
            	
            	String homePage = film.get("homepage").textValue();
            	String originalLanguage = film.get("original_language").textValue();
            	String originalTitle = film.get("original_title").textValue();
            	String overview = film.get("overview").textValue();
            	String posterPath = film.get("poster_path").textValue();
            	
            	List<String> productionCompanies = new ArrayList<String>();
            	Iterator<JsonNode> pcElements = film.get("production_companies").iterator();
            	while(pcElements.hasNext()) {
            		productionCompanies.add(pcElements.next().textValue());
            	}
            	
            	List<String> productionCountries = new ArrayList<String>();
            	Iterator<JsonNode> pctElements = film.get("production_countries").iterator();
            	while(pctElements.hasNext()) {
            		productionCountries.add(pctElements.next().textValue());
            	}
            	
            	String releaseDate = film.get("release_date").textValue();
            	double revenue = film.get("revenue").doubleValue();
            	double runtime = film.get("runtime").doubleValue();
            	
            	List<String> spokenLanguages = new ArrayList<String>();
            	Iterator<JsonNode> slElements = film.get("spoken_languages").iterator();
            	while(slElements.hasNext()) {
            		spokenLanguages.add(slElements.next().textValue());
            	}
            	
            	String status = film.get("status").textValue();
            	String tagline = film.get("tagline").textValue();
            	String title = film.get("title").textValue();
            	
            	int numberOfVisits = 0;
            	
            	List<String> keywords = new ArrayList<String>();
            	Iterator<JsonNode> keywordElements = film.get("keywords").iterator();
            	while(keywordElements.hasNext()) {
            		keywords.add(keywordElements.next().textValue());
            	}
            	
            	double averageRating = 0;
            	int numberOfRatings = 0;
            	
            	List<CastMember> cast = new ArrayList<CastMember>();
            	Iterator<JsonNode> castElements = film.get("cast").iterator();
            	while(castElements.hasNext()) {
            		JsonNode member = castElements.next();
	                String character = member.get("character").textValue();
	                String name = member.get("name").textValue();
	                int order = member.get("order").intValue();
	                String profilePath = member.get("profile_path").textValue();
	                cast.add(new CastMember(character, name, order, profilePath));
            	}
            	
            	List<CrewMember> crew = new ArrayList<CrewMember>();
            	Iterator<JsonNode> crewElements = film.get("crew").iterator();
            	while(crewElements.hasNext()) {
            		JsonNode member = crewElements.next();
            		String department = member.get("department").textValue();
	                String job = member.get("job").textValue();
	                String name = member.get("name").textValue();
	                String profilePath = member.get("profile_path").textValue();
	                crew.add(new CrewMember(department, job, name, profilePath));
            	}
            	films.add(new Film(null, budget, genres, homePage, originalLanguage, originalTitle, overview, posterPath, 
            			productionCompanies, productionCountries, releaseDate, revenue, runtime, spokenLanguages, 
            			status, tagline, title, numberOfVisits, keywords, cast, crew, averageRating, numberOfRatings));
	        }
	        CinemaSpaceArchive.addFilms(films);
	        fileNameField.setText("");
	        Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialogue");
			alert.setHeaderText(null);
			if(i == 1) {
				alert.setContentText("Your have uploaded " + i + " film in the database.");
			}
			else {
				alert.setContentText("Your have uploaded " + i + " films in the database.");
			}
			alert.showAndWait();
	        
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialogue");
			alert.setHeaderText(null);
			alert.setContentText("An error has occured during the process. Please try again !");
			alert.showAndWait();
	    }
	}
	
	public void initUser(User user) {
		this.user = user;
	}
}
