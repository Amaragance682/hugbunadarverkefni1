package com.hugbo.clock_in.dto.response;

import java.time.Instant;
import java.util.Objects;

import com.hugbo.clock_in.TimeRange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftDTO implements TimeRange {
    public Long id;
    public ContractDTO contract;
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftDTO other)) return false;
        return Objects.equals(this.id, other.id);
    }
}
