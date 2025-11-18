package com.hugbo.clock_in.auth;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;
import com.hugbo.clock_in.domain.entity.Role;
import com.hugbo.clock_in.repository.ContractRepository;

@Service
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityService {
    private final ContractRepository contractRepository;
    
    public SecurityService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }
    
    public boolean isCompanyEmployeeOrManager(Long userId, Long companyId) {
        return contractRepository.hasContractByUserIdAndCompanyId(userId, companyId);
    }

    public boolean isCompanyManager(Long userId, Long companyId) {
        System.out.println(userId);
        return contractRepository.findByUserIdAndCompanyId(userId, companyId)
            .map(contract -> Role.MANAGER.equals(contract.role))
            .orElse(false);
    }

    public boolean isCompanyEmployee(Long userId, Long companyId) {
        return contractRepository.findByUserIdAndCompanyId(userId, companyId)
            .map(contract -> Role.EMPLOYEE.equals(contract.role))
            .orElse(false);
    }
}
