package com.kematelyu.kematelyu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kematelyu.kematelyu.dto.LoginRequest;
import com.kematelyu.kematelyu.model.Mahasiswa;          // ✅ pakai subclass konkret
import com.kematelyu.kematelyu.repository.UserRepository;
import com.kematelyu.kematelyu.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)              // ✅ matikan filter security biar simpel
class AuthControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper om;

    @MockBean private UserRepository userRepo;         // ✅ disuntik ke controller
    @MockBean private JwtUtil jwt;                     // ✅ disuntik ke controller

    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

    @Test
    void login_ok_returns_token() throws Exception {
        /* -------- stub user -------- */
        Mahasiswa u = new Mahasiswa();
        u.setId(1L);
        u.setEmail("aa@bb.com");
        u.setPassword(enc.encode("secret"));            // cocok dengan BCrypt di controller
        u.setRole("MAHASISWA");

        Mockito.when(userRepo.findByEmail("aa@bb.com")).thenReturn(Optional.of(u));
        Mockito.when(jwt.generateToken(u.getId(), u.getRole())).thenReturn("dummy.jwt");

        /* -------- request body -------- */
        LoginRequest body = new LoginRequest();
        body.setEmail("aa@bb.com");
        body.setPassword("secret");

        /* -------- perform & assert -------- */
        mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").value("dummy.jwt"));
    }
}
