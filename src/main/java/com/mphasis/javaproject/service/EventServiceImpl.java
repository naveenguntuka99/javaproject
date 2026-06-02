package com.mphasis.javaproject.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mphasis.javaproject.constanst.EventType;
import com.mphasis.javaproject.exception.BusinessErrorCode;
import com.mphasis.javaproject.exception.BusinessException;
import com.mphasis.javaproject.model.Event;
import com.mphasis.javaproject.repository.EventRepo;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private  EventRepo eventRepo;
	
	private static final Set<String> VALID_CURRENCIES = Set.of("USD", "EUR", "INR");


	@Override
	public Event createEvent(Event eventrequest) {
		
		
		Optional<Event> existingEvent = eventRepo.findById(eventrequest.getEventId());
		
		// Here we are return the existing eventid deatils if we get multiple request with same id
		if(existingEvent.isPresent()) {
	        return existingEvent.get();
	    }
		
		
		// This method validate the details of event
		// As per payload timestamp comming as incomming request;
		validationOfEventDeatils(eventrequest);
		
		
		String eventType=eventrequest.getType();
		
		// here validation required for debit transaction to ensure must not low 
		
		return eventType.equals(EventType.DEBIT.toString())? debitEventValidatioandUpdate(eventrequest):eventRepo.save(eventrequest);
		
	}
	
	
	
	public static void validationOfEventDeatils(Event eventrequest) {
		
		  String  accountNumber=eventrequest.getAccountId();
		  
		  if(accountNumber.isBlank()) {
			  // custom Exception required here 
			  throw new BusinessException(BusinessErrorCode.INVALID_ACCOUNT_NUMBER);
		  }
		
		  String eventType=eventrequest.getType();
		
		  if(!eventType.equalsIgnoreCase(EventType.CREDIT.toString()) ||!eventType.equalsIgnoreCase(EventType.DEBIT.toString())) {
			  	  
			  throw new BusinessException(BusinessErrorCode.EVENT_TYPE, "Invalid event type is "+eventType);
		  }
		  String currency=eventrequest.getCurrency();
		  
		  if (!VALID_CURRENCIES.contains(currency)) {
			  
			  throw new BusinessException(BusinessErrorCode.CURRENCY_TYPE,"Invalid Curreny provided by you is "+currency);
			
		}
		  
		  	   	
	}
	

	public  Event debitEventValidatioandUpdate(Event eventrequest) {	
		
		Event savedEntityEvent = null;
		
		if(eventrequest.getAmount()<=0) {
			throw new RuntimeException("please enter the valid amount");
		}
			
		// validate the net balance, if less than debit amount and throw the exceptions

		if(eventrequest.getAmount()>netBalance(eventrequest.getAccountId())) {
			throw new BusinessException(BusinessErrorCode.INSUFFICIENT_FUNDS);
		}
		
		
		try {
					
			savedEntityEvent =	eventRepo.save(eventrequest);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return savedEntityEvent;	  
		   	
	}
	
	
	@Override
	public double netBalance(String accountId) {
		
		List<Event> eventsList=eventRepo.findByAccountId(accountId);
		
		double balance = eventsList.stream()
		        .mapToDouble(event ->
		            event.getType().equals("CREDIT")
		                ? event.getAmount()
		                : -event.getAmount())
		        .sum();
		
		return balance;	
		
	}



	@Override
	public Event getEventById(String id) {
				
	Optional<Event> existingEvent = eventRepo.findByEventId(id);
		
		if(!existingEvent.isPresent()) {
			throw new RuntimeException("Event id not found "+id);
	    }
			
		return existingEvent.get();
	}


	//order Ascending order timestamp

	@Override
	public List<Event> getListOfEventByAccountId(String accountid) {
		
		
		List<Event> eventsList=eventRepo.findByAccountIdOrderByEventTimestampAsc(accountid);
		
		if (eventsList.size()==0) {
			
			throw new BusinessException(BusinessErrorCode.INVALID_ACCOUNT_NUMBER);
			
		}
		
		return eventsList;
	}
	
	
	
	

}
