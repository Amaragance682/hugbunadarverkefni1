package com.hugbo.clock_in.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ShiftCompleteDTO {
    public ShiftDTO shift;
    public List<ShiftTaskDTO> shiftTasks = List.of();
    public List<ShiftBreakDTO> shiftBreaks = List.of();
    // and so on
}
