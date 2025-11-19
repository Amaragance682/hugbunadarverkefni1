package com.hugbo.clock_in.controller.manager;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.auth.CustomUserDetails;
import com.hugbo.clock_in.domain.entity.Status;
import com.hugbo.clock_in.dto.filters.EditRequestFilterDTO;
import com.hugbo.clock_in.dto.filters.LocationFilterDTO;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.filters.TaskFilterDTO;
import com.hugbo.clock_in.dto.request.EditRequestPatchRequestDTO;
import com.hugbo.clock_in.dto.request.LocationPatchRequestDTO;
import com.hugbo.clock_in.dto.request.LocationRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftPatchRequestDTO;
import com.hugbo.clock_in.dto.request.TaskPatchRequestDTO;
import com.hugbo.clock_in.dto.request.TaskRequestDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
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

    // *********************
    //        SHIFTS
    // *********************
    @GetMapping("/shifts")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getShifts(
        @PathVariable Long companyId,
        @RequestParam(required = false) Long contractId,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @RequestParam(required = false) boolean isOngoing
    ) {
        ShiftFilterDTO filter = ShiftFilterDTO.builder()
            .companyId(companyId)
            .userId(userId)
            .contractId(contractId)
            .from(from)
            .to(to)
            .build();
        List<ShiftCompleteDTO> shiftDTOs = shiftService.getShifts(filter);
        if (isOngoing)
            shiftDTOs = shiftDTOs
                .stream()
                .filter(s -> s.shift.endTs == null)
                .toList();
        return ResponseEntity.ok().body(shiftDTOs);
    }

    @PatchMapping("/shifts/{shiftId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> patchShift(
        @PathVariable Long companyId,
        @PathVariable Long shiftId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestBody(required = true) ShiftPatchRequestDTO shiftPatchRequestDTO
    ) {
        ShiftCompleteDTO patchedShift = shiftService.patchShift(shiftId, shiftPatchRequestDTO, customUserDetails.getUser());
        return ResponseEntity.ok().body(patchedShift);
    }

    // *********************
    //       LOCATIONS
    // *********************
    @GetMapping("/locations")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getLocations(
        @PathVariable Long companyId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String address
    ) {
        LocationFilterDTO filter = LocationFilterDTO.builder()
            .companyId(companyId)
            .name(name)
            .address(address)
            .build();
        List<LocationDTO> locationDTOs = locationService.getLocations(filter);
        return ResponseEntity.ok().body(locationDTOs);
    }

    @PostMapping("/locations")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> addLocation(
        @PathVariable Long companyId,
        @RequestBody LocationRequestDTO locationRequestDTO
    ) {
        LocationDTO locationDTO = locationService.addLocation(locationRequestDTO, companyId);
        return ResponseEntity.ok().body(locationDTO);
    }

    @PatchMapping("/locations/{locationId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> patchLocation(
        @PathVariable Long companyId,
        @PathVariable Long locationId,
        @RequestBody LocationPatchRequestDTO locationPatchRequestDTO
    ) {
        LocationDTO locationDTO = locationService.patchLocation(locationId, locationPatchRequestDTO);
        return ResponseEntity.ok().body(locationDTO);
    }

    @DeleteMapping("/locations/{locationId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteLocation(
        @PathVariable Long companyId,
        @PathVariable Long locationId
    ) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.ok().body("Location deleted");
    }

    // *********************
    //         TASKS
    // *********************
    @GetMapping("/tasks")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getTasks(
        @PathVariable Long companyId,
        @RequestParam(required = false) Long locationId,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String description,
        @RequestParam(required = false, defaultValue = "false") boolean isFinished
    ) {
        TaskFilterDTO filter = TaskFilterDTO.builder()
            .companyId(companyId)
            .locationId(locationId)
            .name(name)
            .description(description)
            .isFinished(isFinished)
            .build();
        List<TaskDTO> taskDTOs = taskService.getTasks(filter);
        return ResponseEntity.ok().body(taskDTOs);
    }

    @PostMapping("/tasks")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> addTask(
        @PathVariable Long companyId,
        @RequestBody TaskRequestDTO taskRequestDTO
    ) {
        TaskDTO taskDTO = taskService.addTask(companyId, taskRequestDTO);
        return ResponseEntity.ok().body(taskDTO);
    }

    @PatchMapping("/tasks/{taskId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> patchTask(
        @PathVariable Long companyId,
        @PathVariable Long taskId,
        @RequestBody TaskPatchRequestDTO taskPatchRequestDTO
    ) {
        TaskDTO taskDTO = taskService.patchTask(taskId, taskPatchRequestDTO);
        return ResponseEntity.ok().body(taskDTO);
    }

    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(
        @PathVariable Long companyId,
        @PathVariable Long taskId
    ) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().body("Task deleted");
    }

    // *********************
    //     EDIT REQUESTS
    // *********************
    @GetMapping("/edit-requests")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getEditRequests(
        @PathVariable Long companyId,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) Long shiftId,
        @RequestParam(required = false) String reason,
        @RequestParam(required = false) Status status,
        @RequestParam(required = false) Long reviewedByUserId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant reviewedAtFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant reviewedAtTo,
        @RequestParam(required = false) boolean isOngoing
    ) {
        EditRequestFilterDTO filter = EditRequestFilterDTO.builder()
            .userId(userId)
            .shiftId(shiftId)
            .companyId(companyId)
            .reason(reason)
            .status(status)
            .reviewedByUserId(reviewedByUserId)
            .reviewedAtFrom(reviewedAtFrom)
            .reviewedAtTo(reviewedAtTo)
            .build();
        List<EditRequestDTO> editRequestDTOs = editRequestService.getEditRequests(filter);
        return ResponseEntity.ok().body(editRequestDTOs);
    }

    @GetMapping("/edit-requests/{editRequestId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getEditRequest(
        @PathVariable Long companyId,
        @PathVariable Long editRequestId
    ) {
        EditRequestDTO editRequestDTO = editRequestService.getEditRequest(editRequestId);
        return ResponseEntity.ok().body(editRequestDTO);
    }

    @PatchMapping("/edit-requests/{editRequestId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> patchEditRequest(
        @PathVariable Long companyId,
        @PathVariable Long editRequestId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestBody(required = true) EditRequestPatchRequestDTO editRequestPatchRequestDTO
    ) {
        EditRequestDTO editRequestDTO = editRequestService.patchEditRequest(editRequestPatchRequestDTO, editRequestId, customUserDetails.getId());
        return ResponseEntity.ok().body(editRequestDTO);
    }

    @PostMapping("/edit-requests/{editRequestId}/apply")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> applyEditRequest(
        @PathVariable Long companyId,
        @PathVariable Long editRequestId
    ) {
        ShiftCompleteDTO shiftCompleteDTO = editRequestService.applyEditRequest(editRequestId);
        return ResponseEntity.ok().body(shiftCompleteDTO);
    }
}
