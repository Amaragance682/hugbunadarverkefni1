package com.hugbo.clock_in.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugbo.clock_in.SpecificationUtils;
import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.domain.entity.EditRequest;
import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.ShiftBreak;
import com.hugbo.clock_in.domain.entity.ShiftTask;
import com.hugbo.clock_in.domain.entity.Status;
import com.hugbo.clock_in.domain.entity.Task;
import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.filters.EditRequestFilterDTO;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.request.EditRequestPatchRequestDTO;
import com.hugbo.clock_in.dto.request.EditRequestRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftBreakRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftTaskRequestDTO;
import com.hugbo.clock_in.dto.request.TaskRequestDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.dto.response.ShiftBreakDTO;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.dto.response.ShiftTaskDTO;
import com.hugbo.clock_in.mappers.EditRequestMapper;
import com.hugbo.clock_in.mappers.ShiftBreakMapper;
import com.hugbo.clock_in.mappers.ShiftMapper;
import com.hugbo.clock_in.mappers.ShiftTaskMapper;
import com.hugbo.clock_in.repository.ContractRepository;
import com.hugbo.clock_in.repository.EditRequestRepository;
import com.hugbo.clock_in.repository.ShiftBreakRepository;
import com.hugbo.clock_in.repository.ShiftRepository;
import com.hugbo.clock_in.repository.ShiftTaskRepository;
import com.hugbo.clock_in.repository.TaskRepository;
import com.hugbo.clock_in.repository.UserRepository;

import jakarta.validation.ValidationException;

@Service
public class EditRequestService {
    @Autowired
    private EditRequestRepository editRequestRepository;
    @Autowired
    private EditRequestMapper editRequestMapper;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftTaskRepository shiftTaskRepository;
    @Autowired
    private ShiftBreakRepository shiftBreakRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private ObjectMapper objectMapper;

    public List<EditRequestDTO> getAllEditRequests() {
        return getEditRequests(new EditRequestFilterDTO());
    }

    public List<EditRequestDTO> getEditRequests(EditRequestFilterDTO editRequestFilterDTO) {
        Specification<EditRequest> spec = SpecificationUtils.fromFilter(editRequestFilterDTO);
        List<EditRequest> editRequests = editRequestRepository.findAll(spec);

        return editRequests
            .stream()
            .map(editRequest -> editRequestMapper.toDTO(editRequest))
            .toList();
    }

    public EditRequestDTO addEditRequest(EditRequestRequestDTO editRequestRequestDTO, Long contractId) {
        Contract contract = contractRepository.findById(contractId).orElseThrow();
        EditRequest request = EditRequest.builder()
            .user(contract.user)
            .reason(editRequestRequestDTO.reason)
            .requestedChanges(objectMapper.valueToTree(editRequestRequestDTO.requestedChanges))
            .build();

        EditRequest addedRequest = editRequestRepository.save(request);

        return editRequestMapper.toDTO(addedRequest);
    }

    public EditRequestDTO patchEditRequest(EditRequestPatchRequestDTO editRequestPatchRequestDTO, Long editRequestId) {
        EditRequest editRequest = editRequestRepository.findById(editRequestId).orElseThrow();
        validateEditRequestPatchRequest(editRequestPatchRequestDTO);

        String toBeReason = editRequestPatchRequestDTO.reason;
        ShiftRequestDTO toBeRequestedChanges = editRequestPatchRequestDTO.requestedChanges;
        Status toBeStatus = editRequestPatchRequestDTO.status;
        Long toBeReviewedByUserId = editRequestPatchRequestDTO.reviewedByUserId;
        Instant toBeReviewedAt = editRequestPatchRequestDTO.reviewedAt;

        if (toBeReason != null) editRequest.reason = toBeReason;
        if (toBeRequestedChanges != null) editRequest.requestedChanges = objectMapper.valueToTree(toBeRequestedChanges);
        if (toBeStatus != null) editRequest.status = toBeStatus;
        if (toBeReviewedByUserId != null) {
            User toBeReviewedBy = userRepository.findById(toBeReviewedByUserId).orElseThrow();
            editRequest.reviewedBy = toBeReviewedBy;
        }
        if (toBeReviewedAt != null) editRequest.reviewedAt = toBeReviewedAt;

        EditRequest savedRequest = editRequestRepository.save(editRequest);

        return editRequestMapper.toDTO(savedRequest);

        // ALSO CREATE AND ADD SHIFT NOTE
    }
    public ShiftCompleteDTO applyEditRequest(Long editRequestId) {
        EditRequest editRequest = editRequestRepository.findById(editRequestId).orElseThrow();
        if (editRequest.status != Status.APPROVED)
            throw new ValidationException("Request cannot be applied if it has not been approved");

        ShiftRequestDTO requestedChanges;
        try {
            requestedChanges = objectMapper.treeToValue(editRequest.requestedChanges, ShiftRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw new ValidationException("Invalid format of requested edit!", e);
        }
        Contract contract = contractRepository.findById(requestedChanges.contractId).orElseThrow();
        Shift newShift = Shift.builder()
            .contract(contract)
            .startTs(requestedChanges.startTs)
            .endTs(requestedChanges.endTs)
            .build();
        ArrayList<ShiftTask> addedShiftTasks = new ArrayList<>();
        ArrayList<ShiftBreak> addedShiftBreaks = new ArrayList<>();
        for (ShiftTaskRequestDTO rcShiftTask : requestedChanges.shiftTasks) {
            Task task = taskRepository.findById(rcShiftTask.taskId).orElseThrow();
            ShiftTask newShiftTask = ShiftTask.builder()
                .task(task)
                .shift(newShift)
                .startTs(rcShiftTask.startTs)
                .endTs(rcShiftTask.endTs)
                .build();
            addedShiftTasks.add(newShiftTask);
        }
        for (ShiftBreakRequestDTO rcShiftBreak : requestedChanges.shiftBreaks) {
            ShiftBreak newShiftBreak = ShiftBreak.builder()
                .shift(newShift)
                .breakType(rcShiftBreak.breakType)
                .startTs(rcShiftBreak.startTs)
                .endTs(rcShiftBreak.endTs)
                .build();
            addedShiftBreaks.add(newShiftBreak);
        }
        shiftRepository.delete(SpecificationUtils.fromFilter(ShiftFilterDTO.builder()
                .from(newShift.startTs)
                .to(newShift.endTs)
                .build()));

        newShift = shiftRepository.save(newShift);
        addedShiftTasks.forEach(shiftTask -> shiftTaskRepository.save(shiftTask));
        addedShiftBreaks.forEach(shiftBreak -> shiftBreakRepository.save(shiftBreak));

        return shiftMapper.createCompleteShiftDTO(newShift);
    }
    public void validateEditRequestPatchRequest(EditRequestPatchRequestDTO editRequestPatchRequestDTO) {
        // If any validation turns out to be needed later on
        if (false)
            throw new ValidationException("Either one of name, address or active is required and cannot be blank");
    }
}
