package com.hugbo.clock_in.dto.filters;

import java.time.Instant;

import com.hugbo.clock_in.FilterPath;
import com.hugbo.clock_in.domain.entity.Status;

public class EditRequestFilterDTO {
    @FilterPath("user.id")
    public Long userId;
    @FilterPath("shift.id")
    public Long shiftId;
    @FilterPath("shift.contract.company.id")
    public Long companyId;
    @FilterPath("reason")
    public String reason;
    @FilterPath("status")
    public Status status;
    @FilterPath("reviewedBy.id")
    public Long reviewedByUserId;

    @FilterPath("reviewedAt")
    public Instant reviewedAtFrom;

    @FilterPath("reviewedAt")
    public Instant reviewedAtTo;
}
