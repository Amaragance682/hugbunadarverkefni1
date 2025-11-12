package com.hugbo.clock_in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.auth.AuthRequestDTO;
import com.hugbo.clock_in.dto.auth.AuthResponseDTO;
import com.hugbo.clock_in.dto.auth.RegisterRequestDTO;
import com.hugbo.clock_in.dto.response.UserDTO;
import com.hugbo.clock_in.exception.AuthenticationException;
import com.hugbo.clock_in.mappers.UserMapper;
import com.hugbo.clock_in.repository.UserRepository;
import com.hugbo.clock_in.auth.JwtUtils;
import com.hugbo.clock_in.auth.*;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserMapper userMapper;

    public AuthResponseDTO authenticateUser(AuthRequestDTO request) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getEmail());
            if (!passwordEncoder.matches(request.getPassword(), customUserDetails.getPassword())) {
                throw new AuthenticationException("Incorrect password");
            }
            String token = jwtUtils.generateToken(customUserDetails);

            return new AuthResponseDTO(
                    "Login successful",
                    token,
                    userMapper.toDTO(customUserDetails.user)
                    );
        } catch (UsernameNotFoundException e) {
            throw new AuthenticationException("No account exists with this email");
        }
    }

    public AuthResponseDTO registerUser(RegisterRequestDTO request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AuthenticationException("An account is already tied to this email");
            }
            User user = new User();
            user.name = request.name;
            user.email = request.email;
            user.admin = request.admin;
            String hashedPassword = passwordEncoder.encode(request.password);
            user.password = hashedPassword;

            User savedUser = userRepository.save(user);

            CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(request.getEmail());

            String token = jwtUtils.generateToken(customUserDetails);

            return new AuthResponseDTO(
                "Registration successful",
                token,
                userMapper.toDTO(savedUser)
                );
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> userMapper.toDTO(user)).toList();
    }
}
