package interfaceFXML;

import java.io.IOException;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.CinemaSpaceArchive;
import main.java.User;

public class FXMLControllerConnection {
	//Switch page controller
	private Parent root;
	private Stage stage;
		
	//Parameter
	private User user;
	
	//FXML elements
	//Sign Up part
	@FXML private Text titleSignUp;
	@FXML private TextField usernameSignUp;
	@FXML private TextField emailSignUp;
	@FXML private PasswordField passwordSignUp;
	@FXML private Text genderSignUp;
	@FXML private RadioButton maleSignUp;
	@FXML private RadioButton femaleSignUp;
	@FXML private TextField dateOfBirthSignUp;
	@FXML private Button confirmSignUp;
	//Log In part
	@FXML private Text titleLogin;
	@FXML private TextField emailLogin;
	@FXML private PasswordField passwordLogin;
	@FXML private Button confirmLogin;
	@FXML private ToggleGroup gender;
	
	private String email;
	private String dateOfBirth;
	
	public FXMLControllerConnection() {
		super();
	}
	
	@FXML protected void initialize() {
	}
	
	@FXML protected void handleMaleRadioButtonAction (ActionEvent event) {
		maleSignUp.setSelected(true);
		femaleSignUp.setSelected(false);
	}
	
	@FXML protected void handleFemaleRadioButtonAction (ActionEvent event) {
		maleSignUp.setSelected(false);
		femaleSignUp.setSelected(true);
	}
	
	@FXML protected void handleConfirmSignUpButtonAction (ActionEvent event) {
		if(usernameSignUp.getText().isEmpty() || emailSignUp.getText().isEmpty() || passwordSignUp.getText().isEmpty() || dateOfBirthSignUp.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Diaglog");
			alert.setHeaderText(null);
			alert.setContentText("Some fields are still empty, please feed them.");
			alert.showAndWait();
		}
		else {
			dateOfBirth = dateOfBirthSignUp.getText();
			email = emailSignUp.getText();
			if(isValidDateOfBirth(dateOfBirth) && isValidEmail(email)) {
				if(maleSignUp.isSelected()) {
					user = new User(usernameSignUp.getText(), passwordSignUp.getText(), emailSignUp.getText(), dateOfBirthSignUp.getText(), "Male", false);
				}
				else if(femaleSignUp.isSelected()) {
					user = new User(usernameSignUp.getText(), passwordSignUp.getText(), emailSignUp.getText(), dateOfBirthSignUp.getText(), "Female", false);
				}
				
				if(user != null) {
					if(CinemaSpaceArchive.addUser(user)) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Information Dialogue");
						alert.setHeaderText(null);
						alert.setContentText("Your account has been successfully created.");
						alert.showAndWait();
						usernameSignUp.setText("");
						emailSignUp.setText("");
						passwordSignUp.setText("");
						dateOfBirthSignUp.setText("");
						user = null;
					}
					else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Dialogue");
						alert.setHeaderText(null);
						alert.setContentText("A problem occurred during the creation of your account. Please try again !");
						alert.showAndWait();
					}
				}
			}
			else if (!isValidDateOfBirth(dateOfBirth)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("Your date of birth is uncorrect.");
				alert.showAndWait();
			}
			else if (!isValidEmail(email)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("Your email is uncorrect.");
				alert.showAndWait();
			}
			
		}
	}
	
	@FXML protected void handleConfirmLoginButtonAction (ActionEvent event) {
		if(emailLogin.getText().isEmpty() || passwordLogin.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Diaglog");
			alert.setHeaderText(null);
			alert.setContentText("Some fields are still empty, please fill in them.");
			alert.showAndWait();
		}
		else {
			email = emailLogin.getText();
			if(isValidEmail(email)) {
				try {
					FXMLLoader load = new FXMLLoader(getClass().getResource("home.fxml"));
					root = load.load();
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.setScene(new Scene(root));
					FXMLControllerHome controller = load.<FXMLControllerHome>getController();
					user = CinemaSpaceArchive.login(email, passwordLogin.getText());
					if(user != null) {
						controller.initUser(user);
					}
					else
					{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Dialogue");
						alert.setHeaderText(null);
						alert.setContentText("A problem occurred during your connexion. Please try again !");
						alert.showAndWait();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialogue");
				alert.setHeaderText(null);
				alert.setContentText("Your email is uncorrect.");
				alert.showAndWait();
			}
		}
	}
	
	public static boolean isValidEmail(String email)
    {
        String emailRegex = "^(.+)@(.+)$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
	
	public static boolean isValidDateOfBirth(String dateOfBirth)
    {
        String emailRegex = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (dateOfBirth == null)
            return false;
        return pat.matcher(dateOfBirth).matches();
    }
}
