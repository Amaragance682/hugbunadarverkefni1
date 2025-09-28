package com.hugbo.clock_in.dto.auth;

import com.hugbo.clock_in.dto.response.UserDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthResponseDTO {
    public String message;
    public String token;
    public UserDTO user;

		public AuthResponseDTO(String message, String token, UserDTO user) {
			this.message = message;
			this.token = token;
			this.user = user;
		}
}
