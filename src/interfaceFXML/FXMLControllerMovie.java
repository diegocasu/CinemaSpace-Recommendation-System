package interfaceFXML;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.CastMember;
import main.java.CinemaSpaceArchive;
import main.java.CrewMember;
import main.java.Film;
import main.java.Rating;
import main.java.User;

public class FXMLControllerMovie {
	//Switch page controller
	private Parent root;
	private Stage stage;
		
	//Parameters
	private Film film;
	private User user;
	
	//FXML elements
	@FXML private ImageView poster;
	@FXML private Text title;
	@FXML private Text runtime;
	@FXML private Text genreList;
	@FXML private Text overview;
	@FXML private Text overallMeanRating;
	@FXML private Text recentMeanRating;
	@FXML private Text director;
	@FXML private Text castList;
	@FXML private Text crewList;
	@FXML private Text country;
	@FXML private Text language;
	@FXML private Text budget;
	@FXML private Text productionCompany;
	@FXML private ComboBox rate;
	@FXML private BarChart<String, Number> barRatings;
	@FXML private HBox statisticPart;
	@FXML private Text allAll;
	@FXML private Text all18;
	@FXML private Text all1845;
	@FXML private Text all45;
	@FXML private Text maleAll;
	@FXML private Text male18;
	@FXML private Text male1845;
	@FXML private Text male45;
	@FXML private Text femaleAll;
	@FXML private Text female18;
	@FXML private Text female1845;
	@FXML private Text female45;
	@FXML private Button profile;
	@FXML private Button deleteFilmButton;
	@FXML private ScrollBar scrollBarOverview;
	
	private double overallMeanRatingDouble;
	private double recentMeanRatingDouble;
	public FXMLControllerMovie() {
		super();
	}
	
	@FXML protected void initialize() {		
		
	}
	
	@FXML protected void handleHomeButtonAction (ActionEvent event) {
		try {
			FXMLLoader load = new FXMLLoader(getClass().getResource("home.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerHome controller = load.<FXMLControllerHome>getController();
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
			FXMLLoader load = new FXMLLoader(getClass().getResource("home.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerHome controller = load.<FXMLControllerHome>getController();
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
			FXMLLoader load = new FXMLLoader(getClass().getResource("home.fxml"));
			root = load.load();
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(new Scene(root));
			FXMLControllerHome controller = load.<FXMLControllerHome>getController();
			controller.mostPopular();
			if(user != null) {
				controller.initUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	
	@FXML protected void handleRateButtonAction (ActionEvent event) throws ParseException {
		String string_rate = (String) rate.getValue();
		String[] s_rate = string_rate.split("/");
		double rate_value = Double.parseDouble(s_rate[0]);
		
		Date date = new Date();
		long date_m = date.getTime();
		
		SimpleDateFormat old_simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat new_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date_birthday = old_simpleDateFormat.parse("25/6/1996");
		String string_birthday = new_simpleDateFormat.format(date_birthday);
		LocalDate birthday = new_simpleDateFormat.parse(string_birthday).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int age = Period.between(birthday, currentDate).getYears();
		
		Rating new_rate = new Rating(user.getId(), film.getId(), rate_value, date_m, age, user.getGender());
		Rating existing_rate = CinemaSpaceArchive.getRating(film, user);
		if (existing_rate != null && new_rate != null) {
			CinemaSpaceArchive.updateRating(new_rate);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialogue");
			alert.setHeaderText(null);
			alert.setContentText("Your rating has been updated.");
			alert.showAndWait();
		}
		else if(new_rate != null){
			CinemaSpaceArchive.addRating(new_rate);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialogue");
			alert.setHeaderText(null);
			alert.setContentText("Your rating has been added.");
			alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialogue");
			alert.setHeaderText(null);
			alert.setContentText("A problem has ocured during the rating. Please try again !");
			alert.showAndWait();
		}
	}
	
	@FXML protected void handleConfirmDeleteFilmsButtonAction (ActionEvent event) {
		if(film != null) {
			if(CinemaSpaceArchive.deleteFilm(film)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("The film has been correctly deleted.");
				alert.showAndWait();
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("A problem has ocured during the deleting. Please try again !");
				alert.showAndWait();
			}
		}
	}
	
	public void initUser(User user) {
		this.user = user;
		
		if(user.getAdministrator()) {
			profile.setText("Add film(s)");	
			deleteFilmButton.setVisible(true);
			deleteFilmButton.setDisable(false);
		}
		
		Rating existing_rate = CinemaSpaceArchive.getRating(film, user);
		if (existing_rate != null) {
			String text_rate = String.valueOf(existing_rate.getRating()) + "/5";
			rate.setPromptText(text_rate);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void initFilm(Film film) {
		this.film = film;	
		
		URL url;
		try {
			url = new URL("https://image.tmdb.org/t/p/w154/" + film.getPosterPath());
			try {
				poster.setImage(SwingFXUtils.toFXImage(ImageIO.read(url), null));
			} catch (IOException e) {
				e.printStackTrace();
				poster.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			poster.setImage(new Image(getClass().getResource("nophoto.jpg").toString()));
		}
		
		title.setText(film.getTitle());
		runtime.setText(String.valueOf(film.getRuntime()));
		
		String genres = "";
		List<String> list_genre = film.getGenres();
		for (int i = 0; i < list_genre.size(); i++) {
			if (i == 0) {
				genres += list_genre.get(i);
			}
			else {
				genres += " | " + list_genre.get(i);
			}
		}
		genreList.setText(genres);
		
		overview.setWrappingWidth(350.0);
		overview.setText(film.getOverview());
		director.setText(film.getCrew().get(0).getName());
		
		String cast = "";
		List<CastMember> list_cast = film.getCast();
		for (int i = 0; i < list_cast.size(); i++) {
			if (i == 0) {
				cast += list_cast.get(i).getName();
			}
			else if (i == 6) {
				cast += " ...";
			}
			else if (i < 6 && i != 0) {
				cast += " | " + list_cast.get(i).getName();
			}
		}		
		castList.setText(cast);
		
		String crew = "";
		List<CrewMember> list_crew = film.getCrew();
		for (int i = 1; i < list_crew.size(); i++) {
			if (i == 1) {
				crew += list_crew.get(i).getName();
			}
			else if (i == 7) {
				crew += " ...";
			}
			else if (i < 7 && i != 0) {
				crew += " | " + list_crew.get(i).getName();
			}
		}		
		crewList.setText(crew);
		
		String countries = "";
		List<String> list_country = film.getProductionCountries();
		for (int i = 0; i < list_country.size(); i++) {
			if (i == 0) {
				countries += list_country.get(i);
			}
			else {
				countries += " | " + list_country.get(i);
			}
		}		
		country.setText(countries);
		
		String spokenLanguage = "";
		List<String> list_language = film.getSpokenLanguages();
		for (int i = 0; i < list_language.size(); i++) {
			if (i == 0) {
				spokenLanguage += list_language.get(i);
			}
			else {
				spokenLanguage += " | " + list_language.get(i);
			}
		}		
		language.setText(spokenLanguage);
		
		budget.setText(String.valueOf(film.getBudget()));
		
		String companies = "";
		List<String> list_company = film.getProductionCompanies();
		for (int i = 0; i < list_company.size(); i++) {
			if (i == 0) {
				companies += list_company.get(i);
			}
			else if (i == 4) {
				companies += " ...";
			}
			else if (i < 4 && i != 0){
				companies += " | " + list_company.get(i);
			}
		}		
		productionCompany.setText(companies);
		
		//Overall Mean rating
		overallMeanRatingDouble = film.getAverageRating();
		overallMeanRating.setText(String.format("%.2f", overallMeanRatingDouble) + "/5");
		
		//Recent Mean rating
		
		recentMeanRatingDouble = CinemaSpaceArchive.generateRecentMeanRating(film);
		if(recentMeanRatingDouble == -1) {
			recentMeanRating.setText( "None");
		}
		else {
			recentMeanRating.setText(String.format("%.2f", recentMeanRatingDouble) + "/5");
		}
		
		Map<String, Integer> barData = CinemaSpaceArchive.generateDistributionOfRatings(film);
		NumberAxis yAxis = new NumberAxis();
		CategoryAxis xAxis = new CategoryAxis();
		barRatings = new BarChart<String,Number>(xAxis,yAxis);
		xAxis.setLabel("Ratings");

		XYChart.Series<String, Number> serie = new XYChart.Series<String, Number>();
		serie.getData().add(new XYChart.Data<String,Number>("0.0", (Number)barData.get("0.0")));
		serie.getData().add(new XYChart.Data<String,Number>("0.5", (Number)barData.get("0.5")));
		serie.getData().add(new XYChart.Data<String,Number>("1.0", (Number)barData.get("1.0")));
		serie.getData().add(new XYChart.Data<String,Number>("1.5", (Number)barData.get("1.5")));
		serie.getData().add(new XYChart.Data<String,Number>("2.0", (Number)barData.get("2.0")));
		serie.getData().add(new XYChart.Data<String,Number>("2.5", (Number)barData.get("2.5")));
		serie.getData().add(new XYChart.Data<String,Number>("3.0", (Number)barData.get("3.0")));
		serie.getData().add(new XYChart.Data<String,Number>("3.5", (Number)barData.get("3.5")));
		serie.getData().add(new XYChart.Data<String,Number>("4.0", (Number)barData.get("4.0")));
		serie.getData().add(new XYChart.Data<String,Number>("4.5", (Number)barData.get("4.5")));
		serie.getData().add(new XYChart.Data<String,Number>("5.0", (Number)barData.get("5.0")));
		
		barRatings.getData().addAll(serie);
		statisticPart.getChildren().add(barRatings);
		Map<String, Double> barDemographicData = CinemaSpaceArchive.generateDistributionOfRatingsByDemographic(film);
		allAll.setText(String.format("%.2f", film.getAverageRating()) + "/5");
		all18.setText(String.format("%.2f", barDemographicData.get("All_18")) + "/5");
		all1845.setText(String.format("%.2f", barDemographicData.get("All_18_45")) + "/5");
		all45.setText(String.format("%.2f", barDemographicData.get("All_45")) + "/5");
		
		maleAll.setText(String.format("%.2f", barDemographicData.get("Male_All")) + "/5");
		male18.setText(String.format("%.2f", barDemographicData.get("Male_18")) + "/5");
		male1845.setText(String.format("%.2f", barDemographicData.get("Male_18_45")) + "/5");
		male45.setText(String.format("%.2f", barDemographicData.get("Male_45")) + "/5");
		
		femaleAll.setText(String.format("%.2f", barDemographicData.get("Female_All")) + "/5");
		female18.setText(String.format("%.2f", barDemographicData.get("Female_18")) + "/5");
		female1845.setText(String.format("%.2f", barDemographicData.get("Female_18_45")) + "/5");
		female45.setText(String.format("%.2f", barDemographicData.get("Female_45")) + "/5");
	}

}
