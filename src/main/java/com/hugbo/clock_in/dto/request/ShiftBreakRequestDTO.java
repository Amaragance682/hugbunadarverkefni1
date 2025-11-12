package com.hugbo.clock_in.dto.request;

import java.time.Instant;

public class ShiftBreakRequestDTO {
    public Instant startTs;
    public Instant endTs;
    public String breakType;
}
