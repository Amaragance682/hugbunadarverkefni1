package com.hugbo.clock_in.dto.filters;

import java.time.Instant;

import com.hugbo.clock_in.FilterPath;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftFilterDTO {
    @FilterPath("contract.id")
    public Long contractId;

    @FilterPath("contract.user.id")
    public Long userId;

    @FilterPath("contract.company.id")
    public Long companyId;

    @FilterPath("startTs")
    public Instant from;

    @FilterPath("endTs")
    public Instant to;
}
