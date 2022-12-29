package com.abhidutta.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.abhidutta.dto.LoginForm;
import com.abhidutta.dto.UnlockAccForm;
import com.abhidutta.model.City;
import com.abhidutta.model.Country;
import com.abhidutta.model.EmailDetails;
import com.abhidutta.model.State;
import com.abhidutta.model.User;
import com.abhidutta.repo.CityRepository;
import com.abhidutta.repo.CountryRepository;
import com.abhidutta.repo.EmailRepository;
import com.abhidutta.repo.StateRepository;
import com.abhidutta.repo.UserRepository;
import com.abhidutta.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private CityRepository cityRepository;
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public boolean isEmailExist(String email) {
		return userRepository.existsUserByEmail(email);
	}

	@Override
	public Map<Integer, String> getCountries() {
		// Map<Integer, String> countriesMap = new HashMap<>();
		List<Country> countriesList = countryRepository.findAll();
		/*
		 * for (Country c : countriesList) { countriesMap.put(c.getCountryId(),
		 * c.getCountryName()); }
		 */
		Map<Integer, String> countriesMap = countriesList.stream()
				.collect(Collectors.toMap(Country::getCountryId, Country::getCountryName));
		return countriesMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		// Map<Integer, String> statesMap = new HashMap<>();
		List<State> statesList = stateRepository.findByCountryId(countryId);
		/*
		 * for (State s : statesList) { statesMap.put(s.getStateId(), s.getStateName());
		 * }
		 */

		Map<Integer, String> statesMap = statesList.stream()
				.collect(Collectors.toMap(State::getStateId, State::getStateName));
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		// Map<Integer, String> citiesMap = new HashMap<>();
		List<City> citiesList = cityRepository.findByStateId(stateId);

		/*
		 * for (City ct : citiesList) { citiesMap.put(ct.getCityId(), ct.getCityName());
		 * }
		 */
		Map<Integer, String> citiesMap = citiesList.stream()
				.collect(Collectors.toMap(City::getCityId, City::getCityName));
		return citiesMap;
	}

	@Override
	public String registerUser(User user) {
		String registerMsg = "";
		user.setPassword(generateRandomPassword());
		user.setLocked(true);
		User userData = userRepository.save(user);
		if (userData.getUserId() != null) {
			EmailDetails emailDetails = new EmailDetails();
			emailDetails.setFromDetails(sender);
			String msgBody = "Your Temp Password is-" + userData.getPassword();
			emailDetails.setMsgBody(msgBody);
			emailDetails.setSubject("Unlock IES Account");
			emailDetails.setToDetails(userData.getEmail());
			emailRepository.save(emailDetails);
			String sendSimpleMail = sendSimpleMail(emailDetails);
			if (sendSimpleMail.equals("Success")) {
				registerMsg = "Please check your email to unlock account";
			}
		}
		return registerMsg;
	}

	@Override
	public String unlockAccount(UnlockAccForm accForm) {
		String unlockAccountMsg = "";
		String tempPass = accForm.getTempPass();
		String email = accForm.getEmail();
		String newPass = accForm.getNewPass();
		Optional<User> userDetails = userRepository.findByEmail(email);
		User user = userDetails.get();

		if (userDetails.isPresent()) {
			if (user.getPassword().equals(tempPass)) {
				user.setPassword(newPass);
				user.setLocked(false);
				;
				userRepository.save(user);
				unlockAccountMsg = "Account unlocked, please proceed with login";
			} else {
				unlockAccountMsg = "Invalid Credentials";
			}
		} else {
			unlockAccountMsg = "Invalid Credentials";
		}

		return unlockAccountMsg;
	}

	@Override
	public String login(LoginForm loginForm) {
		String logInMsg = "";
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();

		Optional<User> userDetails = userRepository.findByEmail(email);

		if (userDetails.isPresent()) {
			User user = userDetails.get();
			if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
				if (!user.isLocked()) {
					logInMsg = "Valid Credentials";
				}
			} else {
				logInMsg = "‘Your Account Is Locked";
			}

		} else {
			logInMsg = "Invalid Credentials";

		}

		return logInMsg;
	}

	@Override
	public String forgotPwd(String email) {
		String forgotPwdMsg = "";
		if (isEmailExist(email)) {
			Optional<User> userDetails = userRepository.findByEmail(email);
			User user = userDetails.get();

			if (userDetails.isPresent()) {
				EmailDetails emailDetails = new EmailDetails();
				emailDetails.setFromDetails(sender);
				String msgBody = "Your Password is-" + user.getPassword();
				emailDetails.setMsgBody(msgBody);
				emailDetails.setSubject("Password of IES Account");
				emailDetails.setToDetails(email);
				emailRepository.save(emailDetails);
				String sendSimpleMail = sendSimpleMail(emailDetails);
				if (sendSimpleMail.equals("Success")) {
					forgotPwdMsg = "Please check your email reset Password";
				}
			}

		} else {
			forgotPwdMsg = "This email is not register";
		}
		return forgotPwdMsg;
	}

	// GenerateRandom Alphanumeric String
	public String generateRandomPassword() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

	// To send a simple email
	public String sendSimpleMail(EmailDetails emailDetails) {

		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setFrom(sender);
			mailMessage.setTo(emailDetails.getToDetails());
			mailMessage.setText(emailDetails.getMsgBody());
			mailMessage.setSubject(emailDetails.getSubject());

			javaMailSender.send(mailMessage);
			return "Success";
		}

		catch (Exception e) {
			return "Failed";
		}
	}

}
