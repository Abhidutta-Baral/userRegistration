package com.abhidutta.service;

import com.abhidutta.model.EmailDetails;
import com.abhidutta.model.User;

public interface EmailService {
	public String sendUnlockAccountEmail(EmailDetails emailDetails, User user);

	public String sendForgotPasswordEmail(EmailDetails emailDetails, User user);

}
