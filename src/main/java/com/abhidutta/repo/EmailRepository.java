package com.abhidutta.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhidutta.model.EmailDetails;

public interface EmailRepository extends JpaRepository<EmailDetails, Integer>{

}
