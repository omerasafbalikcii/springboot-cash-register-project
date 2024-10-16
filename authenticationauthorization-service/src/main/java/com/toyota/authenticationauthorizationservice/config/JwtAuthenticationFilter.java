package com.toyota.authenticationauthorizationservice.config;

import com.toyota.authenticationauthorizationservice.dao.TokenRepository;
import com.toyota.authenticationauthorizationservice.service.abstracts.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

/**
 * Filter for JWT authentication in the application.
 * This filter is applied to every request to check for a valid JWT token.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        logger.info("Processing request to URL: {}", request.getRequestURL());
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No Bearer token found in Authorization header");
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        username = this.jwtService.extractUsername(jwt);

        logger.debug("Extracted JWT: {}", jwt);
        logger.debug("Extracted username from JWT: {}", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            boolean isTokenValid = this.tokenRepository.findById(this.jwtService.extractTokenId(jwt))
                    .map(token -> !token.getExpirationDate().before(new Date()) && !token.isRevoked())
                    .orElse(false);
            if (this.jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                logger.info("JWT is valid. Setting authentication context for user: {}", username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                logger.warn("JWT is invalid or token is not valid for user: {}", username);
            }
        } else {
            logger.debug("Username is null or authentication context already set");
        }
        filterChain.doFilter(request, response);
        logger.info("Finished processing request to URL: {}", request.getRequestURL());
    }
}
