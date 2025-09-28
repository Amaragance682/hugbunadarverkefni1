package com.hugbo.clock_in.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugbo.clock_in.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractRequestDTO {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Builder.Default
    public JsonNode contractSettings = mapper.createObjectNode();

	@Builder.Default
	public Role role = Role.EMPLOYEE;
}
