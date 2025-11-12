package com.hugbo.clock_in.dto.request;

import java.time.Instant;
import java.util.List;

public class ShiftRequestDTO {
    public Long contractId;
    public Instant startTs;
    public Instant endTs;
    public List<ShiftTaskRequestDTO> shiftTasks;
    public List<ShiftBreakRequestDTO> shiftBreaks;
}
