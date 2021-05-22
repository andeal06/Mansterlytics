package com.alec.game.model;

import java.util.List;

// Comes from the BuffMap at end of file. This describes and names each buff ID
public class BuffInfo {
	private String name;
	private String icon;
	private Boolean stacking;
	private List<String> descriptions;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Boolean getStacking() {
		return stacking;
	}
	public void setStacking(Boolean stacking) {
		this.stacking = stacking;
	}
	public List<String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	
	
}
