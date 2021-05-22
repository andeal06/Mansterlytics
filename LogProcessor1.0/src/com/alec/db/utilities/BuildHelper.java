package com.alec.db.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.BoonCodes;
import com.alec.Driver;
import com.alec.game.model.BoonsGenGroup;
import com.alec.game.model.LogInfoRaw;
import com.alec.game.model.PlayerDPS;
import com.alec.game.model.Players;

public class BuildHelper {
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static HashMap<String, String> decideBuild(LogInfoRaw info) {
		LOGGER.fine("Entering build helper");
		
		HashMap<String, String> playerBuilds = new HashMap<String, String>();
		List<Players> Players = info.getPlayers();
		
		
		for( Players i : Players) {
			// check stats in order of healing, concentration, condition. 
			// then check generated boon against profession
			
			String profession = i.getProfession();
			String playerName = i.getName();
			
			// grab all generated boons 
			HashMap<Long, Double> boonMap = new HashMap<Long, Double>();
			List<BoonsGenGroup> boonsGenerated = i.getGroupBuffs();
			
			//grab dps distribution for power vs condi dps check
			// gets the list of targets. The targets each have a list of playerDPS by phase. 
			List<List<PlayerDPS>> playerDPSList= i.getPlayerDPS();
			// move into the main boss list of DPS by phase
			List<PlayerDPS> bossDPSList = playerDPSList.get(0);
			//grabs DPS and breakbar breakdown on the boss over the entire fight 
			PlayerDPS fullFightDPSBreakdown = bossDPSList.get(0);
			
			int powerDPS = fullFightDPSBreakdown.getPowerDps();
			int condiDPS = fullFightDPSBreakdown.getCondiDps();
			
			
			// TODO: This can be rewritten using a switch based on profession
			// update would provide more specific builds based on meta but much more hard-coded  
			
			if (boonsGenerated != null) {
				
				for( BoonsGenGroup boon : boonsGenerated ) {
					// id, boon generated for full fight 
					boonMap.put(boon.getId(), boon.getBuffData().get(0).getGeneration());
				}
				
				if (i.getHealing() >= 3) {
					// TODO: this also does not correct select by profession
					if (profession == "Chronomancer") {
						playerBuilds.put(playerName,  "Boon Chronomancer");
						continue;
					} else {
						playerBuilds.put(playerName, "Heal " + profession);
						continue;
					}
				} else if (i.getConcentration() >= 3) {
					// TODO: Currently doesn't work???? Just kicks to else statement 
					if (profession == "Renegade") {
						playerBuilds.put(playerName, "Alacrigade");
						continue;
					} else {
						playerBuilds.put(playerName, "Boon " + profession);
						continue;
					} // TODO: Add flags for builds that are close the cuttoffs. Also add for condi vs power damage
				} else if (boonMap.get(BoonCodes.getQuickness()) != null && boonMap.get(BoonCodes.getQuickness()) >= 10.0) {					
					playerBuilds.put(playerName, "Quickness " + profession);
					continue;					
				} else if (boonMap.get(BoonCodes.getAlacrity()) != null && boonMap.get(BoonCodes.getAlacrity()) >= 10.0 ) {				
					playerBuilds.put(playerName, "Alac " + profession);
					continue;					
				} else if (condiDPS > powerDPS) {
					playerBuilds.put(playerName, "Condition " + profession);
					continue;
				} else {
					playerBuilds.put(playerName, "Power " + profession);
					continue;
				}
			} else {
				if (condiDPS > powerDPS) {					
					playerBuilds.put(playerName, "Condition " + profession);
					continue;
				} else if (i.getHealing() >= 3) {
					playerBuilds.put(playerName, "Heal " + profession);
					continue;
				} else {
					playerBuilds.put(playerName, "Power " + profession);
					continue;
				}
				
			}
			
		}
		return playerBuilds;
	}
}
