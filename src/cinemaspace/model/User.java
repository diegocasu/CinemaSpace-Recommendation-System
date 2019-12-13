package cinemaspace.model;


import org.bson.types.*;

public class User {
	private ObjectId id;
	private String username;
	private String password;
	private String email;
	private String dateOfBirth;
	private String gender;
	private boolean administrator;
	
	public User(ObjectId id, String username, String password, String email, 
				String dateOfBirth, String gender, boolean administrator) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.administrator = administrator;
	}	
	
	public ObjectId getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	public String getGender() {
		return gender;
	}
	
	public boolean getAdministrator() {
		return administrator;
	}

}
