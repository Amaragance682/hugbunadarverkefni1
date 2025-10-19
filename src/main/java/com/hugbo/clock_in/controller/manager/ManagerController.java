package com.hugbo.clock_in.controller.manager;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.dto.filters.EditRequestFilterDTO;
import com.hugbo.clock_in.dto.filters.LocationFilterDTO;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.filters.TaskFilterDTO;
import com.hugbo.clock_in.dto.request.LocationRequestDTO;
import com.hugbo.clock_in.dto.request.TaskRequestDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.dto.response.TaskDTO;
import com.hugbo.clock_in.service.EditRequestService;
import com.hugbo.clock_in.service.LocationService;
import com.hugbo.clock_in.service.ShiftService;
import com.hugbo.clock_in.service.TaskService;

@RestController
@RequestMapping("/companies/{companyId}/manager")
public class ManagerController {
    @Autowired
    private LocationService locationService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ShiftService shiftService;
    @Autowired
    private EditRequestService editRequestService;

    @PostMapping("/tasks")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> addTask(
        @PathVariable Long companyId,
        @RequestBody TaskRequestDTO taskRequestDTO
    ) {
        TaskDTO taskDTO = taskService.addTask(companyId, taskRequestDTO);
        return ResponseEntity.ok().body(taskDTO);
    }

    @PostMapping("/locations")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> addLocation(
        @PathVariable Long companyId,
        @RequestBody LocationRequestDTO locationRequestDTO
    ) {
        LocationDTO locationDTO = locationService.addLocation(locationRequestDTO, companyId);
        return ResponseEntity.ok().body(locationDTO);
    }

    @GetMapping("/shifts")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> getShifts(
        @PathVariable Long companyId,
        @RequestBody(required = true) ShiftFilterDTO shiftFilterDTO
    ) {
        List<ShiftDTO> shiftDTOs = shiftService.getShifts(shiftFilterDTO);
        return ResponseEntity.ok().body(shiftDTOs);
    }

    @GetMapping("/locations")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> getLocations(
        @PathVariable Long companyId,
        @RequestBody(required = true) LocationFilterDTO locationFilterDTO
    ) {
        List<LocationDTO> locationDTOs = locationService.getLocations(locationFilterDTO);
        return ResponseEntity.ok().body(locationDTOs);
    }

    @GetMapping("/tasks")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> getTasks(
        @PathVariable Long companyId,
        @RequestBody(required = true) TaskFilterDTO taskFilterDTO
    ) {
        List<TaskDTO> taskDTOs = taskService.getTasks(taskFilterDTO);
        return ResponseEntity.ok().body(taskDTOs);
    }

    @GetMapping("/edit-requests")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> getEditRequests(
        @PathVariable Long companyId,
        @RequestBody(required = true) EditRequestFilterDTO editRequestFilterDTO
    ) {
        List<EditRequestDTO> editRequestDTOs = editRequestService.getEditRequests(editRequestFilterDTO);
        return ResponseEntity.ok().body(editRequestDTOs);
    }
}
