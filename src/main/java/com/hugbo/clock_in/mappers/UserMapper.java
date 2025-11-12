package com.hugbo.clock_in.mappers;

import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.response.UserDTO;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
            .id(user.id)
            .name(user.name)
            .email(user.email)
            .admin(user.admin)
            .build();
    }
    public User fromDTO(UserDTO userDTO) {
        if (userDTO == null) return null;
        return User.builder()
            .id(userDTO.id)
            .name(userDTO.name)
            .email(userDTO.email)
            .admin(userDTO.admin)
            .build();
    }
}
