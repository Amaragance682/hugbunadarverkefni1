package com.hugbo.clock_in.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.dto.request.CompanyPatchRequestDTO;
import com.hugbo.clock_in.dto.request.CompanyRequestDTO;
import com.hugbo.clock_in.dto.response.CompanyDTO;
import com.hugbo.clock_in.mappers.CompanyMapper;
import com.hugbo.clock_in.repository.CompanyRepository;
import jakarta.validation.ValidationException;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyMapper companyMapper;

    public List<CompanyDTO> getAllCompanies() {
        List<CompanyDTO> companies = companyRepository.findAll()
            .stream()
            .map(company -> companyMapper.toDTO(company))
            .toList();

        return companies;
    }

    public CompanyDTO getCompany(Long companyId) {
        CompanyDTO companyDTO = companyMapper.toDTO(
                companyRepository.findById(companyId).orElseThrow());

        return companyDTO;
    }

    public CompanyDTO addCompany(CompanyRequestDTO companyRequestDTO) {
        Company company = companyMapper.toEntity(companyRequestDTO);
        Company savedCompany = companyRepository.save(company);

        return companyMapper.toDTO(savedCompany);
    }

    public CompanyDTO patchCompany(Long companyId, CompanyPatchRequestDTO companyPatchRequestDTO) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        validateCompanyPatchRequest(companyPatchRequestDTO);

        if (companyPatchRequestDTO.name != null)
            company.name = companyPatchRequestDTO.name;

        Company savedCompany = companyRepository.save(company);

        return companyMapper.toDTO(savedCompany);
    }

    public void deleteCompany(Long companyId) {
        companyRepository.deleteById(companyId);
    }

    private void validateCompanyPatchRequest(CompanyPatchRequestDTO companyPatchRequestDTO) {
        if (companyPatchRequestDTO.name == null) {
            throw new ValidationException("New name or change of active state is required");
        }
    }
}
