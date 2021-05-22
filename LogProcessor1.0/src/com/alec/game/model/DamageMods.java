package com.alec.game.model;

import java.util.List;

public class DamageMods {
	private String id;
	private List<CharacterModInfo> damageModifiers;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<CharacterModInfo> getDamageModifiers() {
		return damageModifiers;
	}
	public void setDamageModifiers(List<CharacterModInfo> damageModifiers) {
		this.damageModifiers = damageModifiers;
	}
	
	
}
