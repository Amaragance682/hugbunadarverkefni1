package com.hugbo.clock_in.dto.response;

import java.time.Duration;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftBreakDTO {
    public Long id;
    public Long shiftId;
    public String breakType;
    public Instant startTs;
    public Instant endTs;
}
