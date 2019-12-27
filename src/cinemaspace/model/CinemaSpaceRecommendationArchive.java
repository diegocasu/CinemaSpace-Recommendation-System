package cinemaspace.model;

import java.util.*;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.Neo4jException;


public class CinemaSpaceRecommendationArchive {
	private Driver driverGraphDB;
	private String databaseAddress = "bolt://";
	
	private Void createUserNode(Transaction transaction, User user) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", user.getId().toString());
		
		Statement queryCreateUser = new Statement(
				"CREATE (u:User {objectId: $userId})",
				queryParameters);
		
		transaction.run(queryCreateUser);
		
		return null;
	}
	
	private Void deleteUserNode(Transaction transaction, User user) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", user.getId().toString());
		
		Statement queryDeleteUser = new Statement(
				"MATCH (u:User {objectId: $userId}) DETACH DELETE u",
				queryParameters);
		
		transaction.run(queryDeleteUser);
		
		return null;
	}
	
	private Void createRatingRelationship(Transaction transaction, Rating rating) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", rating.getUserId().toString());
		queryParameters.put("filmId", rating.getFilmId().toString());
		queryParameters.put("ratingValue", rating.getRating());
		queryParameters.put("timestampValue", rating.getTimestamp());
		
		Statement queryCreateRating = new Statement(
				"MATCH (u:User {objectId: $userId}), (f:Film {objectId: $filmId}) " + 
				"CREATE (u)-[r: RATED {rating: $ratingValue, timestamp: $timestampValue}]->(f)",
				queryParameters);
		
		transaction.run(queryCreateRating);
		
		return null;
	}
	
	private Void updateRatingRelationship(Transaction transaction, Rating rating) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", rating.getUserId().toString());
		queryParameters.put("filmId", rating.getFilmId().toString());
		queryParameters.put("ratingValue", rating.getRating());
		queryParameters.put("timestampValue", rating.getTimestamp());
		
		Statement queryUpdateRating = new Statement(
				"MATCH (u:User {objectId: $userId})-[r:RATED]->(f:Film {objectId: $filmId}) " + 
				"SET r.rating = $ratingValue, r.timestamp = $timestampValue",
				queryParameters);
		
		transaction.run(queryUpdateRating);
		
		return null;
	}
	
	private Void createFilmGenreRelationship(Transaction transaction, Film film, String genre) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("genreName", genre);
		queryParameters.put("filmId", film.getId().toString());
		
		Statement queryCreateGenreAndRelationship = new Statement(
				"MERGE (g:Genre {name: $genreName}) " + 
				"WITH g " + 
				"MATCH (f:Film {objectId: $filmId}) " + 
				"CREATE (f)-[:HAS_GENRE]->(g)",
				queryParameters);
		
		transaction.run(queryCreateGenreAndRelationship);
		
		return null;
	}
	
	private Void createFilmNode(Transaction transaction, Film film) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("filmId", film.getId().toString());

		Statement queryCreateFilm = new Statement(
				"CREATE (f:Film {objectId: $filmId})",
				queryParameters);
		
		transaction.run(queryCreateFilm);
		
		return null;
	}
	
	private Void deleteFilmNode(Transaction transaction, Film film) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("filmId", film.getId().toString());
		
		Statement queryDeleteFilm = new Statement(
				"MATCH (f:Film {objectId: $filmId}) DETACH DELETE f",
				queryParameters);
		
		transaction.run(queryDeleteFilm);
		
		return null;
	}
	
	private Void deleteGenreNodesWithoutEdges(Transaction transaction) {		
		Statement queryDeleteGenres = new Statement(
				"MATCH (g:Genre) " +
				"WHERE NOT((g)<-[:HAS_GENRE]-(:Film)) " +
				"DELETE g");
		
		transaction.run(queryDeleteGenres);
		
		return null;
	}
	
	private Void deleteRatingRelationship(Transaction transaction, Rating rating) {		
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", rating.getUserId().toString());
		queryParameters.put("filmId", rating.getFilmId().toString());
		
		Statement queryDeleteRating = new Statement(
				"MATCH (u:User {objectId: $userId})-[r:RATED]->(f:Film {objectId: $filmId}) " + 
				"DELETE r",
				queryParameters);
		
		transaction.run(queryDeleteRating);
		return null;
	}
	
	private List<String> generateRecommendationsOnGenre(Transaction transaction, User user) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", user.getId().toString());
		
		Statement queryRecommendations = new Statement(
				"MATCH (user:User {objectId: $userId})-[rated:RATED]->(film:Film) " + 
				"WHERE rated.rating >= 2.5 " + 
				"WITH rated, film, user " + 
				"ORDER BY rated.timestamp DESC " + 
				"LIMIT 3 " + 
				"MATCH (film)-[:HAS_GENRE]->(:Genre)<-[:HAS_GENRE]-(suggestedFilm:Film) " + 
				"WHERE NOT ((user)-[:RATED]->(suggestedFilm)) " + 
				"RETURN DISTINCT suggestedFilm.objectId " + 
				"ORDER BY rand() " + 
				"LIMIT 20 ", queryParameters);
		

		List<String> filmList = new ArrayList<>();
		StatementResult result = transaction.run(queryRecommendations);
		
		while(result.hasNext()) {
			filmList.add(result.next().get(0).toString());
		}
		
		return filmList;
	}

	private List<String> generateRecommendationsOnOtherUsers(Transaction transaction, User user) {
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("userId", user.getId().toString());
		
		Statement queryRecommendations = new Statement(
				"MATCH (user:User {objectId: $userId})-[:RATED]->(:Film)<-[:RATED]-(otherUser:User) " + 
				"WITH DISTINCT user, otherUser " + 
				"ORDER BY rand() " + 
				"LIMIT 1000 " + 
				"MATCH (user:User {objectId: $userId})-[:RATED]->(film:Film) " + 
				"WITH user, collect(id(film)) as userFilms, otherUser " + 
				"MATCH (otherUser:User)-[:RATED]->(film:Film) " + 
				"WITH user, userFilms, otherUser, collect(id(film)) as otherUserFilms " + 
				"WITH user, otherUser, algo.similarity.jaccard(userFilms, otherUserFilms) AS similarity " + 
				"ORDER BY similarity DESC " + 
				"LIMIT 5 " + 
				"MATCH (otherUser:User)-[rated:RATED]->(film:Film) " + 
				"WHERE rated.rating >= 2.5 AND NOT((film)<-[:RATED]-(user:User)) " + 
				"RETURN DISTINCT film.objectId " + 
				"ORDER BY rand() " + 
				"LIMIT 20 ", queryParameters);
		

		List<String> filmList = new ArrayList<>();
		StatementResult result = transaction.run(queryRecommendations);
		
		while(result.hasNext()) {
			filmList.add(result.next().get(0).toString());
		}
		
		return filmList;
	}	
	
	
	
	public boolean openConnection(String connectionString, String user, String password) {
		databaseAddress += connectionString;
		boolean connectionEstablished = false;
		
		try {
			driverGraphDB = GraphDatabase.driver(databaseAddress, AuthTokens.basic(user, password));
			connectionEstablished = true;
			
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return connectionEstablished;
	}
	
	public void closeConnection() {
		driverGraphDB.close();
	}

	public boolean addUser(User user) {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return createUserNode(transaction, user);
				}
			});
			
			querySuccess = true;
			
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public boolean deleteUser(User user) {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return deleteUserNode(transaction, user);
				}
			});
			
			querySuccess = true;
			
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public boolean addRating(Rating rating) {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return createRatingRelationship(transaction, rating);
				}
			});
			
			querySuccess = true;
			
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public boolean updateRating(Rating rating) {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return updateRatingRelationship(transaction, rating);
				}
			});
			
			querySuccess = true;
			
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public boolean deleteRating(Rating rating) {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return deleteRatingRelationship(transaction, rating);
				}
			});
			
			querySuccess = true;
			
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return querySuccess;
	}
	
	public boolean addFilms(List<Film> films) {
		for(Film film : films) {
			
			// Create film node
			try(Session session = driverGraphDB.session()) {
				session.writeTransaction(new TransactionWork<Void>() 
				{
					public Void execute(Transaction transaction){
						return createFilmNode(transaction, film);
					}
				});
							
			} catch(Neo4jException exception) {
					exception.printStackTrace();
					return false;
			}			
			
			// Create genre nodes if they don't exist and their relationships with the film
			for(String genre : film.getGenres()) {
				try(Session session = driverGraphDB.session()) {
					session.writeTransaction(new TransactionWork<Void>() 
					{
						public Void execute(Transaction transaction){
							return createFilmGenreRelationship(transaction, film, genre);
						}
					});
					
				} catch(Neo4jException exception) {
					exception.printStackTrace();
					return false;
				}
			}
		
		}
		
		return true;
	}
	
	public boolean deleteGenresWithoutRelationships() {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return deleteGenreNodesWithoutEdges(transaction);
				}
			});
				
			querySuccess = true;
			
		} catch(Neo4jException exception) {
				exception.printStackTrace();
		}	
		
		return querySuccess;
	}
	
	public boolean deleteFilm(Film film) {
		boolean querySuccess = false;
		
		try(Session session = driverGraphDB.session()) {
			session.writeTransaction(new TransactionWork<Void>() 
			{
				public Void execute(Transaction transaction){
					return deleteFilmNode(transaction, film);
				}
			});
			
			querySuccess = true;
			
		} catch(Neo4jException exception) {
				exception.printStackTrace();
		}	
		
		return querySuccess;
	}
	
	public List<String> requestFilmRecommendationsBasedOnGenre(User user) {
		List<String> filmList = new ArrayList<>();
		
		try(Session session = driverGraphDB.session()) {
			filmList = session.readTransaction( new TransactionWork<List<String>>() 
			{
				public List<String> execute(Transaction transaction){
					return generateRecommendationsOnGenre(transaction, user);
				}
			});
				
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return filmList;
	}
	
	public List<String> requestFilmRecommendationsBasedOnOtherUsersWithCommonInterests(User user) {
		List<String> filmList = new ArrayList<>();
		
		try(Session session = driverGraphDB.session()) {
			filmList = session.readTransaction( new TransactionWork<List<String>>() 
			{
				public List<String> execute(Transaction transaction){
					return generateRecommendationsOnOtherUsers(transaction, user);
				}
			});
				
		} catch(Neo4jException exception) {
			exception.printStackTrace();
		}
		
		return filmList;
	}

}
