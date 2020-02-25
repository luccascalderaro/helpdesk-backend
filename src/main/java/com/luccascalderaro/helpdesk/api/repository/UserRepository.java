package com.luccascalderaro.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.luccascalderaro.helpdesk.api.entity.User;


public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
	
	
}
