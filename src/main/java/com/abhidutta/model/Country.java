package com.abhidutta.model;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Country {
	private String countryId;
	private String countryName;
}
