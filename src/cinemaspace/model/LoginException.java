package cinemaspace.model;

public class LoginException extends Exception {
	
	public LoginException() {
		super("There aren't registered users with the given credentials.");
	}
	
}
