package cinemaspace.model;


import java.util.*;
import org.bson.types.*;

public class CinemaSpaceArchive {
	private static CinemaSpaceMainArchive mainArchive = null;
	private static CinemaSpaceRecommendationArchive recommendationArchive = null;
	
 	public static boolean openConnection(String mainArchiveConnectionString, 
 										String recommendationArchiveConnectionString,
 										String recommendationArchiveUser,
 										String recommendationArchivePassword)
 	{
 		mainArchive = new CinemaSpaceMainArchive();
 		recommendationArchive = new CinemaSpaceRecommendationArchive();
 		
 		mainArchive.openConnection(mainArchiveConnectionString);
 		
 		// The MongoDB driver notifies a connection problem only when executing the first query
 		// In this case, the check is possible only with Neo4j 
 		boolean connectionEstablished = recommendationArchive.openConnection(recommendationArchiveConnectionString,
 																			recommendationArchiveUser,
 																			recommendationArchivePassword);
 		
 		return connectionEstablished;
	}
	
	public static void closeConnection() {
		mainArchive.closeConnection();
		recommendationArchive.closeConnection();
	}
	
	public static boolean addUser(User user) throws SignUpException {
		boolean querySuccess = false;
		
		try {
			ObjectId newUserId = mainArchive.addUser(user);
			
			if(newUserId != null) {
				User newUser = new User(newUserId, user.getUsername(), user.getPassword(),
										user.getEmail(), user.getDateOfBirth(),
										user.getGender(), user.getAdministrator());
				
				boolean intermediateQuerySuccess = recommendationArchive.addUser(newUser);
				
				if(!intermediateQuerySuccess) {
					// Rollback
					mainArchive.deleteUser(newUser);
				}
				else {
					querySuccess = true;
				}
			}
			
		} catch(SignUpException exception) {
			throw new SignUpException();
		}
		
		return querySuccess;
	}
	
	public static User login(String email, String password) throws LoginException {
		User loggedUser = null;
		
		try {
			loggedUser = mainArchive.login(email, password);
			
		} catch(LoginException exception) {
			throw new LoginException();
		}
		
		return loggedUser;
	}

	public static boolean deleteUser(User user) {
		boolean querySuccess = false;
		
		boolean intermediateQuerySuccess = mainArchive.deleteUser(user);
		if(intermediateQuerySuccess) {
			recommendationArchive.deleteUser(user);
			querySuccess = true;
		}
		
		return querySuccess;
	}
	
	public static List<Film> searchFilmsByKeywords(List<String> keywords) {
		return mainArchive.searchFilmsByKeywords(keywords);
	}
	
	public static List<Film> searchFilmsByHighestRatings() {
		return mainArchive.searchFilmsByHighestRatings();
	}
	
	public static List<Film> searchFilmsByHighestNumberOfVisits() {
		return mainArchive.searchFilmsByHighestNumberOfVisits();
	}
	
	public static double generateRecentMeanRating(Film film) {
		return mainArchive.generateRecentMeanRating(film);
	}
	
	public static Map<String, Integer> generateDistributionOfRatings(Film film) {
		return mainArchive.generateDistributionOfRatings(film);
	}
	
	public static Map<String, Double> generateDistributionOfRatingsByDemographic(Film film) {
		return mainArchive.generateDistributionOfRatingsByDemographic(film);
	}

	public static Rating getRating(Film film, User user) {
		return mainArchive.getRating(film, user);
	}
	
	public static boolean addRating(Rating rating) {
		boolean querySuccess = false;
		
		// To avoid errors in the update of the mean rating, we execute first
		// the insertion inside the recommendation archive
		boolean intermediateQuerySuccess = recommendationArchive.addRating(rating);
			
		if(intermediateQuerySuccess) {
			intermediateQuerySuccess = mainArchive.addRating(rating);
				
			if(!intermediateQuerySuccess) {
				// Rollback
				recommendationArchive.deleteRating(rating);
			}
			else {
				querySuccess = true;
			}
		}
		
		return querySuccess;
	}

	public static boolean updateRating(Rating rating) {
		boolean querySuccess = false;
		
		// Old rating for an eventual rollback
		Film film = new Film(rating.getFilmId(), 0.0, null, "", "", "", "", "", null, null, "", 0.0, 0.0, null, "", "", "", 0, null, null, null, 0.0, 0);
		User user = new User(rating.getUserId(), "", "", "", "", "", false);	
		Rating oldRating = mainArchive.getRating(film, user);
		
		// To avoid errors in the update of the mean rating, we execute first
		// the update inside the recommendation archive
		boolean intermediateQuerySuccess = recommendationArchive.updateRating(rating);
			
		if(intermediateQuerySuccess) {
			intermediateQuerySuccess = mainArchive.updateRating(rating);
				
			if(!intermediateQuerySuccess) {
				// Rollback
				recommendationArchive.updateRating(oldRating);
			}
			else {
				querySuccess = true;
			}
		}
		
		return querySuccess;	
	}

	public static boolean addFilms(List<Film> films) {
		boolean querySuccess = false;
		
		List<ObjectId> newFilmIds = mainArchive.addFilms(films);
		
		if(newFilmIds != null) {
			// Add the MongoDB film id to the films in the list
			for( int i = 0 ; i < newFilmIds.size() ; i++) {
				films.get(i).setId(newFilmIds.get(i));
			}
	
			boolean intermediateQuerySuccess = recommendationArchive.addFilms(films);
				
			if(!intermediateQuerySuccess) {
				// Rollback: the film nodes are created with multiple transactions, 
				// so it is needed a rollback also for the recommendation archive
				
				for(Film film : films) {
					mainArchive.deleteFilm(film);
					recommendationArchive.deleteFilm(film);
				}
				
				recommendationArchive.deleteGenresWithoutRelationships();
			}
			else {
				querySuccess = true;
			}
		}

		return querySuccess;
	}
	
	public static boolean deleteFilm(Film film) {
		boolean querySuccess = false;
		
		boolean intermediateQuerySuccess = mainArchive.deleteFilm(film);
		if(intermediateQuerySuccess) {
			recommendationArchive.deleteFilm(film);
			recommendationArchive.deleteGenresWithoutRelationships();
			querySuccess = true;
		}
		
		return querySuccess;
	}

	public static Map<String, Integer> generateMostRecurrentGenresByRatingIntervals(User user, double min, double max) {
		return mainArchive.generateMostRecurrentGenresByRatingIntervals(user, min, max);
	}
	
	public static void increaseNumberOfVisits(Film film) {
		mainArchive.increaseNumberOfVisits(film);
	}

	public static Film getFilm(ObjectId filmId) {
		return mainArchive.getFilm(filmId);
	}
	
	public static List<String> requestFilmRecommendationsBasedOnGenre(User user) {
		return recommendationArchive.requestFilmRecommendationsBasedOnGenre(user);
	}
	
	public static List<String> requestFilmRecommendationsBasedOnOtherUsersWithCommonInterests(User user) {
		return recommendationArchive.requestFilmRecommendationsBasedOnOtherUsersWithCommonInterests(user);
	}
	
}
