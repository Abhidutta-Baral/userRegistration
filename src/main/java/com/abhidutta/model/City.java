package com.abhidutta.model;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class City {
	private String cityId;
	private String stateId;
	private String cityName;
}
