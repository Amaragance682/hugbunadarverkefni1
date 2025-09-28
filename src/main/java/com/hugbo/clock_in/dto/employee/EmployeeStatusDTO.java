package com.hugbo.clock_in.dto.employee;

import java.time.Instant;

import com.hugbo.clock_in.dto.response.ShiftDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeStatusDTO {
    public Boolean isClockedIn;
    public ShiftDTO currentShift;

    public void setClockedIn(boolean clocked) {
        isClockedIn = clocked;
    }
    public void setShift(ShiftDTO shift) {
        currentShift = shift;
    }
}

