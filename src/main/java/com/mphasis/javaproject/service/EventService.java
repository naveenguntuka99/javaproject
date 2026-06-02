package com.mphasis.javaproject.service;

import java.util.List;

import com.mphasis.javaproject.model.Event;


public interface EventService {

	Event createEvent( Event eventrequest);

	Event getEventById(String id);

	List<Event> getListOfEventByAccountId(String accountid);

	double netBalance(String accountID);

}
