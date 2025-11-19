package com.hugbo.clock_in.dto.filters;

import com.hugbo.clock_in.FilterPath;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationFilterDTO {
    @FilterPath("company.id")
    public Long companyId;
    @FilterPath("name")
    public String name;
    @FilterPath("address")
    public String address;
}
