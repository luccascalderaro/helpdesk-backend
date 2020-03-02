package com.luccascalderaro.helpdesk.api.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luccascalderaro.helpdesk.api.dto.Summary;
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
import com.luccascalderaro.helpdesk.api.service.impl.TicketServiceImpl;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins = "*")
public class TicketResource {

	@Autowired
	private TicketServiceImpl ticketService;

	@Autowired
	protected UserService userLogado;

	@PostMapping()
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Ticket> insert(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result) {

		ticket.setId(null);
		ticketService.insert(ticket);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ticket.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@PutMapping()
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Ticket> update(HttpServletRequest request, @RequestBody Ticket ticket, BindingResult result) {

		ticketService.update(ticket);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Ticket> findById(@PathVariable("id") String id) {

		Ticket ticket = ticketService.findById(id);

		return ResponseEntity.ok().body(ticket);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('CUSTOMER')")
	public ResponseEntity<Void> delete(@PathVariable("id") String id) {
		ticketService.delete(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{page}/{count}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Page<Ticket>> findAll(HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("count") int count) {
		
		Page<Ticket> pages = this.ticketService.findAllParams(page, count);
		
		return ResponseEntity.ok().body(pages);

	}


	@GetMapping(value = "/{page}/{count}/{number}/{title}/{status}/{priority}/{assisgned}")
	@PreAuthorize("hasAnyRole('CUSTOMER', 'TECHNICIAN')")
	public ResponseEntity<Page<Ticket>> findByParams(HttpServletRequest request,
			@PathVariable("page") int page, @PathVariable("count") int count, @PathVariable("number") Integer number,
			@PathVariable("title") String title, @PathVariable("status") String status,
			@PathVariable("priority") String priority, @PathVariable("assigned") boolean assigned) {

		title = title.equals("uninformed") ? "" : title;
		status = status.equals("uninformed") ? "" : status;
		priority = priority.equals("uninformed") ? "" : priority;

		Page<Ticket> tickets = null;
		if (number > 0) {
			tickets = ticketService.findByNumber(page, count, number);
			
		} else {
			User userRequest = ticketService.userFromRequest(request);
			if (userRequest.getProfile().contains(ProfileEnum.ROLE_TECHNICIAN)) {
				if (assigned) {
					tickets = ticketService.findByParameterAndAssignedUser(page, count, title, status, priority,
							userRequest.getId());
				} else {
					tickets = ticketService.findByParameters(page, count, title, status, priority);
				}
			} else if (userRequest.getProfile().contains(ProfileEnum.ROLE_CUSTOMER)) {
				tickets = ticketService.findByParametersAndCurrent(page, count, title, status, priority,
						userRequest.getId());
			}
		}
		return ResponseEntity.ok().body(tickets);
	}

	@PutMapping(value = "/{id}/status")
	@PreAuthorize("hasAnyRole('CUSTOMER','TECHNICIAN')")
	public ResponseEntity<Ticket> changeStatus(
													@PathVariable("id") String id,
													@Valid @RequestBody Ticket ticket,
													HttpServletRequest request,
													BindingResult result) {
		ticketService.updateStatusTicket(id, ticket.getStatus());	
		return ResponseEntity.noContent().build();			
	}

	@GetMapping(value = "/summary")
	public ResponseEntity<Response<Summary>> findChart() {
		Response<Summary> response = new Response<Summary>();
		Summary chart = new Summary();
		int amountNew = 0;
		int amountResolved = 0;
		int amountApproved = 0;
		int amountDisapproved = 0;
		int amountAssigned = 0;
		int amountClosed = 0;
		Iterable<Ticket> tickets = ticketService.findAll();
		if (tickets != null) {
			for (Iterator<Ticket> iterator = tickets.iterator(); iterator.hasNext();) {
				Ticket ticket = iterator.next();
				if (ticket.getStatus().equals(StatusEnum.New)) {
					amountNew++;
				}
				if (ticket.getStatus().equals(StatusEnum.Resolved)) {
					amountResolved++;
				}
				if (ticket.getStatus().equals(StatusEnum.Approved)) {
					amountApproved++;
				}
				if (ticket.getStatus().equals(StatusEnum.Disapproved)) {
					amountDisapproved++;
				}
				if (ticket.getStatus().equals(StatusEnum.Assigned)) {
					amountAssigned++;
				}
				if (ticket.getStatus().equals(StatusEnum.Closed)) {
					amountClosed++;
				}
			}
		}
		chart.setAmountNew(amountNew);
		chart.setAmountResolved(amountResolved);
		chart.setAmountApproved(amountApproved);
		chart.setAmountDisapproved(amountDisapproved);
		chart.setAmountAssigned(amountAssigned);
		chart.setAmountClosed(amountClosed);
		response.setData(chart);
		return ResponseEntity.ok(response);
	}

}
