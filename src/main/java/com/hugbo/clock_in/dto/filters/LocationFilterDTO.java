package com.hugbo.clock_in.dto.filters;

import com.hugbo.clock_in.FilterPath;

public class LocationFilterDTO {
    @FilterPath("company.id")
    public Long companyId;
    @FilterPath("name")
    public String name;
    @FilterPath("address")
    public String address;
}
