package com.hugbo.clock_in.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugbo.clock_in.SpecificationUtils;
import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.domain.entity.EditRequest;
import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.Status;
import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.filters.EditRequestFilterDTO;
import com.hugbo.clock_in.dto.request.EditRequestPatchRequestDTO;
import com.hugbo.clock_in.dto.request.EditRequestRequestDTO;
import com.hugbo.clock_in.dto.response.EditRequestDTO;
import com.hugbo.clock_in.mappers.EditRequestMapper;
import com.hugbo.clock_in.repository.ContractRepository;
import com.hugbo.clock_in.repository.EditRequestRepository;
import com.hugbo.clock_in.repository.ShiftRepository;
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
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;

    public List<EditRequestDTO> getEditRequests(EditRequestFilterDTO editRequestFilterDTO) {
        Specification<EditRequest> spec = SpecificationUtils.fromFilter(editRequestFilterDTO);
        List<EditRequest> editRequests = editRequestRepository.findAll(spec);

        return editRequests
            .stream()
            .map(editRequest -> editRequestMapper.toDTO(editRequest))
            .toList();
    }

    public EditRequestDTO addEditRequest(EditRequestRequestDTO editRequestRequestDTO, Long contractId) {
        Long shiftId = editRequestRequestDTO.shiftId;
        Shift shift = shiftRepository.findById(shiftId).orElseThrow();
        Contract contract = contractRepository.findById(contractId).orElseThrow();

        EditRequest request = EditRequest.builder()
            .shift(shift)
            .user(contract.user)
            .reason(editRequestRequestDTO.reason)
            .requestedChanges(editRequestRequestDTO.requestedChanges)
            .build();

        EditRequest addedRequest = editRequestRepository.save(request);

        return editRequestMapper.toDTO(addedRequest);
    }

    public EditRequestDTO patchEditRequest(EditRequestPatchRequestDTO editRequestPatchRequestDTO, Long editRequestId) {
        EditRequest editRequest = editRequestRepository.findById(editRequestId).orElseThrow();
        validateEditRequestPatchRequest(editRequestPatchRequestDTO);

        String toBeReason = editRequestPatchRequestDTO.reason;
        JsonNode toBeRequestedChanges = editRequestPatchRequestDTO.requestedChanges;
        Status toBeStatus = editRequestPatchRequestDTO.status;
        Long toBeReviewedByUserId = editRequestPatchRequestDTO.reviewedByUserId;
        Instant toBeReviewedAt = editRequestPatchRequestDTO.reviewedAt;

        User toBeReviewedBy = userRepository.findById(toBeReviewedByUserId).orElseThrow();

        if (toBeReason != null) editRequest.reason = toBeReason;
        if (toBeRequestedChanges != null) editRequest.requestedChanges = toBeRequestedChanges;
        if (toBeStatus != null) editRequest.status = toBeStatus;
        if (toBeReviewedBy != null) editRequest.reviewedBy = toBeReviewedBy;
        if (toBeReviewedAt != null) editRequest.reviewedAt = toBeReviewedAt;

        EditRequest savedRequest = editRequestRepository.save(editRequest);

        return editRequestMapper.toDTO(savedRequest);

        // ALSO CREATE AND ADD SHIFT NOTE
    }
    public void validateEditRequestPatchRequest(EditRequestPatchRequestDTO editRequestPatchRequestDTO) {
        // If any validation turns out to be needed later on
        if (false)
            throw new ValidationException("Either one of name, address or active is required and cannot be blank");
    }
}
