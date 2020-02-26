package com.luccascalderaro.helpdesk.api.service;

import org.springframework.security.core.context.SecurityContextHolder;

import com.luccascalderaro.helpdesk.api.security.UserSS;

public class UserService {

	public static UserSS authenticated() {
		// Retorna o usuario logado no sistema
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
