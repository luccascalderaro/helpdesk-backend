package com.luccascalderaro.helpdesk.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luccascalderaro.helpdesk.api.entity.User;
import com.luccascalderaro.helpdesk.api.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class UserResource {
	
	
	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping()
	public ResponseEntity<Page<User>> findAll() {
		
		Page<User> list = userService.findAll(1, 2);
		
		return ResponseEntity.ok().body(list);
		
	}

}
