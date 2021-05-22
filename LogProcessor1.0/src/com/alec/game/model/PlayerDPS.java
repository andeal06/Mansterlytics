package com.alec.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDPS {
	private int	dps; 
	private int condiDps;
	private int powerDps;
	private int breakbarDamage;
	
	
	public int getDps() {
		return dps;
	}
	public void setDps(int dps) {
		this.dps = dps;
	}
	public int getCondiDps() {
		return condiDps;
	}
	public void setCondiDps(int condiDps) {
		this.condiDps = condiDps;
	}
	public int getPowerDps() {
		return powerDps;
	}
	public void setPowerDps(int powerDps) {
		this.powerDps = powerDps;
	}
	public int getBreakbarDamage() {
		return breakbarDamage;
	}
	public void setBreakbarDamage(int breakbarDamage) {
		this.breakbarDamage = breakbarDamage;
	}
	
	
}
