package com.luccascalderaro.helpdesk.api.resource;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luccascalderaro.helpdesk.api.entity.User;
import com.luccascalderaro.helpdesk.api.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserResource {

	@Autowired
	private UserServiceImpl userService;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> create(HttpServletRequest request, @RequestBody User user, BindingResult result) {
		userService.insert(user);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> update(HttpServletRequest request, @RequestBody User user, BindingResult result) {

		userService.update(user);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<User> findById(@PathVariable("id") String id) {
		
		User us = userService.findById(id);
		return ResponseEntity.ok().body(us);

	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		
		userService.delete(id);
		
		return ResponseEntity.noContent().build();
	
	}
	
	@GetMapping(value = "/{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Page<User>> finAll(@PathVariable int page, @PathVariable int count) {
		
		Page<User> pg = userService.findAll(page, count);
		
		return ResponseEntity.ok().body(pg);
	
}

}
