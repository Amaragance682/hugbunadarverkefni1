package com.hugbo.clock_in.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeStatsDTO {
    public int hoursThisWeek;
    public int tasksCompletedThisWeek;
    public int shiftsThisMonth;
}
