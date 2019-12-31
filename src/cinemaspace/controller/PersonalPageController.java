package cinemaspace.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.bson.types.ObjectId;

import cinemaspace.model.CinemaSpaceArchive;
import cinemaspace.model.Film;
import cinemaspace.model.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PersonalPageController {
	//Switch page controller
	private Parent root;
	private Stage stage;
		
	//Parameter
	private User user;
	private List<Film> listOfFilmsGenres;
	private List<String> listOfFilmsTitlesGenres;
	private List<Film> listOfFilmsUsers;
	private List<String> listOfFilmsTitlesUsers;
	
	//Previous and Next number of pages parameters - Genres
	private int actualPageGenres;
	private int maxNbPagesGenres;
	private int maxNbFilmsGenres;
	
	//Previous and Next number of pages parameters - Users
	private int actualPageUsers;
	private int maxNbPagesUsers;
	private int maxNbFilmsUsers;
	
	//FXML elements
	@FXML private Text username;
	@FXML private Text email;
	@FXML private Text gender;
	@FXML private Text dateOfBirth;
	@FXML private TextField minField;
	@FXML private TextField maxField;
	@FXML private PieChart pieChart;
	
	@FXML private VBox film1Genres;
	@FXML private ImageView posterFilm1Genres;
	@FXML private Text titleFilm1Genres;
	@FXML private VBox film2Genres;
	@FXML private ImageView posterFilm2Genres;
	@FXML private Text titleFilm2Genres;
	@FXML private VBox film3Genres;
	@FXML private ImageView posterFilm3Genres;
	@FXML private Text titleFilm3;
	@FXML private VBox film4Genres;
	@FXML private ImageView posterFilm4Genres;
	@FXML private Text titleFilm4;
	@FXML private Button previousButtonGenres;
	@FXML private Button nextButtonGenres;
	
	@FXML private VBox film1Users;
	@FXML private ImageView posterFilm1Users;
	@FXML private Text titleFilm1Users;
	@FXML private VBox film2Users;
	@FXML private ImageView posterFilm2Users;
	@FXML private Text titleFilm2Users;
	@FXML private VBox film3Users;
	@FXML private ImageView posterFilm3Users;
	@FXML private Text titleFilm3Users;
	@FXML private VBox film4Users;
	@FXML private ImageView posterFilm4Users;
	@FXML private Text titleFilm4Users;
	@FXML private Button previousButtonUsers;
	@FXML private Button nextButtonUsers;
	
	private double min;
	private double max;
	private Map<String, Integer> pieElement;
	
	public PersonalPageController() {
		super();
	}
	
	@FXML protected void initialize() {
		pieChart.setVisible(false);
		
		if(!previousButtonGenres.isDisabled()) {
			previousButtonGenres.setDisable(true);
		}
		if(!nextButtonGenres.isDisable()) {
			nextButtonGenres.setDisable(true);
		}
		
		actualPageGenres = 0;
		maxNbPagesGenres = 0;
		clearListOfFilmsGenres();
		
		if(!previousButtonUsers.isDisabled()) {
			previousButtonUsers.setDisable(true);
		}
		if(!nextButtonUsers.isDisable()) {
			nextButtonUsers.setDisable(true);
		}
		
		actualPageUsers = 0;
		maxNbPagesUsers = 0;
		clearListOfFilmsUsers();
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
	
	@FXML protected void handleProfileButtonAction (ActionEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/profile.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			PersonalPageController controller = load.<PersonalPageController>getController();
			controller.initUser(user);
			if(user != null) {
				controller.initUser(user);
				controller.initListOfFilmsGenres(user);
				controller.initListOfFilmsUsers(user);
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
	
	@FXML protected void handleCloseAccountButtonAction (ActionEvent event) {
		if(CinemaSpaceArchive.deleteUser(user)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialogue");
			alert.setHeaderText(null);
			alert.setContentText("Your account has been successfully closed.");
			alert.showAndWait();
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
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialogue");
			alert.setHeaderText(null);
			alert.setContentText("A problem occurred during the closure of your account. Please try again !");
			alert.showAndWait();
		}
	}
	
	@FXML protected void handleConfirmPieChartButtonAction (ActionEvent event) {		
		if(minField.getText().isEmpty()) {
			minField.setText("0");
		}
		if(maxField.getText().isEmpty()) {
			maxField.setText("5");
		}
		else if(!minField.getText().isEmpty() && !maxField.getText().isEmpty()) {
			if (isValidValueMinMax(minField.getText()) && isValidValueMinMax(maxField.getText())) {
				min = Double.parseDouble(minField.getText());
				max = Double.parseDouble(maxField.getText());
				if(min > max) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialogue");
					alert.setHeaderText(null);
					alert.setContentText("Your minimum value must be between 0 and 5 and must be lower than the maximum value.");
					alert.showAndWait();
					minField.setText("");
					maxField.setText("");
				}
				else if(min < 0 || min > 5) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialogue");
					alert.setHeaderText(null);
					alert.setContentText("Your minimum value must be between 0 and 5 and must be lower than the maximum value.");
					alert.showAndWait();
					minField.setText("");
				}
				else if(max < 0 || max > 5) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialogue");
					alert.setHeaderText(null);
					alert.setContentText("Your minimum value must be between 0 and 5 and must be lower than the maximum value.");
					alert.showAndWait();
					maxField.setText("");
				}
				else {
					pieChart.setVisible(true);
					pieChart.setTitle("Most Recurrent Genres From " + min + " to " + max);
					pieElement = CinemaSpaceArchive.generateMostRecurrentGenresByRatingIntervals(user, min, max);
					
					int totalRatings = 0;
					for(int numberOfRatings : pieElement.values())
						totalRatings += numberOfRatings;

					if(pieChart.getData() != null) {
						pieChart.getData().clear();
					}
					for(Map.Entry<String,Integer> element : pieElement.entrySet()) {
						String label = element.getKey() + " " + (element.getValue()*100)/totalRatings + "%";
						pieChart.getData().add(new PieChart.Data(label, element.getValue()));
					}
					
					maxField.setText("");
					minField.setText("");
				}
			}
			else if (!isValidValueMinMax(minField.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("Your minimum value must be between 0 and 5 and must be lower than the maximum value.");
				alert.showAndWait();
				minField.setText("");
			}
			else if (!isValidValueMinMax(maxField.getText())) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("Your maximum value must be between 0 and 5 and must be higher than the minimum value.");
				alert.showAndWait();
				maxField.setText("");
			}
		}
	}
	
	@FXML protected void handlePreviousButtonGenresAction (ActionEvent event) throws FileNotFoundException {
		nextButtonGenres.setDisable(false);
		if(actualPageGenres >= 2) {
			actualPageGenres --;
			if(actualPageGenres == 1){
				previousButtonGenres.setDisable(true);
			}
		}
		else {
			previousButtonGenres.setDisable(true);
		}
		if(listOfFilmsGenres != null) {
			displayListOfFilmsGenres();
		}
	}
	
	@FXML protected void handleNextButtonGenresAction (ActionEvent event) throws FileNotFoundException {
		previousButtonGenres.setDisable(false);
		if(actualPageGenres <= maxNbPagesGenres - 1) {
			actualPageGenres ++;
			if(actualPageGenres == maxNbPagesGenres) {
				nextButtonGenres.setDisable(true);
			}
		}
		else {
			nextButtonGenres.setDisable(true);
		}
		if(listOfFilmsGenres != null) {
			displayListOfFilmsGenres();
		}
	}
	
	@FXML protected void handleFilm1TitleGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm1PosterGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm2TitleGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)+1));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm2PosterGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)+1));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm3TitleGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)+2));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm3PosterGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)+2));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm4TitleGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)+3));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm4PosterGenresAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsGenres != null) {
				controller.initFilm(listOfFilmsGenres.get(4*(actualPageGenres-1)+3));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void clearListOfFilmsGenres() {
		if(listOfFilmsGenres != null) {
			listOfFilmsGenres.clear();
		}
		film1Genres.setVisible(false);
		film2Genres.setVisible(false);
		film3Genres.setVisible(false);
		film4Genres.setVisible(false);
	}
	
	private void displayListOfFilmsGenres() throws FileNotFoundException {
		URL url;
		
		//FILM 1
		if(8*(actualPageGenres-1) < maxNbFilmsGenres) {
			film1Genres.setVisible(true);
			titleFilm1Genres.setText(listOfFilmsGenres.get(8*(actualPageGenres-1)).getTitle());
			titleFilm1Genres.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsGenres.get(8*(actualPageGenres-1)).getPosterPath());
				try {
					posterFilm1Genres.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm1Genres.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm1Genres.setImage(new Image(is));
			}
		}
		else {
			film1Genres.setVisible(false);
		}
		
		//FILM 2
		if(8*(actualPageGenres-1)+1 < maxNbFilmsGenres) {
			film2Genres.setVisible(true);
			titleFilm2Genres.setText(listOfFilmsGenres.get(8*(actualPageGenres-1)+1).getTitle());
			titleFilm2Genres.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsGenres.get(8*(actualPageGenres-1)+1).getPosterPath());
				try {
					posterFilm2Genres.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm2Genres.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm2Genres.setImage(new Image(is));
			}
		}
		else {
			film2Genres.setVisible(false);
		}
		
		//FILM 3
		if(8*(actualPageGenres-1)+2 < maxNbFilmsGenres) {
			film3Genres.setVisible(true);
			titleFilm3.setText(listOfFilmsGenres.get(8*(actualPageGenres-1)+2).getTitle());
			titleFilm3.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsGenres.get(8*(actualPageGenres-1)+2).getPosterPath());
				try {
					posterFilm3Genres.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm3Genres.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm3Genres.setImage(new Image(is));
			}
		}
		else {
			film3Genres.setVisible(false);
		}
		
		//FILM 4
		if(8*(actualPageGenres-1)+3 < maxNbFilmsGenres) {
			film4Genres.setVisible(true);
			titleFilm4.setText(listOfFilmsGenres.get(8*(actualPageGenres-1)+3).getTitle());
			titleFilm4.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsGenres.get(8*(actualPageGenres-1)+3).getPosterPath());
				try {
					posterFilm4Genres.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm4Genres.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm4Genres.setImage(new Image(is));
			}
		}
		else {
			film4Genres.setVisible(false);
		}
	}
	
	@FXML protected void handlePreviousButtonUsersAction (ActionEvent event) throws FileNotFoundException {
		nextButtonUsers.setDisable(false);
		if(actualPageUsers >= 2) {
			actualPageUsers --;
			if(actualPageUsers == 1){
				previousButtonUsers.setDisable(true);
			}
		}
		else {
			previousButtonUsers.setDisable(true);
		}
		if(listOfFilmsUsers != null) {
			displayListOfFilmsUsers();
		}
	}
	
	@FXML protected void handleNextButtonUsersAction (ActionEvent event) throws FileNotFoundException {
		previousButtonUsers.setDisable(false);
		if(actualPageUsers <= maxNbPagesUsers - 1) {
			actualPageUsers ++;
			if(actualPageUsers == maxNbPagesUsers) {
				nextButtonUsers.setDisable(true);
			}
		}
		else {
			nextButtonUsers.setDisable(true);
		}
		if(listOfFilmsUsers != null) {
			displayListOfFilmsUsers();
		}
	}
	
	@FXML protected void handleFilm1TitleUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm1PosterUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm2TitleUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)+1));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm2PosterUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)+1));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm3TitleUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)+2));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm3PosterUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)+2));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm4TitleUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)+3));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML protected void handleFilm4PosterUsersAction(MouseEvent event) {
		try {
			String address = new File("target/classes/cinemaspace/view/film.fxml").getAbsolutePath();
			FXMLLoader load = new FXMLLoader(new File(address).toURI().toURL());
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FilmPageController controller = load.<FilmPageController>getController();
			if(listOfFilmsUsers != null) {
				controller.initFilm(listOfFilmsUsers.get(4*(actualPageUsers-1)+3));
				if(user != null) {
					controller.initUser(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void clearListOfFilmsUsers() {
		if(listOfFilmsUsers != null) {
			listOfFilmsUsers.clear();
		}
		film1Users.setVisible(false);
		film2Users.setVisible(false);
		film3Users.setVisible(false);
		film4Users.setVisible(false);
	}
	
	private void displayListOfFilmsUsers() throws FileNotFoundException {
		URL url;
		
		//FILM 1
		if(8*(actualPageUsers-1) < maxNbFilmsUsers) {
			film1Users.setVisible(true);
			titleFilm1Users.setText(listOfFilmsUsers.get(8*(actualPageUsers-1)).getTitle());
			titleFilm1Users.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsUsers.get(8*(actualPageUsers-1)).getPosterPath());
				try {
					posterFilm1Users.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm1Users.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm1Users.setImage(new Image(is));
			}
		}
		else {
			film1Users.setVisible(false);
		}
		
		//FILM 2
		if(8*(actualPageUsers-1)+1 < maxNbFilmsUsers) {
			film2Users.setVisible(true);
			titleFilm2Users.setText(listOfFilmsUsers.get(8*(actualPageUsers-1)+1).getTitle());
			titleFilm2Users.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsUsers.get(8*(actualPageUsers-1)+1).getPosterPath());
				try {
					posterFilm2Users.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm2Users.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm2Users.setImage(new Image(is));
			}
		}
		else {
			film2Users.setVisible(false);
		}
		
		//FILM 3
		if(8*(actualPageUsers-1)+2 < maxNbFilmsUsers) {
			film3Users.setVisible(true);
			titleFilm3.setText(listOfFilmsUsers.get(8*(actualPageUsers-1)+2).getTitle());
			titleFilm3.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsUsers.get(8*(actualPageUsers-1)+2).getPosterPath());
				try {
					posterFilm3Users.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm3Users.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm3Users.setImage(new Image(is));
			}
		}
		else {
			film3Users.setVisible(false);
		}
		
		//FILM 4
		if(8*(actualPageUsers-1)+3 < maxNbFilmsUsers) {
			film4Users.setVisible(true);
			titleFilm4.setText(listOfFilmsUsers.get(8*(actualPageUsers-1)+3).getTitle());
			titleFilm4.setWrappingWidth(170.0);
			try {
				url = new URL("https://image.tmdb.org/t/p/w154/" + listOfFilmsUsers.get(8*(actualPageUsers-1)+3).getPosterPath());
				try {
					posterFilm4Users.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
				} catch (IOException e) {
					e.printStackTrace();
					InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
					posterFilm4Users.setImage(new Image(is));
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				InputStream is = new FileInputStream("target/classes/cinemaspace/view/nophoto.jpg");
				posterFilm4Users.setImage(new Image(is));
			}
		}
		else {
			film4Users.setVisible(false);
		}
	}
	
	public static boolean isValidValueMinMax(String value)
    {
        String emailRegex = "^[0-9.]+$";

        Pattern pat = Pattern.compile(emailRegex);
        if (value == null)
            return false;
        return pat.matcher(value).matches();
    }
	
	public void initUser(User user) {
		this.user = user;
		username.setText(user.getUsername());;
		email.setText(user.getEmail());
		gender.setText(user.getGender());;
		dateOfBirth.setText(user.getDateOfBirth());;
		
	}
	
	public void initListOfFilmsGenres(User user) {
		this.listOfFilmsTitlesGenres = CinemaSpaceArchive.requestFilmRecommendationsBasedOnGenre(user);
		if(this.listOfFilmsGenres != null) {
			this.listOfFilmsGenres = new ArrayList<Film>();
			for(String filmId : this.listOfFilmsTitlesGenres) {
				this.listOfFilmsGenres.add(CinemaSpaceArchive.getFilm(new ObjectId(filmId)));
			}
			if(this.listOfFilmsGenres != null) {
				try {
					displayListOfFilmsGenres();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void initListOfFilmsUsers(User user) {
		this.listOfFilmsTitlesUsers = CinemaSpaceArchive.requestFilmRecommendationsBasedOnOtherUsersWithCommonInterests(user);
		if(this.listOfFilmsTitlesUsers != null) {
			this.listOfFilmsUsers = new ArrayList<Film>();
			for(String filmId : this.listOfFilmsTitlesUsers) {
				this.listOfFilmsUsers.add(CinemaSpaceArchive.getFilm(new ObjectId(filmId)));
				
				System.out.println(CinemaSpaceArchive.getFilm(new ObjectId(filmId)).getTitle());
			}
			if(this.listOfFilmsUsers != null) {
				try {
					displayListOfFilmsUsers();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
