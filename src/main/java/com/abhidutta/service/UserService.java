package com.abhidutta.service;

import java.util.Map;

import com.abhidutta.dto.LoginForm;
import com.abhidutta.dto.UnlockAccForm;
import com.abhidutta.model.User;

public interface UserService {

	public boolean isEmailExist(String email);

	public Map<Integer, String> getCountries();

	public Map<Integer, String> getStates(Integer countryId);

	public Map<Integer, String> getCities(Integer stateId);

	public String registerUser(User user);

	public String unlockAccount(UnlockAccForm accForm);

	public String login(LoginForm loginForm);

	public String forgotPwd(String email);

}
