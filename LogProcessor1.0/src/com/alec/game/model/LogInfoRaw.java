package com.alec.game.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"fightName", "recordedBy", "uploadLinks", "timeStartStd", "timeEndStd", "cm", "players"})
public class LogInfoRaw {

	// Declarations
	private String fightName; 
	private String recordedBy;
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd hh:mm:ss")
	private String timeStartStd;
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd hh:mm:ss")
	private String timeEndStd;
	private String duration; 
	@JsonProperty("isCM")
	private boolean cm;
	private List<Players> Players;
	private List<String> uploadLinks;	
	private Map<String, BuffInfo> buffMap;
	private Map<String, DamageModInfo> damageModMap;
	
	
	// Getters and Setters 
	public String getFightName() {
		return fightName;
	}
	public void setFightName(String fightName) {
		this.fightName = fightName;
	}
	public String getRecordedBy() {
		return recordedBy;
	}
	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
	}
	public String getTimeStartStd() {
		return timeStartStd;
	}
	public void setTimeStartStd(String timeStartStd) {
		this.timeStartStd = timeStartStd;
	}
	public String getTimeEndStd() {
		return timeEndStd;
	}
	public void setTimeEndStd(String timeEndStd) {
		this.timeEndStd = timeEndStd;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public boolean isCm() {
		return cm;
	}
	public void setCm(boolean cm) {
		this.cm = cm;
	}
	public List<Players> getPlayers() {
		return Players;
	}
	public void setPlayers(List<Players> players) {
		Players = players;
	}
	public List<String> getUploadLinks() {
		return uploadLinks;
	}
	public void setUploadLinks(List<String> uploadLinks) {
		this.uploadLinks = uploadLinks;
	}
	public Map<String, BuffInfo> getBuffMap() {
		return buffMap;
	}
	public void setBuffMap(Map<String, BuffInfo> buffMap) {
		this.buffMap = buffMap;
	}
	public Map<String, DamageModInfo> getDamageModMap() {
		return damageModMap;
	}
	public void setDamageModMap(Map<String, DamageModInfo> damageModMap) {
		this.damageModMap = damageModMap;
	}

}	
