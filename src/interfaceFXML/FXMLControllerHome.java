package interfaceFXML;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.CinemaSpaceArchive;
import main.java.Film;
import main.java.User;

public class FXMLControllerHome {

	//Switch page controller
	private Parent root;
	private Stage stage;
	
	//Parameters
	private List<Film> listOfFilms;
	private User user;
	
	//Previous and Next number of pages parameters
	private int actualPage;
	private int maxNbPages;
	private int maxNbFilms;
	
	//FXML elements
	@FXML private Button previousButton;
	@FXML private Button nextButton;
	@FXML private Button profile;
	@FXML private TextField searchBar;
	@FXML private Text films;
	@FXML private VBox filmsCollection;
	@FXML private VBox film1;
	@FXML private ImageView posterFilm1;
	@FXML private Text titleFilm1;
	@FXML private VBox film2;
	@FXML private ImageView posterFilm2;
	@FXML private Text titleFilm2;
	@FXML private VBox film3;
	@FXML private ImageView posterFilm3;
	@FXML private Text titleFilm3;
	@FXML private VBox film4;
	@FXML private ImageView posterFilm4;
	@FXML private Text titleFilm4;
	@FXML private VBox film5;
	@FXML private ImageView posterFilm5;
	@FXML private Text titleFilm5;
	@FXML private VBox film6;
	@FXML private ImageView posterFilm6;
	@FXML private Text titleFilm6;
	@FXML private VBox film7;
	@FXML private ImageView posterFilm7;
	@FXML private Text titleFilm7;
	@FXML private VBox film8;
	@FXML private ImageView posterFilm8;
	@FXML private Text titleFilm8;
	
	public FXMLControllerHome() {
		super();
	}
	
	@FXML protected void initialize() {
		if(!previousButton.isDisabled()) {
			previousButton.setDisable(true);
		}
		if(!nextButton.isDisable()) {
			nextButton.setDisable(true);
		}
		actualPage = 0;
		maxNbPages = 0;
		clearListOfFilms();
		
		//TEST
		listOfFilms = new ArrayList<Film>();
	}
	
	@FXML protected void handleHomeButtonAction (ActionEvent event) {
		if(listOfFilms != null) {
			actualPage = 0;
			maxNbPages = 0;
			clearListOfFilms();
			if(!previousButton.isDisabled()) {
				previousButton.setDisable(true);
			}
			if(!nextButton.isDisable()) {
				nextButton.setDisable(true);
			}
		}
		films.setText("Films");
	}
	
	@FXML protected void handleHighestRatedButtonAction (ActionEvent event) {
		listOfFilms = CinemaSpaceArchive.searchFilmsByHighestRatings();
		if(listOfFilms != null) {
			actualPage = 1;
			maxNbFilms = listOfFilms.size();
			maxNbPages = (int) Math.floor(listOfFilms.size()/8);
			if (listOfFilms.size()%8 != 0) {
				maxNbPages++;
			}
			if(!previousButton.isDisabled()) {
				previousButton.setDisable(true);
			}
			if(maxNbPages == 1) {
				nextButton.setDisable(true);
			}
			else {
				nextButton.setDisable(false);
			}
			displayListOfFilms();
			films.setText("Highest Rated Films");
		}
		else {
			actualPage = 0;
			maxNbPages = 0;
			if(!previousButton.isDisabled()) {
				previousButton.setDisable(true);
			}
			if(!nextButton.isDisable()) {
				nextButton.setDisable(true);
			}
		}
	}
	
	@FXML protected void handleMostPopularButtonAction (ActionEvent event) {
		listOfFilms = CinemaSpaceArchive.searchFilmsByHighestNumberOfVisits();
		if(listOfFilms != null) {
			actualPage = 1;
			maxNbFilms = listOfFilms.size();
			maxNbPages = (int) Math.floor(listOfFilms.size()/8);
			if (listOfFilms.size()%8 != 0) {
				maxNbPages++;
			}
			if(!previousButton.isDisabled()) {
				previousButton.setDisable(true);
			}
			if(maxNbPages == 1) {
				nextButton.setDisable(true);
			}
			else {
				nextButton.setDisable(false);
			}
			displayListOfFilms();
			films.setText("Most Popular Films");
		}
		else {
			actualPage = 0;
			maxNbPages = 0;
			if(!previousButton.isDisabled()) {
				previousButton.setDisable(true);
			}
			if(!nextButton.isDisable()) {
				nextButton.setDisable(true);
			}
		}
	}
	
	@FXML protected void handleSearchButtonAction (ActionEvent event) {
		if (!searchBar.getText().isEmpty()) {
			List<String> keywords = new ArrayList<String>();
			keywords.add(searchBar.getText());
			String[] array_keywords = searchBar.getText().split(" ");
			for (int i = 0; i < array_keywords.length; i++) {
				keywords.add(array_keywords[i]);
			}
			listOfFilms = CinemaSpaceArchive.searchFilmsByKeywords(keywords);
			if(listOfFilms != null) {
				actualPage = 1;
				maxNbFilms = listOfFilms.size();
				maxNbPages = (int) Math.floor(listOfFilms.size()/8);
				if (listOfFilms.size()%8 != 0) {
					maxNbPages++;
				}
				if(!previousButton.isDisabled()) {
					previousButton.setDisable(true);
				}
				if(maxNbPages == 1) {
					nextButton.setDisable(true);
				}
				else {
					nextButton.setDisable(false);
				}
				displayListOfFilms();
			}
			else {
				actualPage = 0;
				maxNbPages = 0;
				if(!previousButton.isDisabled()) {
					previousButton.setDisable(true);
				}
				if(!nextButton.isDisable()) {
					nextButton.setDisable(true);
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("No films match your search.");
				alert.showAndWait();
			}
		}
	}
	
	@FXML protected void handleProfileButtonAction (ActionEvent event) {
		if(profile.getText().equals("Profile")) {
			try {
				FXMLLoader load = new FXMLLoader(getClass().getResource("profile.fxml"));
				root = load.load();
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(new Scene(root));
				FXMLControllerProfile controller = load.<FXMLControllerProfile>getController();
				if(user != null) {
					controller.initUser(user);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		else {
			try {
				root = FXMLLoader.load(getClass().getResource("admin.fxml"));
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(new Scene(root));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
	}
	
	@FXML protected void handleDisconnectionButtonAction (ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("connection.fxml"));
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handlePreviousButtonAction (ActionEvent event) {
		nextButton.setDisable(false);
		if(actualPage >= 2) {
			actualPage --;
			if(actualPage == 1){
				previousButton.setDisable(true);
			}
		}
		else {
			previousButton.setDisable(true);
		}
		if(listOfFilms != null) {
			displayListOfFilms();
		}
	}
	
	@FXML protected void handleNextButtonAction (ActionEvent event) {
		previousButton.setDisable(false);
		if(actualPage <= maxNbPages - 1) {
			actualPage ++;
			if(actualPage == maxNbPages) {
				nextButton.setDisable(true);
			}
		}
		else {
			nextButton.setDisable(true);
		}
		if(listOfFilms != null) {
			displayListOfFilms();
		}
	}
	
	@FXML protected void handleFilm1TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm1PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm2TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+1));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm2PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+1));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm3TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+2));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm3PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+2));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm4TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+3));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm4PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+3));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm5TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+4));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm5PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+4));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm6TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+5));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm6PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+5));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm7TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+6));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm7PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+6));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm8TitleAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+7));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm8PosterAction(MouseEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("film.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerMovie controller = load.<FXMLControllerMovie>getController();
			if(listOfFilms != null) {
				controller.initFilm(listOfFilms.get(8*(actualPage-1)+7));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void initUser(User user) {
		this.user = user;
		
		if(user.getAdministrator()) {
			profile.setText("Add film(s)");
		}
	}
	
	public void initListOfFilms(List<Film> listOfFilms) {
		this.listOfFilms = listOfFilms;
	}
	
	public void mostPopular() {
		handleMostPopularButtonAction (new ActionEvent());
	}
	
	public void highestRated() {
		handleHighestRatedButtonAction (new ActionEvent());
	}
	
	public void homePage() {
		handleHomeButtonAction (new ActionEvent());
	}
	
	private void clearListOfFilms() {
		if(listOfFilms != null) {
			listOfFilms.clear();
		}
		film1.setVisible(false);
		film2.setVisible(false);
		film3.setVisible(false);
		film4.setVisible(false);
		film5.setVisible(false);
		film6.setVisible(false);
		film7.setVisible(false);
		film8.setVisible(false);
	}
	
	private void displayListOfFilms() {
		URL url;
		
		//FILM 1
		if(8*(actualPage-1) < maxNbFilms) {
			film1.setVisible(true);
			titleFilm1.setText(listOfFilms.get(8*(actualPage-1)).getTitle());
			titleFilm1.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)).getPosterPath());
				try {
					posterFilm1.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm1.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm1.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film1.setVisible(false);
		}
		
		//FILM 2
		if(8*(actualPage-1)+1 < maxNbFilms) {
			film2.setVisible(true);
			titleFilm2.setText(listOfFilms.get(8*(actualPage-1)+1).getTitle());
			titleFilm2.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+1).getPosterPath());
				try {
					posterFilm2.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm2.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm2.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film2.setVisible(false);
		}
		
		//FILM 3
		if(8*(actualPage-1)+2 < maxNbFilms) {
			film3.setVisible(true);
			titleFilm3.setText(listOfFilms.get(8*(actualPage-1)+2).getTitle());
			titleFilm3.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+2).getPosterPath());
				try {
					posterFilm3.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm3.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm3.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film3.setVisible(false);
		}
		
		//FILM 4
		if(8*(actualPage-1)+3 < maxNbFilms) {
			film4.setVisible(true);
			titleFilm4.setText(listOfFilms.get(8*(actualPage-1)+3).getTitle());
			titleFilm4.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+3).getPosterPath());
				try {
					posterFilm4.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm4.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm4.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film4.setVisible(false);
		}
		
		//FILM 5
		if(8*(actualPage-1)+4 < maxNbFilms) {
			film5.setVisible(true);
			titleFilm5.setText(listOfFilms.get(8*(actualPage-1)+4).getTitle());
			titleFilm5.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+4).getPosterPath());
				try {
					posterFilm5.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm5.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm5.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film5.setVisible(false);
		}
		
		//FILM 6
		if(8*(actualPage-1)+5 < maxNbFilms) {
			film6.setVisible(true);
			titleFilm6.setText(listOfFilms.get(8*(actualPage-1)+5).getTitle());
			titleFilm6.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+5).getPosterPath());
				try {
					posterFilm6.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm6.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm6.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film6.setVisible(false);
		}
		
		//FILM 7
		if(8*(actualPage-1)+6 < maxNbFilms) {
			film7.setVisible(true);
			titleFilm7.setText(listOfFilms.get(8*(actualPage-1)+6).getTitle());
			titleFilm7.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+6).getPosterPath());
				try {
					posterFilm7.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm7.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm7.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film7.setVisible(false);
		}
		
		//FILM 8
		if(8*(actualPage-1)+7 < maxNbFilms) {
			film8.setVisible(true);
			titleFilm8.setText(listOfFilms.get(8*(actualPage-1)+7).getTitle());
			titleFilm8.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilms.get(8*(actualPage-1)+7).getPosterPath());
				try {
					posterFilm8.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					posterFilm8.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				posterFilm8.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		}
		else {
			film8.setVisible(false);
		}
	}
}
