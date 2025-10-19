package com.hugbo.clock_in.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

public class EditRequestRequestDTO {
    public Long shiftId;
    public String reason;
    public JsonNode requestedChanges;
}
