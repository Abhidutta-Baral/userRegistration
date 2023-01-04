package com.abhidutta.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abhidutta.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	//boolean existsUserByEmail(String email);

	boolean existsUserByEmail(String email);
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email, String password);
}
