package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.Event;
import com.kematelyu.kematelyu.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCreatedBy(Staff staff);
}