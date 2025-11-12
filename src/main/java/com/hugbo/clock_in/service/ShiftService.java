package com.hugbo.clock_in.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;

import com.hugbo.clock_in.Fit;
import com.hugbo.clock_in.SpecificationUtils;
import com.hugbo.clock_in.TimeRange;
import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.domain.entity.EditRequest;
import com.hugbo.clock_in.domain.entity.Location;
import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.domain.entity.ShiftBreak;
import com.hugbo.clock_in.domain.entity.ShiftTask;
import com.hugbo.clock_in.domain.entity.Task;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;
import com.hugbo.clock_in.dto.request.BreakRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftBreakPatchRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftPatchRequestDTO;
import com.hugbo.clock_in.dto.request.ShiftTaskPatchRequestDTO;
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
    private EditRequestMapper editRequestMapper;
    
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<ShiftCompleteDTO> getShifts(
            ShiftFilterDTO shiftFilterDTO
    ) {
        Specification<Shift> spec = SpecificationUtils.fromFilter(shiftFilterDTO);

        List<Shift> shifts = shiftRepository.findAll(spec);

        return shifts
            .stream()
            .map(shift -> shiftMapper.createCompleteShiftDTO(shift))
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

    public List<ShiftCompleteDTO> getOngoingShiftsByCompany(Long companyId) {
        return shiftRepository
            .findOngoingShiftsByCompany(companyId)
            .stream()
            .map(shift -> shiftMapper.createCompleteShiftDTO(shift))
            .toList();
    }

    public ShiftCompleteDTO clockIn(Long userId, Long companyId, Long taskId) {
        Instant now = Instant.now();
        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElse(null);
        if (shift != null) throw new IllegalStateException("User is already clocked in");
        Shift newShift = createShift(userId, companyId, taskId);
        newShift.setStartTs(now);
        shiftRepository.save(newShift);
        Task task = taskRepository.findById(taskId).orElseThrow();
        ShiftTask newShiftTask = createShiftTask(newShift, task);
        newShiftTask.setStartTs(now);
        shiftTaskRepository.save(newShiftTask);

        return shiftMapper.createCompleteShiftDTO(newShift);
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

        return shiftMapper.createCompleteShiftDTO(newShift);
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
        newShiftTask.setStartTs(now);
        shiftTaskRepository.save(newShiftTask);

        return shiftMapper.createCompleteShiftDTO(shift);
    }

    public ShiftCompleteDTO startBreak(Long userId, Long companyId, BreakRequestDTO breakRequestDTO) {
        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElseThrow(() -> new IllegalStateException("User is not clocked in"));

        ShiftBreak shiftBreak = shiftBreakRepository.findOngoingByShift(shift.id)
            .orElse(null);

        if (shiftBreak != null) throw new IllegalStateException("User is already on a break");

        ShiftBreak newShiftBreak = createShiftBreak(shift, breakRequestDTO);
        shiftBreakRepository.save(newShiftBreak);
        return shiftMapper.createCompleteShiftDTO(shift);
    }
    public ShiftCompleteDTO endBreak(Long userId, Long companyId) {
        Shift shift = shiftRepository.findCurrentShift(userId, companyId)
            .orElseThrow(() -> new IllegalStateException("User is not clocked in"));

        ShiftBreak shiftBreak = shiftBreakRepository.findOngoingByShift(shift.id)
            .orElse(null);

        if (shiftBreak == null) throw new IllegalStateException("User is not on a break");

        shiftBreak.endTs = Instant.now();
        shiftBreakRepository.save(shiftBreak);

        return shiftMapper.createCompleteShiftDTO(shift);
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

    public ShiftCompleteDTO patchShift(Long shiftId, ShiftPatchRequestDTO requestDTO) {
        Shift shift = shiftRepository.findById(shiftId).orElseThrow();
        validateShiftPatchRequest(requestDTO, shiftMapper.createCompleteShiftDTO(shift));

        Instant startTs = requestDTO.startTs;
        Instant endTs = requestDTO.endTs;

        startTs = startTs == null ? shift.startTs : startTs;
        endTs = endTs == null ? shift.endTs : endTs;

        List<ShiftTask> shiftTasks = shiftTaskRepository.findByShiftId(shift.id);
        for (ShiftTaskPatchRequestDTO task : requestDTO.tasks) {
            ShiftTask shiftTask = shiftTaskRepository.findById(task.id).orElseThrow();
            shiftTask.startTs = task.startTs;
            shiftTask.endTs = task.endTs;
            if (task.taskId != null) {
                Task requestedTask = taskRepository.findById(task.taskId).orElseThrow();
                shiftTask.task = requestedTask;
            }
        }
        System.out.println("starting to fit tasks");
        if (requestDTO.tasks.isEmpty()) {
            Fit taskFit = requestDTO.taskFit;
            System.out.println(shift.shiftTasks.size());
            modifyTimelineToFit(taskFit, shift.shiftTasks, requestDTO, shift);
            System.out.println(shift.shiftTasks.size());
        }
        System.out.println("starting to fit breaks");
        if (requestDTO.breaks.isEmpty()) {
            Fit breakFit = requestDTO.breakFit;
            modifyTimelineToFit(breakFit, shift.shiftBreaks, requestDTO, shift);
        }
        shift.startTs = startTs;
        shift.endTs = endTs;

        Shift savedShift = shiftRepository.save(shift);
        for (ShiftTask shiftTask : savedShift.shiftTasks)
            shiftTaskRepository.save(shiftTask);
        for (ShiftBreak shiftBreak : savedShift.shiftBreaks)
            shiftBreakRepository.save(shiftBreak);

        return shiftMapper.createCompleteShiftDTO(shift);
        // ALSO CREATE AND ADD SHIFT NOTE
    }

    private <T extends TimeRange,
            Y extends TimeRange,
            U extends TimeRange> void modifyTimelineToFit(Fit fit, List<T> timeline, Y target, U original) {
        if (timeline.isEmpty()) return;
        Instant startT = target.getStartTs();
        Instant endT = target.getEndTs();
        Instant startO = original.getStartTs();
        Instant endO = original.getEndTs();

        Duration deltaStart = startO.until(startT);
        Duration deltaEnd = endO.until(endT);
        System.out.println(deltaStart);
        System.out.println(deltaEnd);

        boolean alignsLeft = timeline.getFirst().getStartTs().equals(startO);
        boolean alignsRight = timeline.getLast().getEndTs().equals(endO);

        System.out.println("starting switch case");
        switch (fit) {
            case Fit.RATIO:
                /*
                long originalDuration = startT.until(endT).getSeconds();
                long startPercentage;
                long endPercentage;
                timeline.forEach(part -> {
                    long startDuration = startO.until(part.getStartTs()).getSeconds();
                    long endDuration = startO.until(part.getEndTs()).getSeconds();
                    long originalStartP = originalDuration / startDuration;
                    long originalEndP = originalDuration / endDuration;
                });
                */
                throw new ValidationException("Incomplete - finish before testing!");
            case Fit.ALIGN_LEFT:
                for (int i = 0; i < timeline.size(); i++) {
                    T part = timeline.get(i);
                    part.setStartTs(part.getStartTs().plus(deltaStart));
                    part.setEndTs(part.getEndTs().plus(deltaStart));
                    System.out.println(part.getStartTs());
                    System.out.println(part.getEndTs());
                    if (i == timeline.size() - 1 && alignsRight)
                        part.setEndTs(endT);
                    if (part.getEndTs().isAfter(endT)) {
                        part.setEndTs(endT);
                        if (part.getStartTs().isAfter(endT)) {
                            timeline.remove(part);
                        }
                    }
                }
                break;
            case Fit.ALIGN_RIGHT:
                for (int i = 0; i < timeline.size(); i++) {
                    T part = timeline.get(i);
                    part.setStartTs(part.getStartTs().plus(deltaEnd));
                    part.setEndTs(part.getEndTs().plus(deltaEnd));
                    if (i == 0 && alignsLeft)
                        part.setStartTs(startT);
                    if (part.getStartTs().isBefore(startT)) {
                        part.setStartTs(startT);
                        if (part.getEndTs().isBefore(startT)) {
                            timeline.remove(part);
                        }
                    }
                }
                break;
        }
    }

    /*
            $ val no neg for parent 
            $ val no overlap for parent
            $ val if children -> all children
            $ val tl of children -> gap-free, fits to parent
            $ val for each child:
                $ no neg
                $ no overlap in-between (overlap between current is fine - since complete overhaul)

                yay
    */

    private void validateShiftPatchRequest(ShiftPatchRequestDTO request, ShiftCompleteDTO shift) {
        Instant startTs = request.startTs != null ? request.startTs : shift.shift.startTs;
        Instant endTs = request.endTs != null ? request.endTs : shift.shift.endTs;
        shift.shift.startTs = startTs;
        shift.shift.endTs = endTs;

        ShiftFilterDTO contractFilter = new ShiftFilterDTO();
        contractFilter.contractId = shift.shift.contract.id;
        List<ShiftDTO> contractShifts = getShifts(contractFilter)
            .stream()
            .map(s -> s.shift)
            .toList();

        handleNegativeDuration(shift.shift);
        handleOverlaps(shift.shift, contractShifts);

        System.out.println("checkpoint 1");
        validateShiftTaskRequests(request.tasks, shift);
        System.out.println("checkpoint 2");
        validateShiftBreakRequests(request.breaks, shift);
        request.tasks.forEach(t -> {
            validateShiftTaskRequest(t);
            handleOverlaps(t, request.tasks);
        });
        request.breaks.forEach(b -> {
            validateShiftBreakRequest(b);
            handleOverlaps(b, request.breaks);
        });
    }

    private void validateShiftTaskRequests(List<ShiftTaskPatchRequestDTO> taskRequests, ShiftCompleteDTO shift) {
        boolean areTasks = !taskRequests.isEmpty();
        if (areTasks) {
            System.out.println("here?");
            List<Long> idsToCheck = new ArrayList<>(shift.shiftTasks
                .stream()
                .map(v -> v.id)
                .toList());
            System.out.println("got through map");
            taskRequests.forEach(t -> idsToCheck.remove(t.id));
            System.out.println("got through forEAch");
            if (!idsToCheck.isEmpty())
                throw new ValidationException("If a change to shift tasks is requested, all shift tasks must be accounted for");

            System.out.println("checkpoint 3");

            validateFitsToTimeRange(taskRequests, shift.shift);

            System.out.println("checkpoint 4");

            taskRequests.forEach(request -> {
                handleOverlaps(request, taskRequests);
                System.out.println("checkpoint 5");
                validateShiftTaskRequest(request);
                System.out.println("checkpoint 6");
            });
        }
    }

    private void validateShiftBreakRequests(List<ShiftBreakPatchRequestDTO> breakRequests, ShiftCompleteDTO shift) {
        boolean areBreaks = !breakRequests.isEmpty();
        if (areBreaks) {
            List<Long> idsToCheck = shift.shiftBreaks
                .stream()
                .map(v -> v.id)
                .toList();
            breakRequests.forEach(t -> idsToCheck.remove(t.id));
            if (!idsToCheck.isEmpty())
                throw new ValidationException("If a change to shift breaks is requested, all shift breaks must be accounted for");
            breakRequests.forEach(request -> {
                validateShiftBreakRequest(request);
                handleOverlaps(request, breakRequests);
            });
        }
    }

    private void validateShiftTaskRequest(ShiftTaskPatchRequestDTO taskRequest) {
        ShiftTask shiftTask = shiftTaskRepository.findById(taskRequest.id).orElseThrow();
        Instant startTs = taskRequest.startTs != null ? taskRequest.startTs : shiftTask.startTs;
        Instant endTs = taskRequest.endTs != null ? taskRequest.endTs : shiftTask.endTs;
        shiftTask.startTs = startTs;
        shiftTask.endTs = endTs;

        handleOverlaps(shiftTask, shiftTask.shift.shiftTasks);
        handleNegativeDuration(shiftTask);
    }
    private void validateShiftBreakRequest(ShiftBreakPatchRequestDTO breakRequest) {
        ShiftBreak shiftBreak = shiftBreakRepository.findById(breakRequest.id).orElseThrow();
        Instant startTs = breakRequest.startTs != null ? breakRequest.startTs : shiftBreak.startTs;
        Instant endTs = breakRequest.endTs != null ? breakRequest.endTs : shiftBreak.endTs;
        shiftBreak.startTs = startTs;
        shiftBreak.endTs = endTs;

        handleOverlaps(shiftBreak, shiftBreak.shift.shiftBreaks);
        handleNegativeDuration(shiftBreak);
    }

    private <T extends TimeRange,D extends TimeRange> void validateFitsToTimeRange(List<T> toCheck, D target) {
        Instant targetS = target.getStartTs();
        Instant targetE = target.getEndTs();

        List<Tuple<Instant, Instant>> timeline = resolveTimeline(toCheck);

        if (targetS.compareTo(timeline.getFirst()._1()) != 0 || targetE.compareTo(timeline.getLast()._2()) != 0) {
            throw new ValidationException("The timeline of shift tasks must match the endpoints of the parent shift");
        }

        for (int i = 1; i < timeline.size(); i++) {
            if (timeline.get(i)._1() != timeline.get(i-1)._2())
                throw new ValidationException("There must be no gaps in the timeline of shift tasks provided");
        }
    }
    private <T extends TimeRange> List<Tuple<Instant, Instant>> resolveTimeline(List<T> list) {
        List<Tuple<Instant, Instant>> allTs = new ArrayList<>();

        list.sort((c, d) -> {
            if (c.getEndTs().isBefore(d.getStartTs()))
                return -1;
            if (c.getStartTs().isAfter(d.getEndTs()))
                return 1;
            return 0;
        });

        list.forEach(e -> {
            allTs.add(new Tuple<Instant, Instant>(e.getStartTs(), e.getEndTs()));
        });

        return allTs;
    }

    private <T extends TimeRange> void handleNegativeDuration(T t) {
        if (t.getStartTs().isAfter(t.getEndTs()))
            throw new ValidationException("Duration cannot be negative: " + t);
    }

    private <T extends TimeRange> void handleOverlaps(T t1, List<T> list) {
        ArrayList<T> overlaps = new ArrayList<>();
        list.forEach(t2 -> {
            if (!t1.equals(t2) && isOverlap(t1, t2)) {
                overlaps.add(t2);
            }
        });

        if (!overlaps.isEmpty())
            throw new ValidationException(t1.toString() + "would overlap with the following: \n" + String.join("\n", overlaps.toString()));
    }
    private <T extends TimeRange> boolean isOverlap(T t1, T t2) {
        Instant s1 = t1.getStartTs();
        Instant e1 = t1.getEndTs();
        Instant s2 = t2.getStartTs();
        Instant e2 = t2.getEndTs();
        return (s1.isBefore(s2) && e1.isAfter(s2)) ||
               (s1.isBefore(e2) && e1.isAfter(e2));
    }
}
