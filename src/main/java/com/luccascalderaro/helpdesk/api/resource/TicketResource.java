package com.luccascalderaro.helpdesk.api.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luccascalderaro.helpdesk.api.entity.ChangeStatus;
import com.luccascalderaro.helpdesk.api.entity.Ticket;
import com.luccascalderaro.helpdesk.api.entity.User;
import com.luccascalderaro.helpdesk.api.enums.ProfileEnum;
import com.luccascalderaro.helpdesk.api.enums.StatusEnum;
import com.luccascalderaro.helpdesk.api.response.Response;
import com.luccascalderaro.helpdesk.api.security.UserSS;
import com.luccascalderaro.helpdesk.api.service.TicketService;
import com.luccascalderaro.helpdesk.api.service.UserService;
import com.luccascalderaro.helpdesk.api.service.UserServiceInterface;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketResource {

	@Autowired
	private TicketService ticketService;

	@Autowired
	protected UserService userLogado;

	@Autowired
	private UserServiceInterface userService;

	@PostMapping()
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> createOrUpdate(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();

		try {

			validateCreateTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			ticket.setStatus(StatusEnum.getStatus("New"));
			ticket.setUser(userFromRequest(request));
			ticket.setDate(new Date());
			ticket.setNumber(generateNumber());
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);

		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);

		}
		return ResponseEntity.ok(response);
	}

	private void validateCreateTicket(Ticket ticket, BindingResult result) {

		if (ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Titulo não informado"));
			return;
		}

	}

	public User userFromRequest(HttpServletRequest request) {

		UserSS uss = UserService.authenticated();

		return userService.findById(uss.getId());

	}

	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}

	@PutMapping()
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();

		try {
			validateUpdateTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}

			Ticket ticketCurrent = ticketService.findById(ticket.getId());
			ticket.setStatus(ticketCurrent.getStatus());
			ticket.setUser(ticketCurrent.getUser());
			ticket.setDate(ticketCurrent.getDate());
			ticket.setNumber(ticketCurrent.getNumber());
			if (ticketCurrent.getAssignedUser() != null) {
				ticket.setAssignedUser(ticketCurrent.getAssignedUser());
			}
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
		} catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);

		}
		return ResponseEntity.ok(response);
	}

	private void validateUpdateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getId() == null) {
			result.addError(new ObjectError("Ticket", "Id não informado"));
			return;
		}

		if (ticket.getTitle() == null) {
			result.addError(new ObjectError("Ticket", "Titulo não informado"));
			return;
		}

	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id) {
		Response<Ticket> response = new Response<Ticket>();
		Ticket ticket = ticketService.findById(id);
		if (ticket == null) {
			response.getErrors().add("Registro não encontrado pelo id: " + id);
			return ResponseEntity.badRequest().body(response);
		}

		List<ChangeStatus> changes = new ArrayList<ChangeStatus>();
		Iterable<ChangeStatus> changesCurrent = ticketService.listChangeStatus(ticket.getId());
		for (Iterator<ChangeStatus> iterator = changesCurrent.iterator(); iterator.hasNext();) {
			ChangeStatus changeStatus = (ChangeStatus) iterator.next();
			changeStatus.setTicket(null);
			changes.add(changeStatus);

		}
		ticket.setChanges(changes);
		response.setData(ticket);
		return ResponseEntity.ok(response);

	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Ticket ticket = ticketService.findById(id);
		if (ticket == null) {
			response.getErrors().add("Registro não encontrado pelo id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		ticketService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	@GetMapping(value = "/{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity <Response<Page<Ticket>>> findAll(HttpServletRequest request ,@PathVariable("page") int page, @PathVariable("count") int count ) {
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket>  tickets = null;
		User userRequest = userFromRequest(request);
		
		
		if(userRequest.getProfile().contains(ProfileEnum.ROLE_TECHNICIAN)) {
			tickets = ticketService.listTicket(page, count);
		}	else if(userRequest.getProfile().contains(ProfileEnum.ROLE_CUSTOMER)) {
			tickets = ticketService.findByCurrentUser(page, count, userRequest.getId());
		}
		
		response.setData(tickets);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/{page}/{count}/{number}/{title}/{status}/{priority}/{assigned}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity <Response<Page<Ticket>>> findByParams(HttpServletRequest request ,
			@PathVariable("page") int page,
			@PathVariable("count") int count,
			@PathVariable("count") Integer number,
			@PathVariable("count") String title,
			@PathVariable("count") String status,
			@PathVariable("count") String priority,
			@PathVariable("count") boolean assigned) {
		
		title = title.equals("uninformed") ? "" : title;
		status = status.equals("uninformed") ? "" : status;
		priority = priority.equals("uninformed") ? "" : priority;
		
		Response<Page<Ticket>> response = new Response<Page<Ticket>>();
		Page<Ticket> tickets = null;
		if(number > 0) {
			tickets = ticketService.findByNumber(page, count, number);
		}else {
			User userRequest = userFromRequest(request);
			if(userRequest.getProfile().contains(ProfileEnum.ROLE_TECHNICIAN)){
				if(assigned) {
					tickets = ticketService.findByParameterAndAssignedUser(page, count, title, status, priority, userRequest.getId());
				} else {
					tickets = ticketService.findByParameters(page, count, title, status, priority);
				}
			}else if (userRequest.getProfile().contains(ProfileEnum.ROLE_CUSTOMER)) {
				tickets = ticketService.findByParametersAndCurrent(page, count, title, status, priority, userRequest.getId());
			}
		}
		response.setData(tickets);
		return ResponseEntity.ok(response);
	}
		
	

}
