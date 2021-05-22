package com.alec.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DamageModInfo {
	private String name;
	private String icon;
	private String description;
	private boolean nonMultiplier;
	private boolean skillBased;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isNonMultiplier() {
		return nonMultiplier;
	}
	public void setNonMultiplier(boolean nonMultiplier) {
		this.nonMultiplier = nonMultiplier;
	}
	public boolean isSkillBased() {
		return skillBased;
	}
	public void setSkillBased(boolean skillBased) {
		this.skillBased = skillBased;
	}
	
}
