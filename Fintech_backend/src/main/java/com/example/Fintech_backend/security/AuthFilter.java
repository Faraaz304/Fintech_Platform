package com.example.Fintech_backend.security;

import com.example.Fintech_backend.exceptions.CustomAuthenticationEntryPoint;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getTokenFromRequest(request);

            if (jwt != null) {
                authenticateUser(jwt, request);
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            log.error("Authentication error: {}", ex.getMessage());
            authenticationEntryPoint.commence(request, response, ex);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        String userEmail;

        try {
            userEmail = tokenService.extractUsernameFromToken(jwt);
        } catch (Exception e) {
            log.error("Failed to extract username from JWT: {}", e.getMessage());
            throw new BadCredentialsException("Invalid JWT token", e);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (tokenService.isValidToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("User {} authenticated successfully", userEmail);
            } else {
                log.warn("Invalid JWT token for user {}", userEmail);
                throw new BadCredentialsException("Invalid JWT token");
            }
        }
    }
}

// MAIN CODE
// -----------------------------------------------------------------------------------------------------

// package com.example.Fintech_backend.security;

// import java.io.IOException;

// import org.springframework.lang.NonNull;
// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import
// org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.example.Fintech_backend.exceptions.CustomAuthenticationEntryPoint;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class AuthFilter extends OncePerRequestFilter {

// private final TokenService tokenService;
// private final CustomUserDetailsService userDetailsService;
// private final CustomAuthenticationEntryPoint authenticationEntryPoint;

// @Override
// protected void doFilterInternal(
// @NonNull HttpServletRequest request,
// @NonNull HttpServletResponse response,
// @NonNull FilterChain filterChain) throws ServletException, IOException {

// try {
// String jwt = getTokenFromRequest(request);

// if (jwt != null) {
// authenticateUser(jwt, request);
// }

// filterChain.doFilter(request, response);
// } catch (AuthenticationException ex) {
// authenticationEntryPoint.commence(request, response, ex);
// }
// }

// private String getTokenFromRequest(HttpServletRequest request) {
// String authHeader = request.getHeader("Authorization");

// if (authHeader != null && authHeader.startsWith("Bearer ")) {
// return authHeader.substring(7);
// }

// return null;
// }

// private void authenticateUser(String jwt, HttpServletRequest request) {
// String userEmail = tokenService.extractUsernameFromToken(jwt);

// if (userEmail != null &&
// SecurityContextHolder.getContext().getAuthentication() == null) {
// UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

// if (tokenService.isValidToken(jwt, userDetails)) {
// UsernamePasswordAuthenticationToken authToken = new
// UsernamePasswordAuthenticationToken(
// userDetails,
// null,
// userDetails.getAuthorities());

// authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request));
// SecurityContextHolder.getContext().setAuthentication(authToken);
// }
// }
// }
// }

// OLD VESION
// ------------------------------------------------------------------------------------------------------------------------------------
// package com.example.Fintech_backend.security;

// import java.io.IOException;

// import org.springframework.lang.NonNull;
// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import
// org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class AuthFilter extends OncePerRequestFilter {

// private final TokenService tokenService;
// private final CustomUserDetailsService userDetailsService;

// @Override
// protected void doFilterInternal(
// @NonNull HttpServletRequest request,
// @NonNull HttpServletResponse response,
// @NonNull FilterChain filterChain) throws ServletException, IOException {

// final String authHeader = request.getHeader("Authorization");
// final String jwt;
// final String userEmail;

// if (authHeader == null || !authHeader.startsWith("Bearer ")) {
// filterChain.doFilter(request, response);
// return;
// }

// jwt = authHeader.substring(7);
// userEmail = tokenService.extractUsernameFromToken(jwt);

// if (userEmail != null &&
// SecurityContextHolder.getContext().getAuthentication() == null) {
// UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

// if (tokenService.isValidToken(jwt, userDetails)) {
// UsernamePasswordAuthenticationToken authToken = new
// UsernamePasswordAuthenticationToken(
// userDetails,
// null,
// userDetails.getAuthorities());

// authToken.setDetails(new
// WebAuthenticationDetailsSource().buildDetails(request));
// SecurityContextHolder.getContext().setAuthentication(authToken);
// }
// }

// filterChain.doFilter(request, response);
// }
// }
