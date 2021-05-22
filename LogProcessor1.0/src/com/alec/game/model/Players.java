package com.alec.game.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Players {
	//Grabs all desired information about the players in a log 
	private String account;
	private String name;
	private String profession;
	private int condition;
	private int concentration;
	private int healing;
	private int toughness;
	private List<String> weapons;
	@JsonProperty("dpsTargets")
	private List<List<PlayerDPS>> playerDPS;
	@JsonProperty("statsTargets")
	private List<List<PlayerStats>> playerStats;
	private List<Buffs> buffUptimes; 
	private List<BoonsGenGroup> groupBuffs; 
	private List<DamageMods> damageModifiers;
	
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public int getConcentration() {
		return concentration;
	}
	public void setConcentration(int concentration) {
		this.concentration = concentration;
	}
	public int getHealing() {
		return healing;
	}
	public void setHealing(int healing) {
		this.healing = healing;
	}
	public int getToughness() {
		return toughness;
	}
	public void setToughness(int toughness) {
		this.toughness = toughness;
	}
	public List<List<PlayerDPS>> getPlayerDPS() {
		return playerDPS;
	}
	public void setPlayerDPS(List<List<PlayerDPS>> playerDPS) {
		this.playerDPS = playerDPS;
	}
	public List<List<PlayerStats>> getPlayerStats() {
		return playerStats;
	}
	public void setPlayerStats(List<List<PlayerStats>> playerStats) {
		this.playerStats = playerStats;
	}
	public List<String> getWeapons() {
		return weapons;
	}
	public void setWeapons(List<String> weapons) {
		this.weapons = weapons;
	}
	public List<Buffs> getBuffUptimes() {
		return buffUptimes;
	}
	public void setBuffUptimes(List<Buffs> buffUptimes) {
		this.buffUptimes = buffUptimes;
	}
	public List<DamageMods> getDamageModifiers() {
		return damageModifiers;
	}
	public void setDamageModifiers(List<DamageMods> damageModifiers) {
		this.damageModifiers = damageModifiers;
	}
	public List<BoonsGenGroup> getGroupBuffs() {
		return groupBuffs;
	}
	public void setGroupBuffs(List<BoonsGenGroup> groupBuffs) {
		this.groupBuffs = groupBuffs;
	}
	
}
