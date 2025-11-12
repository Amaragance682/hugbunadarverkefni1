package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Task;
import com.hugbo.clock_in.dto.response.TaskDTO;

@Component
public class TaskMapper {
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private LocationMapper locationMapper;

    public TaskDTO toDTO(Task task) {
        if (task == null) return null;
        return new TaskDTO(
            task.id,
            companyMapper.toDTO(task.company),
            locationMapper.toDTO(task.location),
            task.name,
            task.description,
            task.isFinished
        );
    }
    public Task fromDTO(TaskDTO taskDTO) {
        if (taskDTO == null) return null;
        return Task.builder()
            .id(taskDTO.id)
            .company(companyMapper.fromDTO(taskDTO.company))
            .location(locationMapper.fromDTO(taskDTO.location))
            .name(taskDTO.name)
            .description(taskDTO.description)
            .isFinished(taskDTO.isFinished)
            .build();
    }
}
