package com.hugbo.clock_in.dto.employee;

import com.hugbo.clock_in.dto.response.CompanyDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeIndexDTO {
    public CompanyDTO company;
    public EmployeeStatusDTO currentStatus;
    //public EmployeeStatsDTO stats;
    //
    public void setCompany(CompanyDTO companyDTO) {
        company = companyDTO;
    }
    public void setEmployeeStatus(EmployeeStatusDTO employeeStatusDTO) {
        currentStatus = employeeStatusDTO;
    }
    //public void setEmployeeStats(EmployeeStatsDTO employeeStatsDTO) {
    //    stats = employeeStatsDTO;
    //}
}
