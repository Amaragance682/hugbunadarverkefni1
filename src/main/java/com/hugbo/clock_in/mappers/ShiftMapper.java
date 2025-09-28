package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;

@Component
public class ShiftMapper {
    @Autowired
    private ContractMapper contractMapper;
    public ShiftDTO toDTO(Shift shift) {
        if (shift == null) return null;
        return new ShiftDTO(
            shift.id,
            contractMapper.toDTO(shift.contract),
            shift.startTs,
            shift.endTs
        );
    }
}
