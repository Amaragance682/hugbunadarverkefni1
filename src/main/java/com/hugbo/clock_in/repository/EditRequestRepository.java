package com.hugbo.clock_in.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hugbo.clock_in.domain.entity.EditRequest;

/**
 * EditRequestRepository
 */
public interface EditRequestRepository extends JpaRepository<EditRequest, Long>, JpaSpecificationExecutor<EditRequest> {
}
