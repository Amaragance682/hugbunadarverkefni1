package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.ShiftTask;
import com.hugbo.clock_in.dto.response.ShiftTaskDTO;

@Component
public class ShiftTaskMapper {
    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private TaskMapper taskMapper;
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
}
