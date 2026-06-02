package com.mphasis.javaproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mphasis.javaproject.model.Event;
import com.mphasis.javaproject.service.EventService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@PostMapping
	public ResponseEntity<Event> createEvent(@Valid @RequestBody Event eventrequest){
		
	
		Event responsEvent=eventService.createEvent(eventrequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responsEvent);
		
		
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<Event> getEventById(@PathVariable @NotBlank(message = "Event id should not blank")  String id){
		
		
		Event responsEvent=eventService.getEventById(id);
		
		return ResponseEntity.status(HttpStatus.OK).body(responsEvent);
	}
	
	

	@GetMapping
	public ResponseEntity<List<Event>> getListOfEventByAccountId( @RequestParam(required = true) @NotBlank(message = "account id should not blank")  String accountid){
		
		
		List<Event> responsEvent=eventService.getListOfEventByAccountId(accountid);
		
		return ResponseEntity.status(HttpStatus.OK).body(responsEvent);
	}
	

	@GetMapping("accounts/{accountId}/balance")
	public ResponseEntity<String> findtheNetBalanceByAccountId( @PathVariable @NotBlank(message = "account id should not blank")  String accountId){
		
		
		double balance=eventService.netBalance(accountId);
		
		String response=String.format("%s Balance is %.2f ", accountId,balance);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
