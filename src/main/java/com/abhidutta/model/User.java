package com.abhidutta.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class User {
	private String firstName;
	private String lastName;
	private String email;
	private Long phone;
	private LocalDate dob;
	private String gender;
	private String country;
	private String state;
	private String city;
	private boolean isLocked;
	private String password;
	private String newPassword;
	private String confirmPassword;
}
