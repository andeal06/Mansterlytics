package com.alec.game.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Buffs {
	private long id;	
	private List<BuffData> buffData;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<BuffData> getBuffData() {
		return buffData;
	}
	public void setBuffData(List<BuffData> buffData) {
		this.buffData = buffData;
	}	
}
