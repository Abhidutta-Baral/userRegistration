package com.abhidutta.model;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class State {
	private String stateId;
	private String countryId;
	private String stateName;
}
