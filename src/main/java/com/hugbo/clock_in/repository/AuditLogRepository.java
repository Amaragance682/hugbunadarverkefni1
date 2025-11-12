package com.hugbo.clock_in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hugbo.clock_in.domain.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
