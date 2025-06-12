package com.kematelyu.kematelyu.service;

import com.kematelyu.kematelyu.dto.CreateEventRequest;
import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Staff;
import com.kematelyu.kematelyu.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @InjectMocks private EventService service;
    @Mock private EventRepository repo;
    @Mock private StaffRepository staffRepo;

    private final Staff stubStaff = new Staff();

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        stubStaff.setId(11L);
        stubStaff.setName("Pak Staff");
    }

    @Test
    void createEvent_happy_path() {
        // 1. arrange dto & stubbing
        CreateEventRequest dto = new CreateEventRequest();
        dto.setTitle("Webinar TDD");
        dto.setDescription("Belajar TDD");
        dto.setDate(LocalDate.now().plusDays(10));
        dto.setTime(LocalTime.of(9, 0));
        dto.setMaxParticipant(100);

        when(staffRepo.findById(11L)).thenReturn(java.util.Optional.of(stubStaff));
        when(repo.save(any(Event.class))).thenAnswer(a -> a.getArgument(0)); // return entity as-is

        // 2. act
        Event saved = service.createEvent(dto, 11L);

        // 3. assert
        assertEquals(dto.getTitle(), saved.getTitle());
        assertEquals(stubStaff, saved.getCreatedBy());
        verify(repo).save(saved);
    }
}
