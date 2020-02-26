package com.luccascalderaro.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.luccascalderaro.helpdesk.api.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
	
	
}
