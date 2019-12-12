package cinemaspace.model;


import java.util.*;
import org.bson.*;

public class DatabaseObjectConverter {
	
	public static double convertToDouble(Object databaseField) {
		double defaultValue = 0.0;
		
		if(databaseField.getClass() == Double.class)
			return (Double) databaseField;
		
		if(databaseField.getClass() == Integer.class)
			return ((Number)databaseField).doubleValue();
		
		return defaultValue;
	}
	
	public static int convertToInteger(Object databaseField) {
		int defaultInteger = 0;
		
		if(databaseField.getClass() == Integer.class)
			return (Integer) databaseField;
		
		if(databaseField.getClass() == Double.class)
			return ((Number)databaseField).intValue();
		
		return defaultInteger;
	}

	public static String convertToString(Object databaseField) {
		String defaultString = "";
		
		if(databaseField.getClass() == String.class)
			return (String) databaseField;
		
		if(databaseField.getClass() == Integer.class || databaseField.getClass() == Double.class)
			return String.valueOf(databaseField);
		
		return defaultString;
	}
	
	public static List<CastMember> convertToCastMemberList(List<Document> cast) {
		List<CastMember> convertedCast = new ArrayList<>();
				
		for(Document castMember : cast)
			convertedCast.add(new CastMember(castMember.getString("character"),
											 castMember.getString("name"),
											 castMember.getInteger("order"),
											 castMember.getString("profile_path")));
		
		return convertedCast;
	}
	
	public static List<CrewMember> convertToCrewMemberList(List<Document> crew) {
		List<CrewMember> convertedCrew = new ArrayList<>();
		
		for(Document crewMember : crew)
			convertedCrew.add(new CrewMember(crewMember.getString("department"),
											 crewMember.getString("job"),
											 crewMember.getString("name"),
											 crewMember.getString("profile_path")));
			
		return convertedCrew;
	}

}
