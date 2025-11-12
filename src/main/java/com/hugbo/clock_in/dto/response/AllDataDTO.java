package com.hugbo.clock_in.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllDataDTO {
    List<UserDTO> users;
    List<CompanyDTO> companies;
    List<LocationDTO> locations;
    List<ContractDTO> contracts;
    List<TaskDTO> tasks;
    List<ShiftCompleteDTO> shifts;
    List<EditRequestDTO> editRequests;
    List<AuditLogDTO> auditLogs;
}
