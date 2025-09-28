package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.ShiftBreak;
import com.hugbo.clock_in.dto.response.ShiftBreakDTO;

@Component
public class ShiftBreakMapper {
    @Autowired
    private ShiftMapper shiftMapper;
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
}
