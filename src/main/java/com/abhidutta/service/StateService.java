package com.abhidutta.service;

import java.util.List;

import com.abhidutta.model.State;

public interface StateService {
	public List<State> getStateByCountryId(String countryId);

	public List<State> getAllState();
}
