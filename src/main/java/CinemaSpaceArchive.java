package main.java;


import java.util.*;
import org.bson.*;
import org.bson.conversions.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.*;


public class CinemaSpaceArchive {
	private static MongoClient clientConnection = null;
	private static MongoDatabase cinemaSpaceDatabase = null;
	private static String databaseAddress = "mongodb://";
	
	
	private static Film extractFilmFromDocument(Document filmDocument) {
		@SuppressWarnings("unchecked")
		Film newFilm = new Film(filmDocument.getObjectId("_id"),
								DatabaseObjectConverter.convertToDouble(filmDocument.get("budget")),
								(List<String>)filmDocument.get("genres"), 
								DatabaseObjectConverter.convertToString(filmDocument.get("homepage")),
								DatabaseObjectConverter.convertToString(filmDocument.get("original_language")), 
								DatabaseObjectConverter.convertToString(filmDocument.get("original_title")),
								DatabaseObjectConverter.convertToString(filmDocument.get("overview")), 
								DatabaseObjectConverter.convertToString(filmDocument.get("poster_path")),
								(List<String>)filmDocument.get("production_companies"),
								(List<String>)filmDocument.get("production_countries"),
								DatabaseObjectConverter.convertToString(filmDocument.get("release_date")), 
								DatabaseObjectConverter.convertToDouble(filmDocument.get("revenue")),
								DatabaseObjectConverter.convertToDouble(filmDocument.get("runtime")),
								(List<String>)filmDocument.get("spoken_languages"),
								DatabaseObjectConverter.convertToString(filmDocument.get("status")), 
								DatabaseObjectConverter.convertToString(filmDocument.get("tagline")),
								DatabaseObjectConverter.convertToString(filmDocument.get("title")), 
								DatabaseObjectConverter.convertToInteger(filmDocument.get("number_of_visits")),
								(List<String>)filmDocument.get("keywords"),
								DatabaseObjectConverter.convertToCastMemberList((List<Document>)filmDocument.get("cast")),
								DatabaseObjectConverter.convertToCrewMemberList((List<Document>)filmDocument.get("crew")),
								DatabaseObjectConverter.convertToDouble(filmDocument.get("average_rating")),
								DatabaseObjectConverter.convertToInteger(filmDocument.get("number_of_ratings")));
								
		return newFilm;
	}
	
	private static List<Bson> recentMeanRatingQuery(Film film) {
		long twoYearsMilliseconds = 63072000000L;
		Document ifBranch = new Document("$gte", Arrays.asList(new Document("$toDate", "$timestamp"),
															   new Document("$subtract", Arrays.asList(new Date(), twoYearsMilliseconds))));
		
		Document conditionOperator = new Document("$cond", Arrays.asList(ifBranch, "$rating", null));
		Document averageOperator = new Document("$avg", conditionOperator);
		
		List<Bson> aggregationQuery = Arrays.asList(
				Aggregates.match(Filters.eq("_id.filmId", film.getId())),
				new Document("$group", new Document("_id", null).append("recentMeanRating", averageOperator)),
				new Document("$project", new Document("_id", 0).append("recentMeanRating", 1)));
									
		return aggregationQuery;
	}
	
	private static Map<String, Integer> generateDefaultRatingDistribution() {
		Map<String, Integer> ratingDistribution = new HashMap<>();
		List<String> possibleRatings = Arrays.asList("0.0", "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5", "5.0");
		
		for(String rating : possibleRatings)
			ratingDistribution.put(rating, 0);
		
		return ratingDistribution;
	}

	private static Map<String, Double> generateDefaultRatingDistributionByDemographic() {
		Map<String, Double> ratingDistribution = new HashMap<>();
		List<String> possibleRatings = Arrays.asList("All_18", "All_18_45", "All_45",
													 "Male_18", "Male_18_45", "Male_45", "Male_All", 
													 "Female_18", "Female_18_45", "Female_45", "Female_All");
		
		for(String rating : possibleRatings)
			ratingDistribution.put(rating, null);
				
		return ratingDistribution;
	}
	
	private static List<Bson> distributionOfRatingsByDemographicQuery(Film film) {
		// Documents useful to specify the conditions inside the group stages

		Document ifBranchLessThan18 = new Document("$lt", Arrays.asList("$age_of_user", 18));
		Document averageLessThan18 = new Document("$avg", new Document("$cond", Arrays.asList(ifBranchLessThan18, "$rating", null)));

		Document ifBranch18_45 = new Document("$and", Arrays.asList(new Document("$gte", Arrays.asList("$age_of_user", 18)),
																	new Document("$lt", Arrays.asList("$age_of_user", 45))));
		Document average18_45 = new Document("$avg", new Document("$cond", Arrays.asList(ifBranch18_45, "$rating", null)));
		
		Document ifBranchMoreThan45 = new Document("$gte", Arrays.asList("$age_of_user", 45));
		Document averageMoreThan45 = new Document("$avg", new Document("$cond", Arrays.asList(ifBranchMoreThan45, "$rating", null)));
		
		Document averageOfGender = new Document("$avg", "$rating");
		
		Document projectByGender = new Document("$project", new Document("_id", 0)
															.append("gender", "$_id")
															.append("averageLessThan18", 1)
															.append("average18_45", 1)
															.append("averageMoreThan45", 1)
															.append("averageOfGender", 1));
		
		Document projectTotal = new Document("$project", new Document("_id", 0)
														 .append("averageLessThan18", 1)
														 .append("average18_45", 1)
														 .append("averageMoreThan45", 1));
			
		// Creation of the independent branches that will run in the $facet stage
		
		List<Document> pipelineGroupByGender = Arrays.asList(new Document("$group", new Document("_id", "$gender")
																					.append("averageLessThan18", averageLessThan18)
																					.append("average18_45", average18_45)
																					.append("averageMoreThan45", averageMoreThan45)
																					.append("averageOfGender", averageOfGender)),
										       				 projectByGender);
		
		List<Document> pipelineGroupTotal = Arrays.asList(new Document("$group", new Document("_id", null)
																				.append("averageLessThan18", averageLessThan18)
																				.append("average18_45", average18_45)
																				.append("averageMoreThan45", averageMoreThan45)),
														  projectTotal);
		
		// $facet stage creation
		
		Facet groupByGender = new Facet("groupByGender", pipelineGroupByGender);
		Facet groupTotal = new Facet("groupTotal", pipelineGroupTotal);
		
		List<Bson> aggregationQuery = Arrays.asList(
				Aggregates.match(Filters.eq("_id.filmId", film.getId())),
				Aggregates.facet(groupByGender, groupTotal));

		return aggregationQuery;
	}
	
	private static List<Document> generateListOfFilmDocuments(List<Film> films) {
		List<Document> documentList = new ArrayList<>();
		
		for(Film film : films) {
			List<Document> castDocumentList = new ArrayList<>();
			List<Document> crewDocumentList = new ArrayList<>();
			
			// Cast conversion
			for(CastMember castMember : film.getCast()) {
				Document castDocument = new Document("character", castMember.getCharacter())
							   			.append("name", castMember.getName())
							   			.append("order", castMember.getOrder())
							   			.append("profile_path", castMember.getProfilePath());
				
				castDocumentList.add(castDocument);
			}
			
			// Crew conversion
			for(CrewMember crewMember : film.getCrew()) {
				Document crewDocument = new Document("department", crewMember.getDepartment())
							   			.append("job", crewMember.getJob())
							   			.append("name", crewMember.getName())
							   			.append("profile_path", crewMember.getProfilePath());
				
				crewDocumentList.add(crewDocument);
			}
			
			Document newFilmDocument = new Document("budget", film.getBudget())
										   .append("genres", film.getGenres())
										   .append("homepage", film.getHomepage())
										   .append("original_language", film.getOriginalLanguage())
										   .append("original_title", film.getOriginalTitle())
										   .append("overview", film.getOverview())
										   .append("poster_path", film.getOverview())
										   .append("production_companies", film.getProductionCompanies())
										   .append("production_countries", film.getProductionCountries())
										   .append("release_date", film.getReleaseDate())
										   .append("revenue", film.getRevenue())
										   .append("runtime", film.getRuntime())
										   .append("spoken_languages", film.getSpokenLanguages())
										   .append("status", film.getStatus())
										   .append("tagline", film.getTagline())
										   .append("title", film.getTitle())
										   .append("number_of_visits", 0)
										   .append("keywords", film.getKeywords())
										   .append("cast", castDocumentList)
										   .append("crew", crewDocumentList)
										   .append("average_rating", 0.0)
										   .append("number_of_ratings", 0);
			
			documentList.add(newFilmDocument);
		
		}
		
		return documentList;
	}
	
	private static List<Bson> mostRecurrentGenresByRatingIntervalsQuery(User user, double min, double max) {
				
		Bson matchFilter = Filters.and(Filters.eq("_id.userId", user.getId()),
				 		   			   Filters.and(Filters.gte("rating", min),
				 		   					   	   Filters.lte("rating", max)));
						 
		List<Bson> aggregationQuery = Arrays.asList(Aggregates.match(matchFilter),
													Aggregates.lookup("Film", "_id.filmId", "_id", "filmDetails"),
													Aggregates.unwind("$filmDetails"),
													new Document("$project", new Document("_id", 0).append("filmId", "$_id.filmId").append("genres", "$filmDetails.genres")),
													Aggregates.unwind("$genres"),
													Aggregates.group("$genres", Accumulators.sum("count", 1)),
													new Document("$project", new Document("_id", 0).append("genre", "$_id").append("count", 1)));
											
		return aggregationQuery;
	}
	
	
 	public static void openConnection(String addressDBMS, String portDBMS) {
 		databaseAddress += addressDBMS + ":" + portDBMS;
 		
		clientConnection = MongoClients.create(databaseAddress);
		cinemaSpaceDatabase = clientConnection.getDatabase("CinemaSpace");
	}
	
	public static void closeConnection() {
		clientConnection.close();
	}
	
	public static boolean addUser(User user) {
		MongoCollection<Document> userCollection = cinemaSpaceDatabase.getCollection("User");
		boolean querySuccess = false;
				
		try{
			// Check if a user with the same email and password is already in the database
			long userSignedUp = userCollection.countDocuments(Filters.and(Filters.eq("email", user.getEmail()),
																		  Filters.eq("password", user.getPassword())));
			
			if(userSignedUp > 0)
				return querySuccess;
				
			Document newUser = new Document("username", user.getUsername())
					   .append("password", user.getPassword())
					   .append("email", user.getEmail())
					   .append("date_of_birth", user.getDateOfBirth())
					   .append("gender", user.getGender())
					   .append("administrator", user.getAdministrator());
			
			userCollection.insertOne(newUser);
			querySuccess = true;
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public static User login(String email, String password) {
		MongoCollection<Document> userCollection = cinemaSpaceDatabase.getCollection("User");
		User loggedUser = null;
		
		try{
			Document userSignedUp = userCollection.find(Filters.and(Filters.eq("email", email),
																	Filters.eq("password", password))).first();
			
			if(userSignedUp == null)
				return loggedUser;
						
			loggedUser = new User(userSignedUp.getObjectId("_id"),
								  userSignedUp.getString("username"), 
								  userSignedUp.getString("password"), 
								  userSignedUp.getString("email"), 
								  userSignedUp.getString("date_of_birth"),
								  userSignedUp.getString("gender"), 
								  userSignedUp.getBoolean("administrator"));

		} catch(Exception exception) {
			exception.printStackTrace();
		}	
			
		return loggedUser;
	}

	public static boolean deleteUser(User user) {
		MongoCollection<Document> userCollection = cinemaSpaceDatabase.getCollection("User");
		boolean querySuccess = false;
		
		try {
			DeleteResult result = userCollection.deleteOne(Filters.eq("_id", user.getId()));
					
			if(result.getDeletedCount() == 1)
				querySuccess = true;
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}	
		
		return querySuccess;
	}
	
	public static List<Film> searchFilmsByKeywords(List<String> keywords) {
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		List<Film> filmSearch = new ArrayList<>();
		
		List<Bson> aggregationQuery = Arrays.asList(Aggregates.match(Filters.in("keywords", keywords)),
													Aggregates.sort(Filters.eq("number_of_visits", -1)),
													Aggregates.limit(50));
				
		try(MongoCursor<Document> cursor = filmCollection.aggregate(aggregationQuery).iterator()) {
			
			while(cursor.hasNext()) {
				Document filmDocument = cursor.next();
				filmSearch.add(CinemaSpaceArchive.extractFilmFromDocument(filmDocument));
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	
		return filmSearch;
	}
	
	public static List<Film> searchFilmsByHighestRatings() {
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		List<Film> filmSearch = new ArrayList<>();
		
		List<Bson> aggregationQuery = Arrays.asList(Aggregates.sort(Filters.eq("average_rating", -1)),
													Aggregates.match(Filters.gte("number_of_ratings", 1000)),
													Aggregates.limit(50));
		
		try(MongoCursor<Document> cursor = filmCollection.aggregate(aggregationQuery).iterator()) {
			
			while(cursor.hasNext()) {
				Document filmDocument = cursor.next();
				filmSearch.add(CinemaSpaceArchive.extractFilmFromDocument(filmDocument));
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return filmSearch;
	}
	
	public static List<Film> searchFilmsByHighestNumberOfVisits() {
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		List<Film> filmSearch = new ArrayList<>();
		
		List<Bson> aggregationQuery = Arrays.asList(Aggregates.sort(Filters.eq("number_of_visits", -1)),
													Aggregates.limit(50));
		
		try(MongoCursor<Document> cursor = filmCollection.aggregate(aggregationQuery).iterator()) {
			
			while(cursor.hasNext()) {
				Document filmDocument = cursor.next();
				filmSearch.add(CinemaSpaceArchive.extractFilmFromDocument(filmDocument));
			}
				
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return filmSearch;
	}
	
	public static double generateRecentMeanRating(Film film) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		double recentMeanRating = -1;
		
		List<Bson> aggregationQuery = CinemaSpaceArchive.recentMeanRatingQuery(film);
		
		try(MongoCursor<Document> cursor = ratingCollection.aggregate(aggregationQuery).iterator()) {
			
			while(cursor.hasNext()) {
				Document ratingDocument = cursor.next();
				
				// If there are no recent ratings, the average result can be null and it must be handled
				Double wrappedRecentMeanRating = ratingDocument.getDouble("recentMeanRating");
				
				if(wrappedRecentMeanRating != null)
					recentMeanRating = wrappedRecentMeanRating;
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return recentMeanRating;
	}
	
	public static Map<String, Integer> generateDistributionOfRatings(Film film) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		Map<String, Integer> ratingDistribution = generateDefaultRatingDistribution();
				
		List<Bson> aggregationQuery = Arrays.asList(Aggregates.match(Filters.eq("_id.filmId",  film.getId())),
													Aggregates.group("$rating", Accumulators.sum("count", 1)),
													new Document("$project", new Document("_id", 0).append("value", "$_id").append("count", "$count")));
		
		try(MongoCursor<Document> cursor = ratingCollection.aggregate(aggregationQuery).iterator()) {
			
			while(cursor.hasNext()) {
				Document ratingDocument = cursor.next();
				
				double rating = ratingDocument.getDouble("value");
				int count = ratingDocument.getInteger("count");
				ratingDistribution.replace(String.valueOf(rating), count);
			}	
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return ratingDistribution;
	}
	
	public static Map<String, Double> generateDistributionOfRatingsByDemographic(Film film) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		Map<String, Double> ratingDistribution = generateDefaultRatingDistributionByDemographic();
				
		List<Bson> aggregationQuery = CinemaSpaceArchive.distributionOfRatingsByDemographicQuery(film);
		
		try(MongoCursor<Document> cursor = ratingCollection.aggregate(aggregationQuery).iterator()) {
			
			if(cursor.hasNext()) {
				Document ratingDocument = cursor.next();
				
				// Statistics grouped by age only (both males and females)
	
				@SuppressWarnings("unchecked")
				Document averageTotal = ((List<Document>)ratingDocument.get("groupTotal")).get(0);
				ratingDistribution.replace("All_18", averageTotal.getDouble("averageLessThan18"));
				ratingDistribution.replace("All_18_45", averageTotal.getDouble("average18_45"));
				ratingDistribution.replace("All_45", averageTotal.getDouble("averageMoreThan45"));

				// Statistics grouped by age and gender
				
				@SuppressWarnings("unchecked")
				List<Document> averageByGender = (List<Document>)ratingDocument.get("groupByGender");
				
				for(Document averageOfGender : averageByGender) {
					if(averageOfGender.getString("gender").equals("Female")) {
						ratingDistribution.replace("Female_18", averageOfGender.getDouble("averageLessThan18"));
						ratingDistribution.replace("Female_18_45", averageOfGender.getDouble("average18_45"));
						ratingDistribution.replace("Female_45", averageOfGender.getDouble("averageMoreThan45"));
						ratingDistribution.replace("Female_All", averageOfGender.getDouble("averageOfGender"));
					}
					else if(averageOfGender.getString("gender").equals("Male")) {
						ratingDistribution.replace("Male_18", averageOfGender.getDouble("averageLessThan18"));
						ratingDistribution.replace("Male_18_45", averageOfGender.getDouble("average18_45"));
						ratingDistribution.replace("Male_45", averageOfGender.getDouble("averageMoreThan45"));
						ratingDistribution.replace("Male_All", averageOfGender.getDouble("averageOfGender"));
					}		
				}

			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return ratingDistribution;
	}

	public static Rating getRating(Film film, User user) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		Rating userRating = null;
		
		try{
			Document databaseRating = ratingCollection.find(Filters.and(Filters.eq("_id.userId", user.getId()),
																		Filters.eq("_id.filmId", film.getId()))).first();
			
			if(databaseRating != null) {
				Document idRating = (Document)databaseRating.get("_id");
				
				userRating = new Rating(idRating.getObjectId("userId"),
										idRating.getObjectId("filmId"),
										databaseRating.getDouble("rating"), 
										databaseRating.getLong("timestamp"),
										databaseRating.getInteger("age_of_user"),
										databaseRating.getString("gender"));
			}

		} catch(Exception exception) {
			exception.printStackTrace();
		}	
		
		return userRating;
	}
	
	public static boolean addRating(Rating rating) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		boolean querySuccess = false;
		
		try{
			// Check if the user rated the film
			Document databaseRating = ratingCollection.find(Filters.and(Filters.eq("_id.userId", rating.getUserId()),
																		Filters.eq("_id.filmId", rating.getFilmId()))).first();
			
			if(databaseRating != null)
				return querySuccess;
			
			// User has not a rating for the selected film, we can proceed with the insertion
			Document idRating  = new Document("userId", rating.getUserId()).append("filmId", rating.getFilmId());	
			Document newRating = new Document("_id", idRating) 
								 .append("rating", rating.getRating())
								 .append("timestamp", rating.getTimestamp())
								 .append("age_of_user", rating.getAgeOfUser())
								 .append("gender", rating.getGender());
			
			ratingCollection.insertOne(newRating);
			querySuccess = true;
			
			// Update number of ratings and mean rating
			Document databaseFilm = filmCollection.find(Filters.eq("_id", rating.getFilmId())).first();
			
			double oldAverage = DatabaseObjectConverter.convertToDouble(databaseFilm.get("average_rating"));
			int oldNumberOfRatings = databaseFilm.getInteger("number_of_ratings"); 
			double newAverage = ((oldAverage*oldNumberOfRatings)+ rating.getRating())/(oldNumberOfRatings+1);
			
			System.out.println("oldAverage : " + oldAverage + "\noldNumberOfRatings : " + oldNumberOfRatings + "\nnewAverage : " + newAverage);
						
			Document updateOperations = new Document("$set", new Document("average_rating", newAverage))
										.append("$inc", new Document("number_of_ratings", 1));
			
			filmCollection.updateOne(Filters.eq("_id", rating.getFilmId()),
									 updateOperations);
			
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}

	public static boolean updateRating(Rating rating) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		boolean querySuccess = false;
		
		try{
			// Check if the user rated the film
			Document oldRating = ratingCollection.find(Filters.and(Filters.eq("_id.userId", rating.getUserId()),
																   Filters.eq("_id.filmId", rating.getFilmId()))).first();
			
			if(oldRating == null)
				return querySuccess;
			
			// User has a rating for the selected film, we can proceed with the update
			Document updatedValues = new Document("rating", rating.getRating())
									 .append("timestamp", rating.getTimestamp())
									 .append("age_of_user", rating.getAgeOfUser());
					
			Document updateOperations = new Document("$set", updatedValues);
			
			UpdateResult result = ratingCollection.updateOne(Filters.and(Filters.eq("_id.userId", rating.getUserId()),
												   						 Filters.eq("_id.filmId", rating.getFilmId())),
									   						 updateOperations);
			if(result.getModifiedCount() == 1) {
				querySuccess = true;
								
				// Update number of ratings and mean rating
				Document databaseFilm = filmCollection.find(Filters.eq("_id", rating.getFilmId())).first();
				
				double oldAverage = DatabaseObjectConverter.convertToDouble(databaseFilm.get("average_rating"));
				int numberOfRatings = databaseFilm.getInteger("number_of_ratings");
				double oldRatingValue = oldRating.getDouble("rating");
				
				double newAverage = ((oldAverage*numberOfRatings)- oldRatingValue + rating.getRating())/numberOfRatings;
											
				filmCollection.updateOne(Filters.eq("_id", rating.getFilmId()),
										 Updates.set("average_rating", newAverage));
			}
				
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}

	public static boolean addFilms(List<Film> films) {
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		boolean querySuccess = false;
		
		List<Document> newFilms = CinemaSpaceArchive.generateListOfFilmDocuments(films);

		try{
			filmCollection.insertMany(newFilms);
			querySuccess = true;
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public static boolean deleteFilm(Film film) {
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		
		boolean querySuccess = false;
		
		try{
			DeleteResult resultFilm = filmCollection.deleteOne(Filters.eq("_id", film.getId()));
					
			if(resultFilm.getDeletedCount() == 1) {
				querySuccess = true;
				ratingCollection.deleteMany(Filters.eq("_id.filmId", film.getId()));
			}
			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}

	public static Map<String, Integer> generateMostRecurrentGenresByRatingIntervals(User user, double min, double max) {
		MongoCollection<Document> ratingCollection = cinemaSpaceDatabase.getCollection("Rating");
		Map<String, Integer> genresDistribution = new HashMap<>();
		
		List<Bson> aggregationQuery = mostRecurrentGenresByRatingIntervalsQuery(user, min, max);
		
		try(MongoCursor<Document> cursor = ratingCollection.aggregate(aggregationQuery).iterator()) {
			
			while(cursor.hasNext()) {
				Document ratingDocument = cursor.next();
				genresDistribution.put(ratingDocument.getString("genre"), ratingDocument.getInteger("count"));
			}
				
		} catch(Exception exception) {
				exception.printStackTrace();
		}
		
		return genresDistribution;
	}
	
	public static void increaseNumberOfVisits(Film film) {
		MongoCollection<Document> filmCollection = cinemaSpaceDatabase.getCollection("Film");
		
		try{
			
			filmCollection.updateOne(Filters.eq("_id", film.getId()),
									 Updates.inc("number_of_visits", 1));

		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

}
