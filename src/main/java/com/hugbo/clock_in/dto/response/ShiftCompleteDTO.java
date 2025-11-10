package com.hugbo.clock_in.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ShiftCompleteDTO {
    public ShiftDTO shift;
    @Builder.Default
    public List<ShiftTaskDTO> shiftTasks = List.of();
    @Builder.Default
    public List<ShiftBreakDTO> shiftBreaks = List.of();
    @Builder.Default
    public List<EditRequestDTO> editRequests = List.of();
    // and so on
}
