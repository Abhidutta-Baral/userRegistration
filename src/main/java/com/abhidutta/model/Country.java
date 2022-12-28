package com.abhidutta.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Country {
	@Id
	@GeneratedValue
	private Integer countryId;
	private String countryName;
}
