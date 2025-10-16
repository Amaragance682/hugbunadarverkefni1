package com.hugbo.clock_in.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO implements AuthRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 128, message = "Name must be between 2 and 128 characters")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be longer than 8 characters")
    public String password;

    // Just for dev
    public Boolean admin;
    public Boolean getAdmin() {
        return admin;
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
}
