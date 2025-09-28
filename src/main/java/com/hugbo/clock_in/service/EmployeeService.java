package com.hugbo.clock_in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.dto.employee.EmployeeIndexDTO;
import com.hugbo.clock_in.dto.employee.EmployeeStatsDTO;
import com.hugbo.clock_in.dto.employee.EmployeeStatusDTO;
import com.hugbo.clock_in.mappers.ShiftMapper;
import com.hugbo.clock_in.repository.ShiftRepository;

@Service
public class EmployeeService {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftMapper shiftMapper;

    public EmployeeIndexDTO getEmployeeIndexData(Long companyId, Long userId) {
        EmployeeIndexDTO employeeIndexDTO = new EmployeeIndexDTO();
        employeeIndexDTO.setCompany(companyService.getCompany(companyId));
        employeeIndexDTO.setEmployeeStatus(getCurrentStatus(userId, companyId));
        return employeeIndexDTO;
    }
    
    private EmployeeStatusDTO getCurrentStatus(Long userId, Long companyId) {
        Shift currentShift = shiftRepository.findCurrentShift(userId, companyId).orElse(null);
        if (currentShift == null) {
            return new EmployeeStatusDTO(false, null);
        }
        return new EmployeeStatusDTO(true, shiftMapper.toDTO(currentShift));
    }
}
