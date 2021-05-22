package com.alec.db.utilities;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import com.alec.Driver;
import com.alec.JDBCUtil;
import com.alec.game.model.LogInfoRaw;

public class FightDBUtil {
	
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static int insertFight(LogInfoRaw info, Connection conn){
		LOGGER.fine("Inserting fight.");
		
		ResultSet existsResultSet = null;
		ResultSet fightIDResultSet = null;
		int fightID = 0;
		
		//prepared Statements
		String insertFight = "INSERT INTO `mansterlogger`.`fight` (Log, Duration, Date, Boss_Name)"
				+ "VALUES(?,?,?,?)";
		
		String fightExists = "SELECT (Fight_ID) FROM `mansterlogger`.`fight`"
				+ "WHERE Log=?";
		
		
		//process the fight
		try {
			
			//setup query and execute
			PreparedStatement fightRecord = conn.prepareStatement(fightExists);
			
			fightRecord.setString(1, info.getUploadLinks().get(0));
			
			existsResultSet = fightRecord.executeQuery();
			
			// if not a record, insert
			if (existsResultSet.next() == false) {
				PreparedStatement insertRecord = conn.prepareStatement(insertFight, Statement.RETURN_GENERATED_KEYS);
				
				insertRecord.setString(1, info.getUploadLinks().get(0));
				
				// insert duration 
				// duration string conversion and math 
				// logs dont 0 pad ms (ex 25ms instead of 025ms)
				String durationRaw = info.getDuration();
				long ms;
				switch (durationRaw.length()) {
				case 11:
					// format XXm XXs Xms
					ms = Long.parseLong(durationRaw.substring(0,2))*60000L;
					ms += Long.parseLong(durationRaw.substring(4,6))*1000L;
					ms += Long.parseLong(durationRaw.substring(8,9));
					break;
				case 12:
					// format XXm XXs XXms
					ms = Long.parseLong(durationRaw.substring(0,2))*60000L;
					ms += Long.parseLong(durationRaw.substring(4,6))*1000L;
					ms += Long.parseLong(durationRaw.substring(8,10));
					break;
				default:
					// format XXm XXs XXXms
					ms = Long.parseLong(durationRaw.substring(0,2))*60000L;
					ms += Long.parseLong(durationRaw.substring(4,6))*1000L;
					ms += Long.parseLong(durationRaw.substring(8,11));
				}
				
				insertRecord.setLong(2, ms);
				
				// insert date. info date is a util.date but we need sql.date. convert to sql date by getting the time and making a new sql date 
				String stringDate = info.getTimeStartStd();
				String cutStringDate = stringDate.substring(0, 10);				
				DateFormat myDate = new SimpleDateFormat("yyyy-MM-dd");
				
				java.util.Date utilDate;
				java.sql.Date timestamp = null;
				try {
					utilDate = myDate.parse(cutStringDate);
					timestamp = new java.sql.Date(utilDate.getTime());
				} catch (ParseException e) {
					LOGGER.severe("Failed to parse date for fight.");
				}
				
				insertRecord.setDate(3, timestamp);
				
				insertRecord.setString(4, info.getFightName());
				
				int rowAffected = insertRecord.executeUpdate();
				
				if(rowAffected == 1) {
					fightIDResultSet = insertRecord.getGeneratedKeys();
					if(fightIDResultSet.next()) {
						fightID = fightIDResultSet.getInt(1);
					}
				}
				
			} else {
				// return the existing record
				fightID = existsResultSet.getInt(1);
				LOGGER.fine(String.format("Fight already exists. See fight %d.", fightID));
			}
			
		} catch (SQLException ex) {
			LOGGER.severe(ex.getMessage());
		} finally {
			try {
				if( fightIDResultSet != null) {
					fightIDResultSet.close();
				}
				if( existsResultSet != null) {
					existsResultSet.close();
				}
			} catch (SQLException e) {
				LOGGER.severe(String.format("Closing faulted with message: %d", e.getMessage()));
			}
		}
		LOGGER.fine(String.format("New fight %d added to database.", fightID));
		return fightID;
	}

}
