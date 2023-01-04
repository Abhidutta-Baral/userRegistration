package com.abhidutta.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhidutta.model.State;

public interface StateRepository extends JpaRepository<State, Integer> {
	List<State> findByCountryId(String countryId);

}
