package com.hugbo.clock_in.mappers;

import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.response.UserDTO;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
						user.id,
						user.name,
						user.email,
						user.admin
						);
    }
}
