package com.hugbo.clock_in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.domain.entity.User;
import com.hugbo.clock_in.dto.request.ContractRequestDTO;
import com.hugbo.clock_in.dto.response.ContractDTO;
import com.hugbo.clock_in.mappers.ContractMapper;
import com.hugbo.clock_in.repository.CompanyRepository;
import com.hugbo.clock_in.repository.ContractRepository;
import com.hugbo.clock_in.repository.UserRepository;

@Service
public class ContractService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractMapper contractMapper;

    public ContractDTO signToCompany(Long userId, Long companyId, ContractRequestDTO contractRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow();
        Company company = companyRepository.findById(companyId).orElseThrow();
        Contract contract = Contract.builder()
            .user(user)
            .company(company)
            .contractSettings(contractRequestDTO.contractSettings)
            .role(contractRequestDTO.role)
            .build();
        Contract savedContract = contractRepository.save(contract);

        return contractMapper.toDTO(savedContract);
    }

    public ContractDTO findByUserAndCompanyId(Long userId, Long companyId) {
        Contract contract = contractRepository.findByUserIdAndCompanyId(userId, companyId).orElseThrow();
        return contractMapper.toDTO(contract);
    }

    public List<ContractDTO> getAllContracts() {
        return contractRepository.findAll().stream().map(c -> contractMapper.toDTO(c)).toList();
    }
}
