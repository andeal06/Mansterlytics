package com.alec.db.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.Driver;
import com.alec.JDBCUtil;
import com.alec.game.model.Players;

public class CharDBUtil {
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static HashMap<String, Integer> insertPlayerChar(List<Players> Players, Connection conn) {
		
		LOGGER.fine("Inserting characters");
		
		// necessary variables
		ResultSet existsResultSet = null;
		ResultSet characterIDResultSet =  null;
		HashMap<String, Integer> characterIDs = new HashMap<String, Integer>();
		
		// Prepared Statements
		String charInsert = "INSERT INTO `mansterlogger`.`toon`(Account, Name) VALUES(?,?)";
		
		String charExists = "SELECT (Character_ID) FROM `mansterlogger`.`toon`"
				+ "WHERE Account=? AND Name=?";
		
		
		// Start processing Players
		for(Players i : Players) {
			
			try {
				
				// setup query and execute
				PreparedStatement charRecord = conn.prepareStatement(charExists);
				
				charRecord.setString(1, i.getAccount());
				charRecord.setString(2, i.getName());
				
				existsResultSet = charRecord.executeQuery();
				
				
				// if there is not a record insert into database
				
				if (existsResultSet.next() == false) {
					PreparedStatement insertPlayer = conn.prepareStatement(charInsert, Statement.RETURN_GENERATED_KEYS);
					
					insertPlayer.setString(1, i.getAccount());
					insertPlayer.setString(2, i.getName());
					
					int rowAffected = insertPlayer.executeUpdate();
					
					if(rowAffected == 1) {
						characterIDResultSet = insertPlayer.getGeneratedKeys();
						if(characterIDResultSet.next()) {
							characterIDs.put(i.getName(), characterIDResultSet.getInt(1)); 
						}
					}
				} else {
					// otherwise add the characters id directly to the returned map 
					characterIDs.put(i.getName(), existsResultSet.getInt(1));
					
				}
				
			} catch (SQLException ex) {
				LOGGER.severe(ex.getMessage());
			} finally {
				try {
					if( characterIDResultSet != null) {
						characterIDResultSet.close();
					}
					if( existsResultSet != null ) {
						existsResultSet.close();
					}
				} catch (SQLException e) {
					LOGGER.severe(String.format("Closing faulted with message: %d", e.getMessage()));
				}
		}
		
	}
		LOGGER.fine("Succesfully added in characters");
		return characterIDs;
	}	
}
