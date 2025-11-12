package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugbo.clock_in.domain.entity.EditRequest;
import com.hugbo.clock_in.dto.request.ShiftRequestDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;

@Component
public class EditRequestMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;

    public EditRequestDTO toDTO(EditRequest editRequest) {
        if (editRequest == null) return null;
        try { 
            return new EditRequestDTO(
                editRequest.id,
                userMapper.toDTO(editRequest.user),
                editRequest.reason,
                objectMapper.treeToValue(editRequest.requestedChanges, ShiftRequestDTO.class),
                editRequest.status,
                userMapper.toDTO(editRequest.reviewedBy),
                editRequest.reviewedAt
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Requested changes could not be parsed, please check your format");
        }
    }
}
