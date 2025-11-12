package com.hugbo.clock_in.mappers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.EditRequest;
import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.ShiftBreak;
import com.hugbo.clock_in.domain.entity.ShiftTask;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.dto.response.ShiftBreakDTO;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.dto.response.ShiftTaskDTO;
import com.hugbo.clock_in.repository.ContractRepository;

@Component
public class ShiftMapper {
    @Lazy
    @Autowired
    private ContractMapper contractMapper;
    @Lazy
    @Autowired
    private ShiftBreakMapper shiftBreakMapper;
    @Lazy
    @Autowired
    private ShiftTaskMapper shiftTaskMapper;

    public ShiftDTO toDTO(Shift shift) {
        if (shift == null) return null;
        return new ShiftDTO(
            shift.id,
            contractMapper.toDTO(shift.contract),
            shift.startTs,
            shift.endTs
        );
    }
    public Shift fromDTO(ShiftDTO shiftDTO) {
        if (shiftDTO == null) return null;
        return Shift.builder()
            .id(shiftDTO.id)
            .contract(contractMapper.fromDTO(shiftDTO.contract))
            .startTs(shiftDTO.startTs)
            .endTs(shiftDTO.endTs)
            .build();
    }

    public ShiftCompleteDTO createCompleteShiftDTO(Shift shift) {
        ShiftDTO shiftDTO = toDTO(shift);
        List<ShiftBreak> shiftBreaks = shift.shiftBreaks;
        List<ShiftTask> shiftTasks = shift.shiftTasks;
        List<ShiftBreakDTO> shiftBreakDTOs = shiftBreaks != null ? shiftBreaks
            .stream()
            .map(shiftBreak -> shiftBreakMapper.toDTO(shiftBreak))
            .toList() : List.of();
        List<ShiftTaskDTO> shiftTaskDTOs = shiftTasks != null ? shiftTasks
            .stream()
            .map(shiftTask -> shiftTaskMapper.toDTO(shiftTask))
            .toList() : List.of();

        return new ShiftCompleteDTO(
            shiftDTO,
            shiftTaskDTOs,
            shiftBreakDTOs
        );
    }
}
