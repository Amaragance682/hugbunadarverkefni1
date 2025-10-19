package com.hugbo.clock_in.dto.filters;

import com.hugbo.clock_in.FilterPath;

public class TaskFilterDTO {
    @FilterPath("location.id")
    public Long locationId;

    @FilterPath("company.id")
    public Long companyId;

    @FilterPath("name")
    public String name;
    @FilterPath("description")
    public String description;
    @FilterPath("isFinished")
    public boolean isFinished;
}
