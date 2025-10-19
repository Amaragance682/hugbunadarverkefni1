package com.hugbo.clock_in.dto.filters;

import java.time.Instant;

import com.hugbo.clock_in.FilterPath;

public class ShiftFilterDTO {
    @FilterPath("contract.id")
    public Long contractId;

    @FilterPath("contract.user.id")
    public Long userId;

    //public Long taskId;   <-- not doing this no way no way

    @FilterPath("contract.company.id")
    public Long companyId;

    @FilterPath("startTs")
    public Instant from;

    @FilterPath("endTs")
    public Instant to;
}
