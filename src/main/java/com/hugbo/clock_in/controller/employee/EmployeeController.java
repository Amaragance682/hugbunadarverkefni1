package com.hugbo.clock_in.controller.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.auth.CustomUserDetails;
import com.hugbo.clock_in.dto.employee.ClockInDTO;
import com.hugbo.clock_in.dto.employee.EmployeeIndexDTO;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.request.BreakRequestDTO;
import com.hugbo.clock_in.dto.request.EditRequestRequestDTO;
import com.hugbo.clock_in.dto.request.LocationRequestDTO;
import com.hugbo.clock_in.dto.request.SwitchTaskRequestDTO;
import com.hugbo.clock_in.dto.response.ContractDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.service.ContractService;
import com.hugbo.clock_in.service.EditRequestService;
import com.hugbo.clock_in.service.EmployeeService;
import com.hugbo.clock_in.service.LocationService;
import com.hugbo.clock_in.service.ShiftService;

@RequestMapping("/companies/{companyId}/employee")
@RestController
@PreAuthorize("@securityService.isCompanyEmployeeOrManager(authentication.principal.id, #companyId)")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private EditRequestService editRequestService;
    @Autowired
    private ContractService contractService;

    @GetMapping
    public ResponseEntity<?> index(
    @PathVariable Long companyId,
    @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        EmployeeIndexDTO index = employeeService.getEmployeeIndexData(companyId, customUserDetails.getId());
        return ResponseEntity.ok().body(index);
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getLocations(
    @PathVariable Long companyId
    ) {
        List<LocationDTO> locationDTOs = locationService.getLocationsAtCompany(companyId);
        return ResponseEntity.ok().body(locationDTOs);
    }

    @PostMapping("/clock-in")
    @PreAuthorize("@securityService.isCompanyEmployeeOrManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> clockIn(
    @PathVariable Long companyId,
    @AuthenticationPrincipal CustomUserDetails customUserDetails,
    @RequestBody ClockInDTO clockInDTO
    ) {
        ShiftCompleteDTO shiftDTO = shiftService.clockIn(customUserDetails.getId(), companyId, clockInDTO.taskId);
        return ResponseEntity.ok().body(shiftDTO);
    }

    @PostMapping("/clock-out")
    @PreAuthorize("@securityService.isCompanyEmployeeOrManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> clockOut(
    @PathVariable Long companyId,
    @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ShiftCompleteDTO shiftDTO = shiftService.clockOut(customUserDetails.getId(), companyId);
        return ResponseEntity.ok().body(shiftDTO);
    }

    @PostMapping("/switch-task")
    @PreAuthorize("@securityService.isCompanyEmployeeOrManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> switchTask(
    @PathVariable Long companyId,
    @AuthenticationPrincipal CustomUserDetails customUserDetails,
    @RequestBody SwitchTaskRequestDTO switchTaskRequestDTO
    ) {
        ShiftCompleteDTO shiftDTO = shiftService.switchTask(customUserDetails.getId(), companyId, switchTaskRequestDTO.taskId);
        return ResponseEntity.ok().body(shiftDTO);
    }

    @PostMapping("/start-break")
    @PreAuthorize("@securityService.isCompanyEmployeeOrManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> startBreak(
    @PathVariable Long companyId,
    @AuthenticationPrincipal CustomUserDetails customUserDetails,
    @RequestBody BreakRequestDTO breakRequestDTO
    ) {
        ShiftCompleteDTO shiftDTO = shiftService.startBreak(customUserDetails.getId(), companyId, breakRequestDTO);
        return ResponseEntity.ok().body(shiftDTO);
    }
    @PostMapping("/end-break")
    @PreAuthorize("@securityService.isCompanyEmployeeOrManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> endBreak(
    @PathVariable Long companyId,
    @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ShiftCompleteDTO shiftDTO = shiftService.endBreak(customUserDetails.getId(), companyId);
        return ResponseEntity.ok().body(shiftDTO);
    }

    @GetMapping("/shifts")
    @PreAuthorize("@securityService.isCompanyEmployee(authentication.principal.id, #companyId)")
    public ResponseEntity<?> getShifts(
        @PathVariable Long companyId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestBody(required = true) ShiftFilterDTO shiftFilterDTO
    ) {
        Long userId = customUserDetails.getId();
        shiftFilterDTO.userId = userId;

        List<ShiftCompleteDTO> shiftDTOs = shiftService.getShifts(shiftFilterDTO);
        return ResponseEntity.ok().body(shiftDTOs);
    }

    @PostMapping("/edit-request")
    @PreAuthorize("@securityService.isCompanyEmployee(authentication.principal.id, #companyId)")
    public ResponseEntity<?> addEditRequest(
        @PathVariable Long companyId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestBody(required = true) EditRequestRequestDTO editRequestRequestDTO
    ) {
        Long userId = customUserDetails.getId();
        ContractDTO contract = contractService.findByUserAndCompanyId(userId, companyId);

        EditRequestDTO editRequestDTO = editRequestService.addEditRequest(editRequestRequestDTO, contract.id);
        return ResponseEntity.ok().body(editRequestDTO);
    }
}
