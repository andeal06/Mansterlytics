package com.alec.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

//uptime = %uptime if non stackable, stacks if boon is stackable
//presence = %uptime if stackable 0 if not stackable
public class BuffData {
	private double uptime;
	private double presence;

	
	public double getUptime() {
		return uptime;
	}
	public void setUptime(double uptime) {
		this.uptime = uptime;
	}
	public double getPresence() {
		return presence;
	}
	public void setPresence(double presence) {
		this.presence = presence;
	}
	
	
}
