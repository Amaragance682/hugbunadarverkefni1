package com.hugbo.clock_in.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLogDTO {
    public Long id;
    public UserDTO actor;
    public String entityType;
    public Long entityId;
    public String action;
    public JsonNode beforeJson;
    public JsonNode afterJson;
    public Instant atTs;
}
