package cinemaspace.model;


public class CrewMember {
	private String department;
	private String job;
	private String name;
	private String profilePath;	
	
	public CrewMember(String department, String job, String name, String profilePath) {
		this.department = department;
		this.job = job;
		this.name = name;
		this.profilePath = profilePath;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public String getJob() {
		return job;
	}
	
	public String getName() {
		return name;
	}
	
	public String getProfilePath() {
		return profilePath;
	}
	
}
