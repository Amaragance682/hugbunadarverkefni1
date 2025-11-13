package com.hugbo.clock_in.controller.manager;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
        @RequestParam boolean isOngoing,
        @RequestBody(required = true) ShiftFilterDTO shiftFilterDTO
    ) {
        List<ShiftCompleteDTO> shiftDTOs = shiftService.getShifts(shiftFilterDTO);
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
        @RequestBody(required = true) ShiftPatchRequestDTO shiftPatchRequestDTO
    ) {
        ShiftCompleteDTO patchedShift = shiftService.patchShift(shiftId, shiftPatchRequestDTO);
        return ResponseEntity.ok().body(patchedShift);
    }

    // *********************
    //       LOCATIONS
    // *********************
    @GetMapping("/locations")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getLocations(
        @PathVariable Long companyId,
        @RequestBody(required = true) LocationFilterDTO locationFilterDTO
    ) {
        List<LocationDTO> locationDTOs = locationService.getLocations(locationFilterDTO);
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
        @RequestBody(required = true) TaskFilterDTO taskFilterDTO
    ) {
        List<TaskDTO> taskDTOs = taskService.getTasks(taskFilterDTO);
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
        @RequestBody(required = true) EditRequestFilterDTO editRequestFilterDTO
    ) {
        List<EditRequestDTO> editRequestDTOs = editRequestService.getEditRequests(editRequestFilterDTO);
        return ResponseEntity.ok().body(editRequestDTOs);
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
