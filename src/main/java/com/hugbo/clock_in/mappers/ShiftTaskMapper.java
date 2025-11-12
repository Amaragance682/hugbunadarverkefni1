package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.ShiftTask;
import com.hugbo.clock_in.dto.response.ShiftTaskDTO;
import com.hugbo.clock_in.repository.ShiftRepository;

@Component
public class ShiftTaskMapper {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ShiftRepository shiftRepository;
    public ShiftTaskDTO toDTO(ShiftTask shiftTask) {
        if (shiftTask == null) return null;
        return new ShiftTaskDTO(
            shiftTask.id,
            shiftTask.shift.id,
            taskMapper.toDTO(shiftTask.task),
            shiftTask.startTs,
            shiftTask.endTs
        );
    }
    public ShiftTask fromDTO(ShiftTaskDTO shiftTaskDTO) {
        if (shiftTaskDTO == null) return null;
        Shift shift = shiftRepository.findById(shiftTaskDTO.shiftId).orElseThrow();
        return ShiftTask.builder()
            .id(shiftTaskDTO.id)
            .shift(shift)
            .task(taskMapper.fromDTO(shiftTaskDTO.task))
            .startTs(shiftTaskDTO.startTs)
            .endTs(shiftTaskDTO.endTs)
            .build();
    }
}
