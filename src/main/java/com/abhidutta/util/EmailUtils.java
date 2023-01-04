package com.abhidutta.util;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.abhidutta.model.EmailDetails;
import com.abhidutta.repo.EmailRepository;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private EmailRepository emailRepository;

	// To send a simple email
	public String sendSimpleMail(EmailDetails emailDetails) {
		// SimpleMailMessage mailMessage = new SimpleMailMessage();
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;

		try {

			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setTo(emailDetails.getToDetails());
			mimeMessageHelper.setText(emailDetails.getMsgBody(),true);
			mimeMessageHelper.setSubject(emailDetails.getSubject());
			emailRepository.save(emailDetails);
			javaMailSender.send(mimeMessage);

			return "Success";
		}

		catch (Exception e) {
			return "Failed";
		}
	}

}
