package com.hugbo.clock_in.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.response.AllDataDTO;
import com.hugbo.clock_in.dto.response.AuditLogDTO;
import com.hugbo.clock_in.dto.response.CompanyDTO;
import com.hugbo.clock_in.dto.response.ContractDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.dto.response.TaskDTO;
import com.hugbo.clock_in.dto.response.UserDTO;
import com.hugbo.clock_in.mappers.ShiftMapper;
import com.hugbo.clock_in.service.AuditLogService;
import com.hugbo.clock_in.service.AuthService;
import com.hugbo.clock_in.service.CompanyService;
import com.hugbo.clock_in.service.ContractService;
import com.hugbo.clock_in.service.EditRequestService;
import com.hugbo.clock_in.service.LocationService;
import com.hugbo.clock_in.service.ShiftService;
import com.hugbo.clock_in.service.TaskService;

/**
 * RootController
 */
@RestController
@RequestMapping("/")
public class RootController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ContractService contractService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private EditRequestService editRequestService;
    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllData() {
        List<UserDTO> users = authService.getAllUsers();
        List<CompanyDTO> companies = companyService.getAllCompanies();
        List<LocationDTO> locations = locationService.getAllLocations();
        List<ContractDTO> contracts = contractService.getAllContracts();
        List<TaskDTO> tasks = taskService.getAllTasks();
        List<ShiftCompleteDTO> shifts = shiftService.getShifts(new ShiftFilterDTO());
        List<EditRequestDTO> editRequests = editRequestService.getAllEditRequests();
        List<AuditLogDTO> auditLogs = auditLogService.getAllAuditLogs();

        return ResponseEntity.ok().body(new AllDataDTO(
                users,
                companies,
                locations,
                contracts,
                tasks,
                shifts,
                editRequests,
                auditLogs));
    }
}
