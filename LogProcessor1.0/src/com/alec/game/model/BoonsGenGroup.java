package com.alec.game.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoonsGenGroup {
	private long id;
	private List<genBuffData> buffData;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<genBuffData> getBuffData() {
		return buffData;
	}
	public void setBuffData(List<genBuffData> buffData) {
		this.buffData = buffData;
	}
	
	
}
