package com.abhidutta.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhidutta.model.City;

public interface CityRepository extends JpaRepository<City, Integer> {
	List<City> findByStateId(String stateId);
}
