package com.hugbo.clock_in.controller.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.dto.response.AuditLogDTO;
import com.hugbo.clock_in.service.AuditLogService;

@RestController
@RequestMapping("/companies/{companyId}/manager/audit")
public class ManagerAuditController {
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getAuditHistory(
        @PathVariable Long companyId,
        @RequestBody String entityType
    ) {
        List<AuditLogDTO> auditLogs = auditLogService.getAuditLogs(companyId, entityType);
        return ResponseEntity.ok().body(auditLogs);
    }
}
