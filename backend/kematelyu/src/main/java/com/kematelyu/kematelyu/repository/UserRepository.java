package com.kematelyu.kematelyu.repository;

import com.kematelyu.kematelyu.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}