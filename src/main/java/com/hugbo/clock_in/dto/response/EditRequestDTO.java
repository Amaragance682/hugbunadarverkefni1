package com.hugbo.clock_in.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugbo.clock_in.domain.entity.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditRequestDTO {
    public Long id;
    public ShiftDTO shift;
    public UserDTO user;
    public String reason;
    public JsonNode requestedChanges;
    @Builder.Default
    public Status status = Status.PENDING;
    public UserDTO reviewedBy;
    public Instant reviewedAt;
}
