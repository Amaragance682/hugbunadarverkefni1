package com.hugbo.clock_in.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hugbo.clock_in.dto.auth.*;
import com.hugbo.clock_in.exception.AuthenticationException;
import com.hugbo.clock_in.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        try {
            AuthResponseDTO authResponse = authService.registerUser(request);
            return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.token)
                .body(authResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            AuthResponseDTO authResponse = authService.authenticateUser(request);
            return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authResponse.token)
                .body(authResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }
}
