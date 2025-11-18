package com.hugbo.clock_in;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Clears the AuditContext so that the user that caused a change to the db
// does not get transferred over to the next change, basically clears context
@Component
public class AuditCleanupFilter extends OncePerRequestFilter {
    @Autowired
    private AuditContext auditContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            auditContext.clearCurrentUserId();
        }
    }
}
