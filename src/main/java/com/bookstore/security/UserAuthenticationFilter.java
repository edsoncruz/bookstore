package com.bookstore.security;

import com.bookstore.entity.User;
import com.bookstore.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenService jwtTokenService;

    private final UserRepository userRepository;

    public UserAuthenticationFilter(JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromHeader(request);
        if (token != null) {
            String subject = jwtTokenService.getSubjectFromToken(token);
            User user = userRepository.findByUsername(subject).get();

            Authentication authentication =  new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
