package com.hugbo.clock_in.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO implements AuthRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be longer than 8 characters")
    public String password;

    @Override
    public String getEmail() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }
}
