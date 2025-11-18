package com.hugbo.clock_in;

import java.time.Instant;

// For all classes that implement a time range, so that time 
// modifying is generic and works for all, since they all basically work the same.
public interface TimeRange {
    Instant getStartTs();
    Instant getEndTs();
    void setStartTs(Instant i);
    void setEndTs(Instant i);
}
