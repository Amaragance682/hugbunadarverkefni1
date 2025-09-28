package com.hugbo.clock_in.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hugbo.clock_in.domain.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    List<Task> findByLocationId(Long locationId);
    List<Task> findByCompanyId(Long companyId);
    List<Task> findByCompanyIdAndLocationIsNull(Long companyId);
}
