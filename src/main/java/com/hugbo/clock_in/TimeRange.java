package com.hugbo.clock_in;

import java.time.Instant;

public interface TimeRange {
    Instant getStartTs();
    Instant getEndTs();
    void setStartTs(Instant i);
    void setEndTs(Instant i);
}
