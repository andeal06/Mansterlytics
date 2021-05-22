package com.alec.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerStats {
	private int connectedDirectDamageCount;
	private int criticalRate;
	private int flankingRate;
	
	
	public int getconnectedDirectDamageCount() {
		return connectedDirectDamageCount;
	}
	public void setconnectedDirectDamageCount(int connectedDirectDamageCount) {
		this.connectedDirectDamageCount = connectedDirectDamageCount;
	}
	public int getcriticalRate() {
		return criticalRate;
	}
	public void setcriticalRate(int criticalRate) {
		this.criticalRate = criticalRate;
	}
	public int getflankingRate() {
		return flankingRate;
	}
	public void setflankingRate(int flankingRate) {
		this.flankingRate = flankingRate;
	}
}
