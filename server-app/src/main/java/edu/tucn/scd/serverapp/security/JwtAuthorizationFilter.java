package edu.tucn.scd.serverapp.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component // creates a JwtAuthorizationFilter instance
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // the following resources don't need to be authorized
    private static final String[] AUTH_EXCEPTIONS = {
            // login endpoint
            "POST /login",

            // create positions
            "POST /positions",

            // swagger URLs
            "GET /swagger-ui/",
            "GET /v3/api-docs",

            // the UI (Admin app) URLs
            "GET /index.html",
            "GET /map.html",
            "GET /js/",
            "GET /favicon.ico",
            "GET /login.html",
            "GET /terminal.html",

            // user
            "POST /users"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //get the Authorization header from request
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) { // null check on Authorization header
            response.sendError(403); // return a 403-Forbidden error if the header is null
            return;
        }

        //remove 'Bearer' from the token header
        String jwtToken = authHeader.substring(7);

        try {
            JwtUtil.validateToken(jwtToken); // verify that the JWT token is valid
        } catch (JwtException e) {
            response.sendError(403); // return a 403-Forbidden error if the token is invalid
            return;
        }

        filterChain.doFilter(request, response);
    }

    // exclude the URL exceptions from the authorization filtering
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String requestMethod = request.getMethod();

        return Arrays.stream(AUTH_EXCEPTIONS).anyMatch(s -> (requestMethod + " " + requestUrl).startsWith(s));
    }
}
