package com.hugbo.clock_in.dto.request;

import java.time.Instant;

import com.hugbo.clock_in.TimeRange;

import jakarta.validation.constraints.NotBlank;

public class ShiftTaskPatchRequestDTO implements TimeRange {
    @NotBlank
    public Long id;
    public Instant startTs;
    public Instant endTs;
    public Long taskId;
    public Instant getStartTs() {
        return startTs;
    }
    public Instant getEndTs() {
        return endTs;
    }
    public void setStartTs(Instant i) {
        startTs = i;
    }
    public void setEndTs(Instant i) {
        endTs = i;
    }
}
