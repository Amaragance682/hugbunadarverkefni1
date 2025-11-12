package com.hugbo.clock_in.dto.response;

import java.time.Instant;

import com.hugbo.clock_in.domain.entity.Status;
import com.hugbo.clock_in.dto.request.ShiftRequestDTO;

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
    public UserDTO user;
    public String reason;
    public ShiftRequestDTO requestedChanges;
    @Builder.Default
    public Status status = Status.PENDING;
    public UserDTO reviewedBy;
    public Instant reviewedAt;
}
