package com.abhidutta.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
	@Id
	@GeneratedValue
	private Integer userId;
	@Column(unique = true, nullable = false)
	private String email;
	private String firstName;
	private String lastName;
	private Long phone;
	private LocalDate dob;
	private String gender;
	private String country;
	private String state;
	private String city;
	private boolean isLocked;
	private String password;
}
