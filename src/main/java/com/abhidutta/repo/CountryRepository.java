package com.abhidutta.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhidutta.model.Country;

public interface CountryRepository extends JpaRepository<Country, Integer> {

}
