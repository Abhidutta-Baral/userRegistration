package com.abhidutta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class State {
	@Id
	@GeneratedValue
	private Integer stateId;
	private String countryId;
	private String stateName;
}
