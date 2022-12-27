package com.abhidutta.service;

import com.abhidutta.model.User;

public interface UserService {
	public String signIn(User user);

	public String registerUser(User user);

	public String linkToUnlockAccount(String email);

	public String unlockAccount(User user);

	public String forgetPassword(String email);
}
