package com.abhidutta.service;

import java.util.List;

import com.abhidutta.model.City;

public interface CityService {
	public List<City> getCityByStateId(String stateId);

	public List<City> getAllCity();
}
