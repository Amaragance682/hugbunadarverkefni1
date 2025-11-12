package com.hugbo.clock_in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hugbo.clock_in.AuditableEntity;
import com.hugbo.clock_in.domain.entity.AuditLog;
import com.hugbo.clock_in.dto.response.AuditLogDTO;
import com.hugbo.clock_in.mappers.AuditLogMapper;
import com.hugbo.clock_in.repository.AuditLogRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private AuditLogMapper auditLogMapper;
    @PersistenceContext
    private final EntityManager em;

    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository
            .findAll()
            .stream()
            .map(a -> auditLogMapper.toDTO(a))
            .toList();
    }

    public List<AuditLogDTO> getAuditLogs(Long companyId, String entityType) {
        AuditableEntity ae = AuditableEntity.fromString(entityType)
            .orElseThrow(() -> new IllegalArgumentException("Invalid entity type requested"));

        String sql = "SELECT a.* FROM audit_log a " +
            "JOIN " + ae.tableName + " t " +
            "ON a.entity_id = t." + ae.tableJoinColumn + " " +
            "WHERE a.entity_type = :entityType " +
            "AND t." + ae.companyColumn + " = :companyId " +
            "ORDER BY a.at_ts ASC";
        if(entityType.equalsIgnoreCase("shifts")) {
            sql = """
                SELECT a.*
                FROM audit_log a
                JOIN shifts s ON a.entity_id = s.id
                JOIN user_company_contracts c ON s.contract_id = c.id
                WHERE a.entity_type = :entityType
                AND c.company_id = :companyId
                ORDER BY a.at_ts ASC
                """;
        }

        return em.createNativeQuery(sql, AuditLog.class)
            .setParameter("entityType", entityType)
            .setParameter("companyId", companyId)
            .getResultList()
            .stream()
            .map(r -> auditLogMapper.toDTO((AuditLog) r))
            .toList();
    }
}
