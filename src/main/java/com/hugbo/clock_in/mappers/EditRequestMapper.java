package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.EditRequest;
import com.hugbo.clock_in.dto.response.EditRequestDTO;

@Component
public class EditRequestMapper {
    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private UserMapper userMapper;
    public EditRequestDTO toDTO(EditRequest editRequest) {
        if (editRequest == null) return null;
        return new EditRequestDTO(
            editRequest.id,
            shiftMapper.toDTO(editRequest.shift),
            userMapper.toDTO(editRequest.user),
            editRequest.reason,
            editRequest.requestedChanges,
            editRequest.status,
            userMapper.toDTO(editRequest.reviewedBy),
            editRequest.reviewedAt
        );
    }
}
