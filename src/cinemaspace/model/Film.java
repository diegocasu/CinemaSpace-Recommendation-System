package cinemaspace.model;


import java.util.*;
import org.bson.types.*;

public class Film {
	private ObjectId id;
	private double budget;
	private List<String> genres;
	private String homepage;
	private String originalLanguage;
	private String originalTitle;
	private String overview;
	private String posterPath;
	private List<String> productionCompanies;
	private List<String> productionCountries;
	private String releaseDate;
	private double revenue;
	private double runtime;
	private List<String> spokenLanguages;
	private String status;
	private String tagline;
	private String title;
	private int numberOfVisits;
	private List<String> keywords;
	private List<CastMember> cast;
	private List<CrewMember> crew;
	private double averageRating;
	private int numberOfRatings;
	
	
	public Film(ObjectId id, double budget, List<String> genres, String homepage, String originalLanguage,
			String originalTitle, String overview, String posterPath, List<String> productionCompanies,
			List<String> productionCountries, String releaseDate, double revenue, double runtime,
			List<String> spokenLanguages, String status, String tagline, String title, int numberOfVisits,
			List<String> keywords, List<CastMember> cast, List<CrewMember> crew, 
			double averageRating, int numberOfRatings) {
		
		this.id = id;
		this.budget = budget;
		this.genres = genres;
		this.homepage = homepage;
		this.originalLanguage = originalLanguage;
		this.originalTitle = originalTitle;
		this.overview = overview;
		this.posterPath = posterPath;
		this.productionCompanies = productionCompanies;
		this.productionCountries = productionCountries;
		this.releaseDate = releaseDate;
		this.revenue = revenue;
		this.runtime = runtime;
		this.spokenLanguages = spokenLanguages;
		this.status = status;
		this.tagline = tagline;
		this.title = title;
		this.numberOfVisits = numberOfVisits;
		this.keywords = keywords;
		this.cast = cast;
		this.crew = crew;
		this.averageRating = averageRating;
		this.numberOfRatings = numberOfRatings;
	}
	
	public ObjectId getId() {
		return id;
	}
	
	public double getBudget() {
		return budget;
	}
	
	public List<String> getGenres() {
		return genres;
	}
	
	public String getHomepage() {
		return homepage;
	}
	
	public String getOriginalLanguage() {
		return originalLanguage;
	}
	
	public String getOriginalTitle() {
		return originalTitle;
	}
	
	public String getOverview() {
		return overview;
	}
	
	public String getPosterPath() {
		return posterPath;
	}
	
	public List<String> getProductionCompanies() {
		return productionCompanies;
	}
	
	public List<String> getProductionCountries() {
		return productionCountries;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public double getRevenue() {
		return revenue;
	}
	
	public double getRuntime() {
		return runtime;
	}
	
	public List<String> getSpokenLanguages() {
		return spokenLanguages;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getTagline() {
		return tagline;
	}
	
	public String getTitle() {
		return title;
	}	
	
	public int getNumberOfVisits() {
		return numberOfVisits;
	}	
	
	public List<String> getKeywords() {
		return keywords;
	}
	
	public List<CastMember> getCast() {
		return cast;
	}
	
	public List<CrewMember> getCrew() {
		return crew;
	}	
	
	public double getAverageRating() {
		return averageRating;
	}
	
	public int getNumberOfRatings() {
		return numberOfRatings;
	}
		
}
