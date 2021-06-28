package com.alec.db.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.BoonCodes;
import com.alec.Driver;
import com.alec.game.model.BoonsGenGroup;
import com.alec.game.model.PlayerDPS;
import com.alec.game.model.Players;


// ****************************************** THIS IS NOW DEPRICATED USE BUILDHELPER02 ************************************************



public class BuildHelper {
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static BuildContainer decideBuild(Players player) {
		LOGGER.fine("Entering build helper");
				
		// check stats in order of healing, concentration, condition. 
		// then check generated boon against profession
		
		String profession = player.getProfession();
		
		// grab all generated boons 
		HashMap<Long, Double> boonMap = new HashMap<Long, Double>();
		List<BoonsGenGroup> boonsGenerated = player.getGroupBuffs();
		
		//grab dps distribution for power vs condi dps check
		// gets the list of targets. The targets each have a list of playerDPS by phase. 
		List<List<PlayerDPS>> playerDPSList= player.getPlayerDPS();
		// move into the main boss list of DPS by phase
		List<PlayerDPS> bossDPSList = playerDPSList.get(0);
		//grabs DPS and bar down on the boss over the entire fight 
		PlayerDPS fullFightDPSdown = bossDPSList.get(0);
		
		int powerDPS = fullFightDPSdown.getPowerDps();
		int condiDPS = fullFightDPSdown.getCondiDps();
		
		// return object creation
		BuildContainer playerBuildInfo= new BuildContainer();
		
		
		// TODO: Finish BuildHelper02 to replace this
		
		if (boonsGenerated != null) {
			
			for( BoonsGenGroup boon : boonsGenerated ) {
				// id, boon generated for full fight 
				boonMap.put(boon.getId(), boon.getBuffData().get(0).getGeneration());
			}
			
			if (player.getHealing() >= 3) {
				// TODO: this also does not correct select by profession
				if ("Chronomancer".equals(profession)) {
					playerBuildInfo.setPlayerBuild("Boon Chronomancer");
					playerBuildInfo.setBuildType("Support");
					
				} else {
					playerBuildInfo.setPlayerBuild("Heal" + profession);
					playerBuildInfo.setBuildType("Support");
					
				}
			} else if (player.getConcentration() >= 3) {
				// TODO: Currently doesn't work???? Just kicks to else statement 
				if (profession == "Renegade") {
					playerBuildInfo.setPlayerBuild("Alacrigade");
					playerBuildInfo.setBuildType("Support");
					
				} else {
					playerBuildInfo.setPlayerBuild("Boon" + profession);
					playerBuildInfo.setBuildType("Support");
					
				} // TODO: Add flags for builds that are close the cuttoffs.
			} else if (boonMap.get(BoonCodes.getQuickness()) != null && boonMap.get(BoonCodes.getQuickness()) >= 10.0) {
				playerBuildInfo.setPlayerBuild("Quickness" + profession);
				playerBuildInfo.setBuildType("Support");
								
			} else if (boonMap.get(BoonCodes.getAlacrity()) != null && boonMap.get(BoonCodes.getAlacrity()) >= 10.0 ) {	
				playerBuildInfo.setPlayerBuild("Alac" + profession);
				playerBuildInfo.setBuildType("Support");
							
			} else if (condiDPS > powerDPS) {
				playerBuildInfo.setPlayerBuild("Condition" + profession);
				playerBuildInfo.setBuildType("Condi");
				
			} else {
				playerBuildInfo.setPlayerBuild("Power" + profession);
				playerBuildInfo.setBuildType("Power");
				
			}
		} else {
			if (condiDPS > powerDPS) {					
				playerBuildInfo.setPlayerBuild("Condition" + profession);
				playerBuildInfo.setBuildType("Condi");
				
			} else if (player.getHealing() >= 3) {
				playerBuildInfo.setPlayerBuild("Heal" + profession);
				playerBuildInfo.setBuildType("Support");
			} else {
				playerBuildInfo.setPlayerBuild("Power" + profession);
				playerBuildInfo.setBuildType("Power");
			}
			
		}


		return playerBuildInfo;
	}
}
