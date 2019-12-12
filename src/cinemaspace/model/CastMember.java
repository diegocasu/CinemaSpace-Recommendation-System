package cinemaspace.model;


public class CastMember {
	private String character;
	private String name;
	private int order;
	private String profilePath;
	

	public CastMember(String character, String name, int order, String profilePath) {
		this.character = character;
		this.name = name;
		this.order = order;
		this.profilePath = profilePath;
	}
	
	public String getCharacter() {
		return character;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOrder() {
		return order;
	}
	
	public String getProfilePath() {
		return profilePath;
	}
	
}
