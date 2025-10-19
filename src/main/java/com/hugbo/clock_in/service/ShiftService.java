package com.hugbo.clock_in.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hugbo.clock_in.SpecificationUtils;
import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.domain.entity.Location;
import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.ShiftBreak;
import com.hugbo.clock_in.domain.entity.ShiftTask;
import com.hugbo.clock_in.domain.entity.Task;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.request.BreakRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftPatchRequestDTO;
import com.hugbo.clock_in.dto.response.ShiftBreakDTO;
import com.hugbo.clock_in.dto.response.ShiftCompleteDTO;
import com.hugbo.clock_in.dto.response.ShiftDTO;
import com.hugbo.clock_in.dto.response.ShiftTaskDTO;
import com.hugbo.clock_in.mappers.ShiftBreakMapper;
import com.hugbo.clock_in.mappers.ShiftMapper;
import com.hugbo.clock_in.mappers.ShiftTaskMapper;
import com.hugbo.clock_in.repository.ContractRepository;
import com.hugbo.clock_in.repository.LocationRepository;
import com.hugbo.clock_in.repository.ShiftBreakRepository;
import com.hugbo.clock_in.repository.ShiftRepository;
import com.hugbo.clock_in.repository.ShiftTaskRepository;
import com.hugbo.clock_in.repository.TaskRepository;

import jakarta.validation.ValidationException;

@Service
public class ShiftService {
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftTaskRepository shiftTaskRepository;
    @Autowired
    private ShiftBreakRepository shiftBreakRepository;

    @Autowired
    private ShiftMapper shiftMapper;
    @Autowired
    private ShiftTaskMapper shiftTaskMapper;
    @Autowired
    private ShiftBreakMapper shiftBreakMapper;
    
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<ShiftDTO> getShifts(
            ShiftFilterDTO shiftFilterDTO
    ) {
        Specification<Shift> spec = SpecificationUtils.fromFilter(shiftFilterDTO);

        List<Shift> shifts = shiftRepository.findAll(spec);
        return shifts
            .stream()
            .map(shift -> shiftMapper.toDTO(shift))
            .toList();
    }


    public List<ShiftDTO> getAllShifts() {
        return shiftRepository
            .findAll()
            .stream()
            .map(shift -> shiftMapper.toDTO(shift))
            .toList();
    }

    public List<ShiftDTO> getAllShiftsByCompany(Long companyId) {
        return shiftRepository
            .findByCompanyId(companyId)
            .stream()
            .map(shift -> shiftMapper.toDTO(shift))
            .toList();
    }

    public List<ShiftDTO> getOngoingShiftsByCompany(Long companyId) {
        return shiftRepository
            .findOngoingShiftsByCompany(companyId)
            .stream()
            .map(shift -> shiftMapper.toDTO(shift))
            .toList();
    }

    public ShiftCompleteDTO clockIn(Long userId, Long companyId, Long taskId) {
        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElse(null);
        if (shift != null) throw new IllegalStateException("User is already clocked in");
        Shift newShift = createShift(userId, companyId, taskId);
        shiftRepository.save(newShift);
        Task task = taskRepository.findById(taskId).orElseThrow();

        ShiftTask newShiftTask = createShiftTask(newShift, task);
        shiftTaskRepository.save(newShiftTask);

        return createCompleteShiftDTO(newShift);
    }

    public ShiftCompleteDTO clockOut(Long userId, Long companyId) {
        Instant now = Instant.now();

        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElseThrow(() -> new IllegalStateException("User is not clocked in"));

        ShiftTask shiftTask = shiftTaskRepository.findOngoingByShift(shift.id)
            .orElseThrow(() -> new IllegalStateException("User must be working on a task"));

        ShiftBreak shiftBreak = shiftBreakRepository.findOngoingByShift(shift.id)
            .orElse(null);

        if (shiftBreak != null) {
            shiftBreak.endTs = now;
            shiftBreakRepository.save(shiftBreak);
        }

        shift.endTs = now;
        shiftTask.endTs = now;
        Shift newShift = shiftRepository.save(shift);
        shiftTaskRepository.save(shiftTask);

        return createCompleteShiftDTO(newShift);
    }

    public ShiftCompleteDTO switchTask(Long userId, Long companyId, Long newTaskId) {
        Instant now = Instant.now();

        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElseThrow(() -> new IllegalStateException("User is not clocked in"));

        ShiftTask shiftTask = shiftTaskRepository.findOngoingByShift(shift.id)
            .orElseThrow(() -> new IllegalStateException("User must be working on a task"));

        shiftTask.endTs = now;
        shiftTaskRepository.save(shiftTask);

        Task task = taskRepository.findById(newTaskId).orElseThrow();
        ShiftTask newShiftTask = createShiftTask(shift, task);
        shiftTaskRepository.save(newShiftTask);

        return createCompleteShiftDTO(shift);
    }

    public ShiftCompleteDTO startBreak(Long userId, Long companyId, BreakRequestDTO breakRequestDTO) {
        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElseThrow(() -> new IllegalStateException("User is not clocked in"));

        ShiftBreak shiftBreak = shiftBreakRepository.findOngoingByShift(shift.id)
            .orElse(null);

        if (shiftBreak != null) throw new IllegalStateException("User is already on a break");

        ShiftBreak newShiftBreak = createShiftBreak(shift, breakRequestDTO);
        shiftBreakRepository.save(newShiftBreak);
        return createCompleteShiftDTO(shift);
    }
    public ShiftCompleteDTO endBreak(Long userId, Long companyId) {
        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElseThrow(() -> new IllegalStateException("User is not clocked in"));

        ShiftBreak shiftBreak = shiftBreakRepository.findOngoingByShift(shift.id)
            .orElse(null);

        if (shiftBreak == null) throw new IllegalStateException("User is not on a break");

        shiftBreak.endTs = Instant.now();
        shiftBreakRepository.save(shiftBreak);

        return createCompleteShiftDTO(shift);
    }

    public Shift createShift(Long userId, Long companyId, Long taskId) {
        Contract contract = contractRepository.findByUserIdAndCompanyId(userId, companyId).orElseThrow();

        Shift newShift = Shift.builder()
            .contract(contract)
            .startTs(Instant.now())
            .build();
        return newShift;
    }
    public ShiftTask createShiftTask(Shift shift, Task task) {
        ShiftTask newShiftTask = ShiftTask.builder()
            .shift(shift)
            .task(task)
            .startTs(Instant.now())
            .build();
        return newShiftTask;
    }
    public ShiftBreak createShiftBreak(Shift shift, BreakRequestDTO breakRequestDTO) {
        ShiftBreak newShiftBreak = ShiftBreak.builder()
            .shift(shift)
            .breakType(breakRequestDTO.breakType)
            .startTs(Instant.now())
            .build();
        return newShiftBreak;
    }

    private ShiftCompleteDTO createCompleteShiftDTO(Shift shift) {
        ShiftDTO shiftDTO = shiftMapper.toDTO(shift);
        List<ShiftBreak> shiftBreaks = shift.shiftBreaks;
        List<ShiftTask> shiftTasks = shift.shiftTasks;
        List<ShiftBreakDTO> shiftBreakDTOs = shiftBreaks != null ? shiftBreaks
            .stream()
            .map(shiftBreak -> shiftBreakMapper.toDTO(shiftBreak))
            .toList() : List.of();
        List<ShiftTaskDTO> shiftTaskDTOs = shiftTasks != null ? shiftTasks
            .stream()
            .map(shiftTask -> shiftTaskMapper.toDTO(shiftTask))
            .toList() : List.of();

        return new ShiftCompleteDTO(
            shiftDTO,
            shiftTaskDTOs,
            shiftBreakDTOs
        );
    }

    public ShiftCompleteDTO patchShift(Long shiftId, ShiftPatchRequestDTO requestDTO) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow();
        validateShiftPatchRequest(requestDTO);

        Instant startTs = requestDTO.startTs;
        Instant endTs = requestDTO.endTs;

        if (startTs != null) shift.startTs = startTs;
        if (endTs != null) shift.endTs = endTs;

        Shift savedShift = shiftRepository.save(shift);

        return createCompleteShiftDTO(savedShift);
    }

    private void validateShiftPatchRequest(ShiftPatchRequestDTO request) {
        Instant startTs = request.startTs;
        Instant endTs = request.endTs;

        if (startTs == null && endTs == null)
            throw new ValidationException("At least one of the following fields is required; startTs, endTs");
    }
}
