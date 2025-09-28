package com.hugbo.clock_in.mappers;

import org.springframework.stereotype.Component;

import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.domain.entity.Location;
import com.hugbo.clock_in.dto.request.LocationRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;

@Component
public class LocationMapper {
    public LocationDTO toDTO(Location location) {
        if (location == null) return null;
        return new LocationDTO(
            location.id,
            location.name,
            location.address);
    }

    public Location toEntity(LocationRequestDTO locationRequestDTO, Company company) {
        if (locationRequestDTO == null) return null;

        Location location = Location.builder()
            .name(locationRequestDTO.name)
            .address(locationRequestDTO.address)
            .company(company)
            .build();
        return location;
    }
}
