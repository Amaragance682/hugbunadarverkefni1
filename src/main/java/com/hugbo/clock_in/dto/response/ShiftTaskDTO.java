package com.hugbo.clock_in.dto.response;

import java.time.Instant;

import com.hugbo.clock_in.TimeRange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftTaskDTO implements TimeRange {
    public Long id;
    public Long shiftId;
    public TaskDTO task;
    public Instant startTs;
    public Instant endTs;
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
