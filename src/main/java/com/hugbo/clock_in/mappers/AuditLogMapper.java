package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.AuditLog;
import com.hugbo.clock_in.dto.response.AuditLogDTO;

@Component
public class AuditLogMapper {
    @Autowired
    private UserMapper userMapper;
    public AuditLogDTO toDTO(AuditLog auditLog) {
        if (auditLog == null) return null;
        return new AuditLogDTO().builder()
            .id(auditLog.id)
            .actor(userMapper.toDTO(auditLog.actor))
            .entityType(auditLog.entityType)
            .entityId(auditLog.entityId)
            .action(auditLog.action)
            .beforeJson(auditLog.beforeJson)
            .afterJson(auditLog.afterJson)
            .atTs(auditLog.atTs)
            .build();
    }
}
