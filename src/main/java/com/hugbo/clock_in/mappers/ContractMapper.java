package com.hugbo.clock_in.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.dto.response.ContractDTO;

@Component
public class ContractMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CompanyMapper companyMapper;
    public ContractDTO toDTO(Contract contract) {
        if (contract == null) return null;
        return new ContractDTO(
            contract.id,
            userMapper.toDTO(contract.user),
            companyMapper.toDTO(contract.company),
            contract.contractSettings,
            contract.role
        );
    }
}
