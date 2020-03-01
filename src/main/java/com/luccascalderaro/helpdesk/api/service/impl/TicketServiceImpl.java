package com.luccascalderaro.helpdesk.api.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luccascalderaro.helpdesk.api.entity.ChangeStatus;
import com.luccascalderaro.helpdesk.api.entity.Ticket;
import com.luccascalderaro.helpdesk.api.entity.User;
import com.luccascalderaro.helpdesk.api.enums.StatusEnum;
import com.luccascalderaro.helpdesk.api.exception.ObjectNotFoundException;
import com.luccascalderaro.helpdesk.api.repository.ChangeStatusRepository;
import com.luccascalderaro.helpdesk.api.repository.TicketRepository;
import com.luccascalderaro.helpdesk.api.security.UserSS;
import com.luccascalderaro.helpdesk.api.service.TicketService;
import com.luccascalderaro.helpdesk.api.service.UserService;
import com.luccascalderaro.helpdesk.api.service.UserServiceInterface;

@Service
public class TicketServiceImpl implements TicketService  {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private ChangeStatusRepository changeStatusRepository;
	
	@Autowired
	private UserServiceInterface userService;

	@Override
	public Ticket insert(Ticket ticket) {
		
		ticket.setStatus(StatusEnum.New);
		Date date = new Date(System.currentTimeMillis());
		ticket.setDate(date);
		ticket.setNumber(generateNumber());
		
		
		return this.ticketRepository.save(ticket);
	}
	
	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}
	
	
	public void updateAux(Ticket newObj, Ticket obj) {
		
		if(obj.getAssignedUser() != null) {
			newObj.setAssignedUser(obj.getAssignedUser());
		}
		
		if(obj.getChanges() != null) {
			newObj.setChanges(obj.getChanges());
		}
		
		if(obj.getDate() != null) {
			newObj.setDate(obj.getDate());
		}
		
		if(obj.getDescription() != null) {
			newObj.setDescription(obj.getDescription());
		}
		
		if(obj.getNumber() != null) {
			newObj.setNumber(obj.getNumber());
		}
		
		if(obj.getUser() != null) {
			newObj.setUser(obj.getUser());
		}
		
		if(obj.getId() != null) {
			newObj.setId(obj.getId());
		}
		
		if(obj.getImage() != null) {
			newObj.setImage(obj.getImage());
		}
		
		if(obj.getPriority() != null) {
			newObj.setPriority(obj.getPriority());
		}
		
		if(obj.getStatus() != null) {
			newObj.setStatus(obj.getStatus());
		}
		
		if(obj.getTitle() != null) {
			newObj.setTitle(obj.getTitle());
		}
		
		
	}
	
	public Ticket update(Ticket ticket) {
		
		Ticket ticketNew = findById(ticket.getId());
		
		updateAux(ticketNew, ticket);
		
		return ticketRepository.save(ticketNew);
		
	}
	
	
	
	public User userFromRequest(HttpServletRequest request) {

		UserSS uss = UserService.authenticated();

		return userService.findById(uss.getId());

	}

	@Override
	public Ticket findById(String id) {
		
		Optional<Ticket> ticket = this.ticketRepository.findById(id);
		
		return ticket.orElseThrow(() -> new ObjectNotFoundException("Nao encontrado"));
	}

	@Override
	public void delete(String id) {
		this.ticketRepository.deleteById(id);
		
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		
		return this.ticketRepository.findAll(pages);
	}

	@Override
	public ChangeStatus createChangeStatus(ChangeStatus changeStatus) {
		return this.changeStatusRepository.save(changeStatus);
		
	}

	@Override
	public Iterable<ChangeStatus> listChangeStatus(String ticketId) {
		
		return this.changeStatusRepository.findByTicketIdOrderByDateChangeStatusDesc(ticketId);
	}

	@Override
	public Page<Ticket> findByCurrentUser(int page, int count, String userId) {
		Pageable pages = PageRequest.of(page, count);
		
		return this.ticketRepository.findByUserIdOrderByDateDesc(pages, userId);
	}

	@Override
	public Page<Ticket> findByParameters(int page, int count, String title, String status, String priority) {
		Pageable pages = PageRequest.of(page, count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingOrderByDateDesc(title, status, priority, pages);
	}

	@Override
	public Page<Ticket> findByParametersAndCurrent(int page, int count, String title, String status, String priority,
			String userId) {
		Pageable pages = PageRequest.of(page, count);
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndUserIdOrderByDateDesc(title, status, priority, userId, pages);
	}

	@Override
	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		Pageable pages = PageRequest.of(page, count);
		return this.ticketRepository.findByNumber(number, pages);
	}

	@Override
	public Iterable<Ticket> findAll() {
	
		return this.ticketRepository.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndAssignedUser(int page, int count, String title, String status,
			String priority, String assignedUser) {
		Pageable pages = PageRequest.of(page, count);
		
		return this.ticketRepository.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPriorityIgnoreCaseContainingAndAssignedUserIdOrderByDateDesc(title, status, priority, assignedUser, pages);
	}

}
