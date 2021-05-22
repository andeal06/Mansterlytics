package com.alec.game.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class genBuffData {
	private double generation;

	public double getGeneration() {
		return generation;
	}

	public void setGeneration(double generation) {
		this.generation = generation;
	}

	
	
}
