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

import com.hugbo.clock_in.dto.request.LocationRequestDTO;
import com.hugbo.clock_in.dto.request.TaskRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.dto.response.TaskDTO;
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

    @PostMapping("/tasks")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId)")
    public ResponseEntity<?> addTask(
        @PathVariable Long companyId,
        @PathVariable Long locationId,
        @RequestBody TaskRequestDTO taskRequestDTO
    ) {
        TaskDTO taskDTO = taskService.addTask(companyId, locationId, taskRequestDTO);
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
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long locationId, 
        @RequestParam(required = false) Long taskId, 
        @RequestParam(required = false) Instant from,
        @RequestParam(required = false) Instant to,
        @RequestParam(required = false) Boolean ongoing
    ) {
        List<ShiftDTO> shiftDTOs = shiftService.getShifts(companyId, userId, locationId, taskId, from, to, ongoing);
        return ResponseEntity.ok().body(shiftDTOs);
    }
}
