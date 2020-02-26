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

		User us1 = new User(null, "luccashmc@hotmail.com", pe.encode("123"));
		
		Set<ProfileEnum> penum = new HashSet<>();
		penum.add(ProfileEnum.ROLE_ADMIN);
		
		us1.setProfile(penum);
		
		userRepository.save(us1);
		
	}

}
