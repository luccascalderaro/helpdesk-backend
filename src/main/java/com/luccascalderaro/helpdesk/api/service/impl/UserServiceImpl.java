package com.luccascalderaro.helpdesk.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luccascalderaro.helpdesk.api.entity.User;
import com.luccascalderaro.helpdesk.api.exception.ObjectNotFoundException;
import com.luccascalderaro.helpdesk.api.repository.UserRepository;
import com.luccascalderaro.helpdesk.api.service.UserServiceInterface;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;

@Service
public class UserServiceImpl implements UserServiceInterface {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder pe;

	@Override
	public User findByEmail(String email) {
		
		return this.userRepository.findByEmail(email);
	}

	@Override
	public User insert(User user) {
		try {
			User us1 = new User(null, user.getEmail(), pe.encode(user.getPassword()));
			us1.setProfile(user.getProfile());
		return this.userRepository.save(us1);
		}
		
		catch(MongoWriteException e) {
		 throw new com.luccascalderaro.helpdesk.api.exception.MongoWriteException("Email ja cadastrado");
		}
	}
	
	public void updateAux(User usNew,User user) {
		
		usNew.setEmail(user.getEmail());
		usNew.setId(user.getId());
		usNew.setPassword(user.getPassword());
		usNew.setProfile(user.getProfile());
	}
	
	
	public User update(User user) {
		
		User newObj = findByEmail(user.getEmail());
		
		updateAux(newObj, user);
		
		String id = findByEmail(user.getEmail()).getId();
				
		newObj.setId(id);
		
		
		return userRepository.save(newObj);
		
	}
	
	

	@Override
	public User findById(String id) {
		Optional<User> user = this.userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException("Objeto nao encontrado") );
	}

	@Override
	public void delete(String id) {
		
		this.userRepository.deleteById(id);
		
	}

	@Override
	public Page<User> findAll(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.userRepository.findAll(pages);
	}

}
