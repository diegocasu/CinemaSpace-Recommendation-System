package cinemaspace.model;

public class SignUpException extends Exception {
	
	public SignUpException() {
		super("A user with the same email and password is registered.");
	}

}
