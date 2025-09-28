package com.hugbo.clock_in.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShiftDTO {
    public Long id;
    public ContractDTO contract;
    public Instant startTs;
    public Instant endTs;
}
