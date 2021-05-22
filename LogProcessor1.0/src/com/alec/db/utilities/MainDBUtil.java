package com.alec.db.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.BoonCodes;
import com.alec.Driver;
import com.alec.JDBCUtil;
import com.alec.game.model.DamageMods;
import com.alec.game.model.LogInfoRaw;
import com.alec.game.model.PlayerDPS;
import com.alec.game.model.Players;

public class MainDBUtil {
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static String insertMain(LogInfoRaw info, HashMap<String, Integer> charIDs, int FightID, Connection conn) {
		LOGGER.fine("Inserting main table data.");
		
		ResultSet existsResultSet = null;
		
		//prepared statements 
		String insertMain = "INSERT INTO `mansterlogger`.`main` (Fight_ID, Character_ID, Profession, "
				+ "Build, DPS, PowerDPS, CondiDPS, Breakbar, CriticalRate, FlankingRate, Scholar) Values (?,?,?,?,?,?,?,?,?,?,?)";
		
		String mainRecordExists = "SELECT * FROM `mansterlogger`.`main`"
				+ "WHERE Fight_ID=? AND Character_ID=?";
		
		// begin processing 
		try {
			
			// query to check for an existing record with the first player ID and fight ID combo
			PreparedStatement mainRecord = conn.prepareStatement(mainRecordExists);
			
			mainRecord.setInt(1, FightID);
			mainRecord.setInt(2, charIDs.get(info.getPlayers().get(0).getName()));
			
			existsResultSet = mainRecord.executeQuery();
			
			// if no record found for that combo generate the new record. 
			// if there was a record found there "should" be a record for all of the players.
			// "should" be safe to exit execution and return separate statement. 
			if ( existsResultSet.next() == false) {
				// run builds tool to get player builds 
				HashMap<String, String> playerBuilds = BuildHelper.decideBuild(info);
				for (Players i :  info.getPlayers()) {
					PreparedStatement insertMainRecord = conn.prepareStatement(insertMain);
					
					insertMainRecord.setInt(1, FightID);
					insertMainRecord.setInt(2, charIDs.get(i.getName()));
					insertMainRecord.setString(3, i.getProfession());
					insertMainRecord.setString(4, playerBuilds.get(i.getName()));
					
					// ************** DPS and Breakbar Data Retrieval *********************
					// gets the list of targets. The targets each have a list of playerDPS by phase. 
					List<List<PlayerDPS>> playerDPSList= i.getPlayerDPS();
					// move into the main boss list of DPS by phase
					List<PlayerDPS> bossDPSList = playerDPSList.get(0);
					//grabs DPS and breakbar breakdown on the boss over the entire fight 
					PlayerDPS fullFightDPSBreakdown = bossDPSList.get(0);
					
					insertMainRecord.setInt(5, fullFightDPSBreakdown.getDps());
					insertMainRecord.setInt(6, fullFightDPSBreakdown.getPowerDps());
					insertMainRecord.setInt(7, fullFightDPSBreakdown.getCondiDps());
					insertMainRecord.setInt(8, fullFightDPSBreakdown.getBreakbarDamage());
					
					// *************** Scholar Flanking Crit Logic *************************
					// damage modifiers. specifically to get scholar uptime atm
					//TODO Clean to allow for nulls here and in database. Scholar as well
					HashMap<String, Float> modifierUptime = new HashMap<String, Float>();
					List<DamageMods> damageModsPresent = i.getDamageModifiers();
					for (DamageMods mod : damageModsPresent) {
						float modifierHits = mod.getDamageModifiers().get(0).getHitCount();
						float totalHits =  mod.getDamageModifiers().get(0).getTotalHitCount();
						float percentUptime;
						
						if (totalHits == 0) {
							percentUptime = (float) 0.0;
						} else {
							percentUptime = modifierHits / totalHits;
						}
						
						modifierUptime.put(mod.getId(), percentUptime);
					}
					
					Float scholar = modifierUptime.get(BoonCodes.getScholar());
					
					// do not remove. will break for players who have not struck any targets
					if (scholar == null) {
						scholar = (float) 0.0;
					}
					
					Float criticalHits = (float) i.getPlayerStats().get(0).get(0).getcriticalRate();
					Float flankingHits = (float) i.getPlayerStats().get(0).get(0).getflankingRate();
					Float totalHits = (float) i.getPlayerStats().get(0).get(0).getconnectedDirectDamageCount();
					
					Float criticalRate;
					Float flankingRate;
				
					if (totalHits == 0) {
						criticalRate = (float) 0.0;
						flankingRate = (float) 0.0;
					} else {
						criticalRate = (criticalHits / totalHits);
						flankingRate = (flankingHits / totalHits);
					}
					
					insertMainRecord.setFloat(9, criticalRate);
					insertMainRecord.setFloat(10, flankingRate);
					insertMainRecord.setFloat(11, scholar);
					
					@SuppressWarnings("unused")
					int rowsAdded = insertMainRecord.executeUpdate();
				}
					
			} else {
				//aforementioned separate return statement if there are already existing records with 
				//same fight and player id
				
				try {
					if (existsResultSet != null) {
						existsResultSet.close();
					}
				} catch (SQLException e) {
					LOGGER.severe(String.format("Closing faulted with message: %d", e.getMessage()));
				} 
				
				return String.format("Records already exist for this fight. See fight ID %d for more info.", FightID);
			}
	
		
		}  catch (SQLException ex) {
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
		
		
		return "Successfully created main table records";
	}
}
