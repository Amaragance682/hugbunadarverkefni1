package com.hugbo.clock_in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hugbo.clock_in.SpecificationUtils;
import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.domain.entity.Location;
import com.hugbo.clock_in.domain.entity.Task;
import com.hugbo.clock_in.dto.filters.TaskFilterDTO;
import com.hugbo.clock_in.dto.request.TaskPatchRequestDTO;
import com.hugbo.clock_in.dto.request.TaskRequestDTO;
import com.hugbo.clock_in.dto.response.TaskDTO;
import com.hugbo.clock_in.mappers.TaskMapper;
import com.hugbo.clock_in.repository.CompanyRepository;
import com.hugbo.clock_in.repository.LocationRepository;
import com.hugbo.clock_in.repository.ShiftRepository;
import com.hugbo.clock_in.repository.TaskRepository;

import jakarta.validation.ValidationException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private LocationRepository locationRepository;

    public List<TaskDTO> getAllTasks() {
        return getTasks(new TaskFilterDTO());
    }
    public List<TaskDTO> getTasks(TaskFilterDTO taskFilterDTO) {
        Specification<Task> spec = SpecificationUtils.fromFilter(taskFilterDTO);

        List<Task> tasks = taskRepository.findAll(spec);
        return tasks
            .stream()
            .map(task -> taskMapper.toDTO(task))
            .toList();
    }

    public TaskDTO addTask(Long companyId, TaskRequestDTO taskRequestDTO) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        Location location = locationRepository.findById(taskRequestDTO.locationId).orElseThrow();

        Task task = Task.builder()
            .name(taskRequestDTO.name)
            .company(company)
            .description(taskRequestDTO.description)
            .isFinished(taskRequestDTO.isFinished)
            .location(location)
            .build();

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public TaskDTO patchTask(Long taskId, TaskPatchRequestDTO taskPatchRequestDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        validateTaskPatchRequest(taskPatchRequestDTO);

        String name = taskPatchRequestDTO.name;
        String description = taskPatchRequestDTO.description;
        Boolean isFinished = taskPatchRequestDTO.isFinished;

        if (name != null) task.name = name;
        if (description != null) task.description = description;
        if (isFinished != null) task.isFinished = isFinished;

        Task savedTask = taskRepository.save(task);

        return taskMapper.toDTO(savedTask);
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private void validateTaskPatchRequest(TaskPatchRequestDTO request) {
        String name = request.name;
        String description = request.description;
        Boolean isFinished = request.isFinished;
        if ((name == null || name.isBlank()) &&
            (description == null || description.isBlank()) &&
            isFinished == null) throw new ValidationException("At least one of the following fields is required; name, description, active, isFinished");
    }
}
