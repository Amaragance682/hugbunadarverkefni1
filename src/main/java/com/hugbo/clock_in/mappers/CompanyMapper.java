package com.hugbo.clock_in.mappers;

import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.dto.request.CompanyRequestDTO;
import com.hugbo.clock_in.dto.response.CompanyDTO;

@Component
public class CompanyMapper {
    public CompanyDTO toDTO(Company company) {
        if (company == null) return null;
        return new CompanyDTO(company.id, company.name);
    }

    public Company toEntity(CompanyRequestDTO companyRequestDTO) {
        if (companyRequestDTO == null) return null;
        Company company = new Company();
        company.name = companyRequestDTO.name;

        return company;
    }
}
