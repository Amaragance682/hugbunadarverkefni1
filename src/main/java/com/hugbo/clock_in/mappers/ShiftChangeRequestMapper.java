package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.ShiftChangeRequest;
import com.hugbo.clock_in.dto.response.ShiftChangeRequestDTO;

@Component
public class ShiftChangeRequestMapper {
    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private UserMapper userMapper;
    public ShiftChangeRequestDTO toDTO(ShiftChangeRequest shiftChangeRequest) {
        if (shiftChangeRequest == null) return null;
        return new ShiftChangeRequestDTO(
            shiftChangeRequest.id,
            shiftMapper.toDTO(shiftChangeRequest.shift),
            userMapper.toDTO(shiftChangeRequest.user),
            shiftChangeRequest.reason,
            shiftChangeRequest.requestedChanges,
            shiftChangeRequest.status,
            userMapper.toDTO(shiftChangeRequest.reviewedBy),
            shiftChangeRequest.reviewedAt
        );
    }
}
