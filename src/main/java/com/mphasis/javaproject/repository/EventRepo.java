package com.mphasis.javaproject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mphasis.javaproject.model.Event;



@Repository
public interface EventRepo extends JpaRepository<Event, String>{
	

    Optional<Event> findByEventId(String eventId);

    List<Event> findByAccountIdOrderByEventTimestampAsc(
            String accountId);

	List<Event> findByAccountId(String accountId);

	
}
