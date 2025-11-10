package com.hugbo.clock_in.dto.request;

import java.time.Instant;
import java.util.List;
import com.hugbo.clock_in.Fit;
import com.hugbo.clock_in.TimeRange;

import lombok.Builder;

public class ShiftPatchRequestDTO implements TimeRange{
    public Instant startTs;
    public Instant endTs;
    @Builder.Default
    public Fit taskFit = Fit.ALIGN_LEFT;
    @Builder.Default
    public Fit breakFit = Fit.ALIGN_LEFT;
    @Builder.Default
    public List<ShiftTaskPatchRequestDTO> tasks = List.of();
    @Builder.Default
    public List<ShiftBreakPatchRequestDTO> breaks = List.of();
    public String reason;
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
