package com.mphasis.javaproject.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.mphasis.javaproject.constanst.EventType;
import com.mphasis.javaproject.exception.BusinessException;
import com.mphasis.javaproject.model.Event;
import com.mphasis.javaproject.repository.EventRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;

    @BeforeEach
    void setup() {

        event = new Event();
        event.setEventId("EVT001");
        event.setAccountId("ACC001");
        event.setAmount(100.0);
        event.setCurrency("USD");
        event.setType(EventType.CREDIT.toString());
        event.setEventTimestamp(Instant.now());
    }
    
    
    //Idempotency test

    @Test
    void createEvent_ShouldReturnExistingEvent_WhenEventAlreadyExists() {

        when(eventRepo.findById("EVT001"))
                .thenReturn(Optional.of(event));

        Event result = eventService.createEvent(event);

        assertNotNull(result);
        assertEquals("EVT001", result.getEventId());

        verify(eventRepo, never()).save(any());
    }

   

    @Test
    void getEventById_ShouldReturnEvent() {

        when(eventRepo.findByEventId("EVT001"))
                .thenReturn(Optional.of(event));

        Event result = eventService.getEventById("EVT001");

        assertNotNull(result);
        assertEquals("EVT001", result.getEventId());
    }

    @Test
    void getEventById_ShouldThrowException_WhenNotFound() {

        when(eventRepo.findByEventId("EVT001"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> eventService.getEventById("EVT001"));

        assertTrue(ex.getMessage().contains("Event id not found"));
    }

    @Test
    void netBalance_ShouldCalculateCorrectBalance() {

        Event credit = new Event();
        credit.setType("CREDIT");
        credit.setAmount(100.0);

        Event debit = new Event();
        debit.setType("DEBIT");
        debit.setAmount(40.0);

        when(eventRepo.findByAccountId("ACC001"))
                .thenReturn(Arrays.asList(credit, debit));

        double balance = eventService.netBalance("ACC001");

        assertEquals(60.0, balance);
    }

    @Test
    void getListOfEventByAccountId_ShouldReturnEvents() {

        when(eventRepo.findByAccountIdOrderByEventTimestampAsc("ACC001"))
                .thenReturn(List.of(event));

        List<Event> result =
                eventService.getListOfEventByAccountId("ACC001");

        assertEquals(1, result.size());
    }

    @Test
    void getListOfEventByAccountId_ShouldThrowBusinessException() {

        when(eventRepo.findByAccountIdOrderByEventTimestampAsc("ACC001"))
                .thenReturn(Collections.emptyList());

        assertThrows(
                BusinessException.class,
                () -> eventService.getListOfEventByAccountId("ACC001"));
    }

    @Test
    void debitEventValidation_ShouldThrowException_WhenAmountLessThanZero() {

        event.setType("DEBIT");
        event.setAmount(-10.0);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> eventService.debitEventValidatioandUpdate(event));

        assertEquals("please enter the valid amount",
                ex.getMessage());
    }

    @Test
    void debitEventValidation_ShouldThrowBusinessException_WhenInsufficientFunds() {

        event.setType("DEBIT");
        event.setAmount(1000.0);

        Event credit = new Event();
        credit.setType("CREDIT");
        credit.setAmount(100.0);

        when(eventRepo.findByAccountId("ACC001"))
                .thenReturn(List.of(credit));

        assertThrows(
                BusinessException.class,
                () -> eventService.debitEventValidatioandUpdate(event));
    }

    @Test
    void debitEventValidation_ShouldSaveDebitEvent() {

        event.setType("DEBIT");
        event.setAmount(50.0);

        Event credit = new Event();
        credit.setType("CREDIT");
        credit.setAmount(100.0);

        when(eventRepo.findByAccountId("ACC001"))
                .thenReturn(List.of(credit));

        when(eventRepo.save(any(Event.class)))
                .thenReturn(event);

        Event result =
                eventService.debitEventValidatioandUpdate(event);

        assertNotNull(result);

        verify(eventRepo).save(event);
    }
}
