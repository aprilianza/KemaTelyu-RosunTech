package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {}