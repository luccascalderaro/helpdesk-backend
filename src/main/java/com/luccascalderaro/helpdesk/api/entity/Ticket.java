package com.luccascalderaro.helpdesk.api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.luccascalderaro.helpdesk.api.enums.PriorityEnum;
import com.luccascalderaro.helpdesk.api.enums.StatusEnum;

@Document
public class Ticket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@DBRef(lazy = true)
	private User user;
	
	private Date date;
	
	private String title;
	
	private Integer number;
	
	private StatusEnum status;
	
	private PriorityEnum priority;
	
	@DBRef(lazy = true)
	private User assignedUser;
	
	private String description;
	
	private String image;
	
	@Transient
	private List<ChangeStatus> changes;
	
	public Ticket() {
		
	}
	
	public Ticket(String id, User user, Date date, String title, Integer number, StatusEnum status,
			PriorityEnum priority, User assignedUser, String description, String image) {
		super();
		this.id = id;
		this.user = user;
		this.date = date;
		this.title = title;
		this.number = number;
		this.status = status;
		this.priority = priority;
		this.assignedUser = assignedUser;
		this.description = description;
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public PriorityEnum getPriority() {
		return priority;
	}

	public void setPriority(PriorityEnum priority) {
		this.priority = priority;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assianedUser) {
		this.assignedUser = assianedUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<ChangeStatus> getChanges() {
		return changes;
	}

	public void setChanges(List<ChangeStatus> changes) {
		this.changes = changes;
	}
	
	
	
	

}
