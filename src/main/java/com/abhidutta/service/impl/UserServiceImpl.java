package com.abhidutta.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

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
import com.abhidutta.util.EmailUtils;

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
	private EmailUtils emailUtils;

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public boolean isEmailExist(String email) {
		return userRepository.existsUserByEmail(email);
	}

	@Override
	public Map<Integer, String> getCountries() {
		List<Country> countriesList = countryRepository.findAll();
		Map<Integer, String> countriesMap = countriesList.stream()
				.collect(Collectors.toMap(Country::getCountryId, Country::getCountryName));
		return countriesMap;
	}

	@Override
	public Map<Integer, String> getStates(Integer countryId) {
		List<State> statesList = stateRepository.findByCountryId(String.valueOf(countryId));
		Map<Integer, String> statesMap = statesList.stream()
				.collect(Collectors.toMap(State::getStateId, State::getStateName));
		return statesMap;
	}

	@Override
	public Map<Integer, String> getCities(Integer stateId) {
		List<City> citiesList = cityRepository.findByStateId(String.valueOf(stateId));
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
			String body = readEmailBody("user_reg_email_format.txt", user);
			emailDetails.setMsgBody(body);
			emailDetails.setSubject("Unlock IES Account");
			emailDetails.setToDetails(userData.getEmail());
			String sendSimpleMail = emailUtils.sendSimpleMail(emailDetails);
			if (sendSimpleMail.equals("Success")) {
				registerMsg = "Please check your email to unlock account";
			}
		}
		return registerMsg;
	}

	@Override
	public String unlockAccount(UnlockAccForm accForm) {
		String tempPass = accForm.getTempPass();
		String email = accForm.getEmail();
		Optional<User> userDetails = userRepository.findByEmail(email);
		User user = userDetails.get();

		if (user != null && user.getPassword().equals(tempPass)) {
			user.setPassword(accForm.getNewPass());
			user.setLocked(false);
			userRepository.save(user);
			return "Account unlocked, please proceed with login";
		}

		return "Invalid Credentials";
	}

	@Override
	public String login(LoginForm loginForm) {
		String email = loginForm.getEmail();
		String password = loginForm.getPassword();

		Optional<User> userDetails = userRepository.findByEmailAndPassword(email, password);
		if (!userDetails.isPresent()) {
			return "Invalid Credentials";
		}
		if (userDetails.get().isLocked()) {
			return "Your Account Is Locked";
		}
		return "Valid Credentials";
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
				String body = readEmailBody("forgot_pwd_email_format.txt", user);
				emailDetails.setMsgBody(body);
				emailDetails.setSubject("Password of IES Account");
				emailDetails.setToDetails(email);
				String sendSimpleMail = emailUtils.sendSimpleMail(emailDetails);
				if (sendSimpleMail.equals("Success")) {
					forgotPwdMsg = "Please check your email to reset Password";
				}
			}

		} else {
			forgotPwdMsg = "This email is not register";
		}
		return forgotPwdMsg;
	}

	// GenerateRandom Alphanumeric String
	private String generateRandomPassword() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 6;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString;
	}

	private String readEmailBody(String fileName, User user) {
		StringBuffer sb = new StringBuffer();

		try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
			lines.forEach(line -> {
				line = line.replace("${fname}", user.getFirstName());
				line = line.replace("${lname}", user.getLastName());
				line = line.replace("${temp_pwd}", user.getPassword());
				line = line.replace("${email}", user.getEmail());
				line = line.replace("${pwd}", user.getPassword());
				sb.append(line);

			});
			lines.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

}
