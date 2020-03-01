package com.luccascalderaro.helpdesk.api.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.luccascalderaro.helpdesk.api.enums.StatusEnum;

@Document
public class ChangeStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@DBRef
	private Ticket ticket;
	
	@DBRef
	private User userChange;
	
	private Date dateChangeStatus;
	
	private StatusEnum status;
	
	public ChangeStatus() {
		
	}
	
	
	public ChangeStatus(String id, Ticket ticket, User userChange, Date dateChangeStatus, StatusEnum status) {
		super();
		this.id = id;
		this.ticket = ticket;
		this.userChange = userChange;
		this.dateChangeStatus = dateChangeStatus;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getUserChange() {
		return userChange;
	}

	public void setUserChange(User userChange) {
		this.userChange = userChange;
	}

	public Date getDateChangeStatus() {
		return dateChangeStatus;
	}

	public void setDateChangeStatus(Date dateChangeStatus) {
		this.dateChangeStatus = dateChangeStatus;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	
	
}
