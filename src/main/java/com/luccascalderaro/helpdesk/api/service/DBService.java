package com.luccascalderaro.helpdesk.api.service;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.luccascalderaro.helpdesk.api.entity.User;
import com.luccascalderaro.helpdesk.api.enums.ProfileEnum;
import com.luccascalderaro.helpdesk.api.repository.UserRepository;

@Service
public class DBService {

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private UserRepository userRepository;

	public void instantiateDatabase() throws ParseException {


		User us2 = new User(null, "luccashmc2@hotmail.com", pe.encode("123"));

		Set<ProfileEnum> penum2 = new HashSet<>();
		penum2.add(ProfileEnum.ROLE_ADMIN);

		us2.setProfile(penum2);

		userRepository.save(us2);

		User us3 = new User(null, "luccashmc3@hotmail.com", pe.encode("123"));

		Set<ProfileEnum> penum3 = new HashSet<>();
		penum3.add(ProfileEnum.ROLE_CUSTOMER);

		us3.setProfile(penum3);

		userRepository.save(us3);

	}

}
