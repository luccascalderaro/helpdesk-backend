package com.luccascalderaro.helpdesk.api.entity;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.luccascalderaro.helpdesk.api.enums.ProfileEnum;

@Document
public class User {
	
	@Id
	private String id;
	
	@Indexed(unique = true)
	@NotBlank(message = "Email requerido")
	@Email(message= "Email invalido")
	private String email;
	
	@NotBlank(message = "Digite uma senha")
	@Size(min = 6,message = "A senha deve ter no minimo 6 digitos")
	private String password;
	
	private Set<ProfileEnum> profile;
	
	
	public User () {
		
	}

	public User(String id, @NotBlank(message = "Email requerido") @Email(message = "Email invalido") String email,
			@NotBlank(message = "Digite uma senha") @Size(min = 6, message = "A senha deve ter no minimo 6 digitos") String password) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Set<ProfileEnum> getProfile() {
		return profile;
	}



	public void setProfile(Set<ProfileEnum> profile) {
		this.profile = profile;
	}

	
	
	
	

}
