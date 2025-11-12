package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.ShiftBreak;
import com.hugbo.clock_in.dto.response.ShiftBreakDTO;
import com.hugbo.clock_in.repository.ShiftRepository;

@Component
public class ShiftBreakMapper {
    @Autowired
    private ShiftRepository shiftRepository;
    public ShiftBreakDTO toDTO(ShiftBreak shiftBreak) {
        if (shiftBreak == null) return null;
        return new ShiftBreakDTO(
            shiftBreak.id,
            shiftBreak.shift.id,
            shiftBreak.breakType,
            shiftBreak.startTs,
            shiftBreak.endTs
        );
    }
    public ShiftBreak fromDTO(ShiftBreakDTO shiftBreakDTO) {
        if (shiftBreakDTO == null) return null;
        Shift shift = shiftRepository.findById(shiftBreakDTO.shiftId).orElseThrow();
        return ShiftBreak.builder()
            .id(shiftBreakDTO.id)
            .shift(shift)
            .breakType(shiftBreakDTO.breakType)
            .startTs(shiftBreakDTO.startTs)
            .endTs(shiftBreakDTO.endTs)
            .build();
    }
}
