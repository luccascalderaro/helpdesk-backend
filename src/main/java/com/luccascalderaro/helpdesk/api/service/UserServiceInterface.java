package com.luccascalderaro.helpdesk.api.service;

import org.springframework.data.domain.Page;

import com.luccascalderaro.helpdesk.api.entity.User;

public interface UserServiceInterface {
	
	User findByEmail(String email);
	
	User insert(User user);
	
	User findById(String id);
	
	void delete(String id);
	
	Page<User> findAll(int page, int count);
	

}
