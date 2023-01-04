package com.abhidutta.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abhidutta.dto.LoginForm;
import com.abhidutta.dto.UnlockAccForm;
import com.abhidutta.model.User;
import com.abhidutta.service.UserService;

@RestController
public class UserController {
	@Autowired
	UserService userService;

	@GetMapping("/login")
	ResponseEntity<String> login(@RequestBody LoginForm loginForm) {
		String login = userService.login(loginForm);
		return new ResponseEntity<>(login, HttpStatus.OK);
	}

	@GetMapping("/countries")
	public Map<Integer, String> getCountries() {
		return userService.getCountries();
	}

	@GetMapping("/states/{countryId}")
	public Map<Integer, String> getStates(@PathVariable Integer countryId) {
		return userService.getStates(countryId);
	}

	@GetMapping("/cities/{stateId}")
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {
		return userService.getCities(stateId);
	}

	@GetMapping("/email/{email}")
	public boolean checkEmail(@PathVariable String email) {
		return userService.isEmailExist(email);
	}

	@PostMapping("/user")
	public ResponseEntity<String> userRegistration(@RequestBody User user) {
		String registerUser = userService.registerUser(user);
		return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
	}

	@PostMapping("/unlock")
	public ResponseEntity<String> unlockAccount(@RequestBody UnlockAccForm unlockAccForm) {
		String unlockAccount = userService.unlockAccount(unlockAccForm);
		return new ResponseEntity<>(unlockAccount, HttpStatus.OK);
	}

	@GetMapping("/forgetPwd/{email}")
	public ResponseEntity<String> forgetPwd(@PathVariable String email) {
		String forgotPwd = userService.forgotPwd(email);
		return new ResponseEntity<>(forgotPwd, HttpStatus.OK);
	}
}