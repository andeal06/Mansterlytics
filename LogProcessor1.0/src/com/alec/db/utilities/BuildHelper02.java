package com.alec.db.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.alec.BoonCodes;
import com.alec.Driver;
import com.alec.game.model.BoonsGenGroup;
import com.alec.game.model.Buffs;
import com.alec.game.model.DamageMods;
import com.alec.game.model.PlayerDPS;
import com.alec.game.model.Players;

public class BuildHelper02 {
	private static final Logger LOGGER = Logger.getLogger(Driver.class.getName());
	
	public static BuildContainer decideBuild2(Players player) {
		LOGGER.fine("Entering build helper");
		
		BuildContainer playerBuildInfo = new BuildContainer();

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
		//grabs DPS and breakbar breakdown on the boss over the entire fight 
		PlayerDPS fullFightDPSBreakdown = bossDPSList.get(0);
		
		int powerDPS = fullFightDPSBreakdown.getPowerDps();
		int condiDPS = fullFightDPSBreakdown.getCondiDps();
		
		
		if (boonsGenerated != null) {
			
			for( BoonsGenGroup boon : boonsGenerated ) {
				// id, boon generated for full fight 
				boonMap.put(boon.getId(), boon.getBuffData().get(0).getGeneration());
			}
			// check for specific boons by class. 
			// not all classes will have unique checks. cases left for completeness and future uses 
			// generic heal checks are sometime doubled for clarity / naming purposes 
			switch(profession) {
			
					// ***************** Guardian Builds **********************
				case "Guardian":
					// Perfect inscriptions check 
					if (boonMap.get(BoonCodes.getBaneSignet()) != null) {
						playerBuildInfo.setPlayerBuild("Power Guardian (PI)");
						playerBuildInfo.setBuildType("Power");
						break;
					}
					break;
				case "Dragonhunter":
					// Perfect inscriptions check 
					if (boonMap.get(BoonCodes.getBaneSignet()) != null) {
						playerBuildInfo.setPlayerBuild("Power Dragonhunter (PI)");
						playerBuildInfo.setBuildType("Power");
						break;
					}
					
					// virtues check
					// process damage modifiers in order to check 
					HashMap<String, Float> modifierUptime = new HashMap<String, Float>();
					List<DamageMods> damageModsPresent = player.getDamageModifiers();
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
					
					if(modifierUptime.get(BoonCodes.getInspiredVirtue()) != null) {
						// check again for Perfect inscriptions
						if (boonMap.get(BoonCodes.getBaneSignet()) != null) {
							playerBuildInfo.setPlayerBuild("Virtues Dragonhunter (PI)");
							playerBuildInfo.setBuildType("Power");
							break;
						} else {
							playerBuildInfo.setPlayerBuild("Virtues Dragonhunter");
							playerBuildInfo.setBuildType("Power");
							break;
						}
					}
					
					break;
				
				case "Firebrand":
					// Healbrand Check
					if (player.getHealing() >= 3) {
						playerBuildInfo.setPlayerBuild("Healbrand");
						playerBuildInfo.setBuildType("Healer");
						break;
					}
					
					// Quickbrand check
					if (boonMap.get(BoonCodes.getQuickness()) != null && boonMap.get(BoonCodes.getQuickness()) >= 10.0) {
						if (condiDPS > powerDPS) {
							playerBuildInfo.setPlayerBuild("Condi QuickBrand");
							playerBuildInfo.setBuildType("Support");
							break;
						} else {
							// Perfect inscriptions check 
							// this build should be incredibly rare in raids and only useful for future fractal support. 
							if (boonMap.get(BoonCodes.getBaneSignet()) != null) {
								playerBuildInfo.setPlayerBuild("Power Quickbrand (PI)");
								playerBuildInfo.setBuildType("Support");
								break;
							} else {
								playerBuildInfo.setPlayerBuild("Power Quickbrand");
								playerBuildInfo.setBuildType("Support");
								break;
							}		
						}
					}
					
					break;
					
					
					// ***************** Revenant Builds **********************
				case "Revenant":
					break;
				case "Herald":
					// no checks for boon vs dps herald. near impossible to tell difference without being told explicitly 
					break;
				case "Renegade":
					// Healren check
					if (player.getHealing() > 5) {
						playerBuildInfo.setPlayerBuild("Healren");
						playerBuildInfo.setBuildType("Healer");
					}
					
					// Diviner check
					if (player.getConcentration() > 3) {
						playerBuildInfo.setPlayerBuild("Alacrigade");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					
					// RR check
					if (boonMap.get(BoonCodes.getAlacrity()) != null && boonMap.get(BoonCodes.getAlacrity()) >= 10.0) {
						if (condiDPS > powerDPS) {
							playerBuildInfo.setPlayerBuild("Condi RR");
							playerBuildInfo.setBuildType("Hybrid Support");
							break;
						} else {
							playerBuildInfo.setPlayerBuild("Power RR");
							playerBuildInfo.setBuildType("Hybrid Support");
							break;
						}	
					}
					
					// Devastation check (only catches via Assassins Presence on condi damage) 
					if (condiDPS > powerDPS) {
						if (boonMap.get(BoonCodes.getAssassinPresence()) != null) {
							playerBuildInfo.setPlayerBuild("Devastation Condi Ren");
							playerBuildInfo.setBuildType("Condi");
							break;
						} else {
							playerBuildInfo.setPlayerBuild("Invocation Condi Ren");
							playerBuildInfo.setBuildType("Condi");
							break;
						}
					}
					
					
					break;
				
					// ***************** Warrior Builds **********************
				case "Warrior":
					// no current checks for support warrior 
					break;
				case "Berserker":
					// banner check 
					if (boonMap.get(BoonCodes.getDisciplineBanner()) != null | boonMap.get(BoonCodes.getStrengthBanner()) != null) {
						if (condiDPS > powerDPS) {
							playerBuildInfo.setPlayerBuild("Condi Bannerslave");
							playerBuildInfo.setBuildType("Support");
							break;
						} else {
							//tactics check
							if (boonMap.get(BoonCodes.getEmpowerAllies()) != null) {
								playerBuildInfo.setPlayerBuild("Tactics Bannerslave");
								playerBuildInfo.setBuildType("Support");
								break;
							} else {
								playerBuildInfo.setPlayerBuild("Power Bannerslave");
								playerBuildInfo.setBuildType("Support");
								break;
							}
						}
					}
					break;
					
				case "Spellbreaker":
					// heal check. Rare meme but fun to include
					if (player.getHealing() > 3) {
						playerBuildInfo.setPlayerBuild("Heal Shoutbreaker");
						playerBuildInfo.setBuildType("Healer");
						break;
					}
					
					//banners check 
					if (boonMap.get(BoonCodes.getDisciplineBanner()) != null | boonMap.get(BoonCodes.getStrengthBanner()) != null) {
						playerBuildInfo.setPlayerBuild("Banner Spellbreaker");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					
					break;
				
					// ***************** Engineer Builds **********************
				case "Engineer":
					break;
				case "Scrapper":
					// heal scrapper check 
					if (player.getHealing() > 5) {
						playerBuildInfo.setPlayerBuild("Heal Scrapper");
						playerBuildInfo.setBuildType("Healer");
						break;
					}
					
					//quickness check
					if (boonMap.get(BoonCodes.getQuickness()) != null && boonMap.get(BoonCodes.getQuickness()) >= 10.0) {
						playerBuildInfo.setPlayerBuild("Quickness Scrapper");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					break;
				case "Holosmith":
					break;
					
					// ***************** Ranger Builds **********************
				case "Ranger":
					break;
				case "Druid":
					// no special builds at this time need to be checked just kick to generic assignment
					break;
				case "Soulbeast":
					// spirits check 
					if (boonMap.get(BoonCodes.getFrostSpirit()) != null | boonMap.get(BoonCodes.getSunSpirit()) != null | boonMap.get(BoonCodes.getStoneSpirit()) != null) {
						playerBuildInfo.setPlayerBuild("SpiritBeast");
						playerBuildInfo.setBuildType("Hybrid Support");
						break;
					}
					break;
					
					// ***************** Thief Builds **********************
				case "Thief":
					if (player.getConcentration() > 3) {
						playerBuildInfo.setPlayerBuild("Boon Thief");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					break;
				case "Daredevil":
					// check only if there is diviners gear otherwise dps DDs can be misindentified from detonate plasma usage
					if (player.getConcentration() > 3) {
						playerBuildInfo.setPlayerBuild("Boon Daredevil");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					break;
				case "Deadeye":
					//boon deadeye not tracked as its basically nonexistant
					break;
					
					// ***************** Elementalist Builds **********************
				case "Elementalist":
					break;
				case "Tempest":
					//heal check
					if (player.getHealing() > 5) {
						playerBuildInfo.setPlayerBuild("Heal Tempest");
						playerBuildInfo.setBuildType("Heal");
					}
					// no check for hybrid boon tempests. hard to differentiate boons from overloads. 
					break;
				case "Weaver":
					break;
					
					// ***************** Mesmer Builds **********************
				case "Mesmer":
					break;
				case "Chronomancer":
					// concentration check
					// lumps all variants together if concentration present in build
					if (player.getConcentration() > 3) {
						playerBuildInfo.setPlayerBuild("Boon Chrono");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					
					// inspiration check **ONLY CHECKS VIA SIGNET MAY BE WRONG**
					//need to process boons in order to search for signet of inspiration 
					List<Buffs> buffsPresent = player.getBuffUptimes();
					HashMap<Long, Double> buffMap = new HashMap<Long, Double>();
					for (Buffs buff : buffsPresent) { 
						buffMap.put(buff.getId(), buff.getBuffData().get(0).getUptime());
					}
					if (buffMap.get(BoonCodes.getSignetOfInspiration()) != null) {
						playerBuildInfo.setPlayerBuild("Inpiration Chrono");
						playerBuildInfo.setBuildType("Support");
						break;
					}
					
					// STM check. not checking GS vs focus 
					if (boonMap.get(BoonCodes.getQuickness()) != null && boonMap.get(BoonCodes.getQuickness()) >= 10.0) {
						playerBuildInfo.setPlayerBuild("STM Chrono");
						playerBuildInfo.setBuildType("Hybrid Support");
						break;
					}
					
					break;
				case "Mirage":
					// Alac check 
					// this cannot check between staff dps mirage and staff alac mirage. label as DPS (its high damage is enough anyway xD)
					if (boonMap.get(BoonCodes.getAlacrity()) != null && boonMap.get(BoonCodes.getAlacrity()) >= 10.0) {
						playerBuildInfo.setPlayerBuild("Staff Mirage");
						playerBuildInfo.setBuildType("Condi");
					}
					break;
					
					// ***************** Necromancer Builds **********************
				case "Necromancer":
					break;
				case "Reaper":
					break;
				case "Scourge":
					break;
			}
					
					// ***************** Default Assignment **********************
			// This covers the case where only unimportant boons were generated and gives general build assignment
			
			if (playerBuildInfo.getPlayerBuild() == null) {
				if (condiDPS > powerDPS) {					
					playerBuildInfo.setPlayerBuild("Condition " + profession);
					playerBuildInfo.setBuildType("Condi");
					
				} else if (player.getHealing() >= 5) {
					playerBuildInfo.setPlayerBuild("Heal " + profession);
					playerBuildInfo.setBuildType("Healer");
				} else {
					playerBuildInfo.setPlayerBuild("Power " + profession);
					playerBuildInfo.setBuildType("Power");
				}
			}
				
			
		} else {
			// this covers the uncommon case where absolutely no boons where generated by a player 
			if (condiDPS > powerDPS) {					
				playerBuildInfo.setPlayerBuild("Condition " + profession);
				playerBuildInfo.setBuildType("Condi");
				
			} else if (player.getHealing() >= 5) {
				playerBuildInfo.setPlayerBuild("Heal " + profession);
				playerBuildInfo.setBuildType("Support");
			} else {
				playerBuildInfo.setPlayerBuild("Power " + profession);
				playerBuildInfo.setBuildType("Power");
			}
		}
		
		return playerBuildInfo;
	}
}
