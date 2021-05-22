package com.alec.db.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.Driver;
import com.alec.JDBCUtil;
import com.alec.game.model.LogInfoRaw;
import com.alec.game.model.Players;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DBBuilder {
	
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static void buildTables(File file) throws Exception{
		
		//TODO system.println -> log printouts 
		LOGGER.info(String.format("Parsing %s", file.getName()));
		
		ObjectMapper mapper = new ObjectMapper();
		LogInfoRaw info = mapper.readValue(file, LogInfoRaw.class);
		
		Connection myConn = null;
		int fightID = -666;
		try {
			myConn = JDBCUtil.getConnection();
			
			// turn off auto commit
			myConn.setAutoCommit(false);
			
			//insert fight 
			fightID = FightDBUtil.insertFight(info, myConn);
			
			//insert players
			List<Players> Players = info.getPlayers();
			HashMap<String, Integer> playerIDs = CharDBUtil.insertPlayerChar(Players, myConn);
			LOGGER.finer(playerIDs.toString());
			
			//insert main/damage table
			String mainTableReturn = MainDBUtil.insertMain(info, playerIDs, fightID, myConn);
			LOGGER.fine(mainTableReturn);
			
			//insert boons
			String boonTableReturn = CharBoonsDBUtil.insertCharacterBoons(info, playerIDs, fightID, myConn);
			LOGGER.fine(boonTableReturn);
			
			// if all successful commit 
			myConn.commit();
			myConn.close();
			
			LOGGER.info(String.format("Succesfully parsed JSON file. See Fight ID: %d. ", fightID));
			
		} catch (Exception ex) {
			LOGGER.warning(String.format("DB upload fail with exception: %s. Rolling back. Check file: %s", ex, file.getName()));
			myConn.rollback();
			myConn.close();
		}
		
		return;
		
	}
	
}
