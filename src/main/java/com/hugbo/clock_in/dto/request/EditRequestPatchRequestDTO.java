package com.hugbo.clock_in.dto.request;

import java.time.Instant;

import com.hugbo.clock_in.domain.entity.Status;

public class EditRequestPatchRequestDTO {
    public String reason;
    public ShiftRequestDTO requestedChanges;
    public Status status;
    public Long reviewedByUserId;
    public Instant reviewedAt;
}
