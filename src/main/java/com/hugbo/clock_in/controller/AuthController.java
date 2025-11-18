package com.hugbo.clock_in.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.auth.CustomUserDetails;
import com.hugbo.clock_in.dto.auth.*;
import com.hugbo.clock_in.dto.response.UserDTO;
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

    @DeleteMapping("/me")
    public ResponseEntity<?> delete(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        authService.deleteUser(customUserDetails.getId());
        return ResponseEntity.ok().body("Your account has been deleted");
    }

    @PutMapping("/me")
    public ResponseEntity<?> replaceUser(
        @RequestBody UserPutRequestDTO userPutRequestDTO,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        UserDTO changedUser = authService.putUser(userPutRequestDTO, customUserDetails.getId());
        return ResponseEntity.ok().body(changedUser);
    }
}
