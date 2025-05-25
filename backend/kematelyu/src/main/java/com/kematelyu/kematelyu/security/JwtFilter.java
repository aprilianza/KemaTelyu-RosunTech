package com.kematelyu.kematelyu.security;

import com.kematelyu.kematelyu.repository.UserRepository;
import com.kematelyu.kematelyu.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepo) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
                                    throws ServletException, IOException {

        String path   = req.getServletPath();
        String method = req.getMethod();

        // ✅ BYPASS: endpoint auth & public GET /api/events
        if (path.startsWith("/api/auth") ||
           (method.equals("GET") && path.equals("/api/events"))) {
            chain.doFilter(req, res);
            return;
        }

        // ✅ Validasi token bila ada
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Claims claims = jwtUtil.parse(token).getBody();
                Long   userId = Long.parseLong(claims.getSubject());
                String role   = claims.get("role", String.class);

                /* ⚠️ Cek apakah user masih ada di DB */
                if (!userRepo.existsById(userId)) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.getWriter().write("Token tidak valid: user tidak ditemukan");
                    return;
                }

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role)));

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Token invalid / expired → reject
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Token tidak valid atau expired");
                return;
            }
        }

        chain.doFilter(req, res);
    }
}
