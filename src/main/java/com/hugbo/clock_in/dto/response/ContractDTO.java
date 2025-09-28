package com.hugbo.clock_in.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugbo.clock_in.domain.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDTO {
    public Long id;
    public UserDTO user;
    public CompanyDTO company;
    public JsonNode contractSettings;

    @Builder.Default
    public Role role = Role.EMPLOYEE;
}
