package com.hugbo.clock_in.dto.request;

import java.time.Instant;

public class ShiftTaskRequestDTO {
    public Instant startTs;
    public Instant endTs;
    public Long taskId;
}
