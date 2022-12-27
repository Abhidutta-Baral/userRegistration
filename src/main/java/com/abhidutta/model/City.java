package com.abhidutta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class City {
	@Id
	@GeneratedValue
	private Integer id;
	private String cityId;
	private String stateId;
	private String cityName;
}
