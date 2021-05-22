package com.alec.db.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.Driver;
import com.alec.JDBCUtil;
import com.alec.game.model.Buffs;
import com.alec.game.model.LogInfoRaw;
import com.alec.game.model.Players;

public class CharBoonsDBUtil {
	
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static String  insertCharacterBoons(LogInfoRaw info,  HashMap<String, Integer> charIDs, int FightID, Connection conn) {
		LOGGER.fine("Inserting fight boon data");
		
		ResultSet existsResultSet = null;
		
		//prepared statements
		String insertFightBoons = "INSERT INTO `mansterlogger`.`fight_boons` (Fight_ID, Character_ID, Boon_ID, FullFight)"
				+ "Values (?,?,?,?)";
		
		String recordExists = "SELECT * FROM `mansterlogger`.`fight_boons` WHERE Fight_ID=? AND Character_ID=?";
		
		try {
			
			//query existing record
			PreparedStatement record = conn.prepareStatement(recordExists);
			
			record.setInt(1, FightID);
			record.setInt(2, charIDs.get(info.getPlayers().get(0).getName()));
			
			existsResultSet = record.executeQuery();
			
			if ( existsResultSet.next() == false) {
				
				PreparedStatement insertBoons = conn.prepareStatement(insertFightBoons);
				for( Players i : info.getPlayers()) {
					// collect data for prepared statement
					List<Buffs> buffsPresent = i.getBuffUptimes();
					insertBoons.setInt(1, FightID);
					insertBoons.setInt(2, charIDs.get(i.getName()));
					for (Buffs buff : buffsPresent) {
						insertBoons.setInt(3, (int) buff.getId());
						insertBoons.setDouble(4, buff.getBuffData().get(0).getUptime());

						insertBoons.execute();
					}						
				}
				
			} else {
				try {
					if (existsResultSet != null) {
						existsResultSet.close();
					}
				} catch (SQLException e) {
					LOGGER.severe(String.format("Closing faulted with message: %d", e.getMessage()));
				} 
				
				return String.format("Records already exist for this fight. See fight ID %d for more info.", FightID);
			}
			
		} catch (SQLException ex) {
			LOGGER.severe(ex.getMessage()); 
		} finally {
			try {
				if (existsResultSet != null) {
					existsResultSet.close();
				}
			} catch (SQLException e) {
				LOGGER.severe(String.format("Closing faulted with message: %d", e.getMessage()));
			} 
		}
		
		return "Successfully created boon information for fight";
	}
}
