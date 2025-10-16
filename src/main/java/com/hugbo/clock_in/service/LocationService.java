package com.hugbo.clock_in.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.domain.entity.Location;
import com.hugbo.clock_in.dto.request.LocationPatchRequestDTO;
import com.hugbo.clock_in.dto.request.LocationRequestDTO;
import com.hugbo.clock_in.dto.response.LocationDTO;
import com.hugbo.clock_in.mappers.LocationMapper;
import com.hugbo.clock_in.repository.CompanyRepository;
import com.hugbo.clock_in.repository.LocationRepository;

import jakarta.validation.ValidationException;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private CompanyRepository companyRepository;

    public List<LocationDTO> getLocationsAtCompany(Long companyId) {
        List<Location> locations = locationRepository.findByCompanyId(companyId);

        return locations.stream().map(location -> locationMapper.toDTO(location)).toList();
    }

    public List<LocationDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return locations.stream().map(location -> locationMapper.toDTO(location)).toList();
    }

    public LocationDTO addLocation(LocationRequestDTO locationRequestDTO, Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        Location location = locationMapper.toEntity(locationRequestDTO, company);
        Location savedLocation = locationRepository.save(location);

        return locationMapper.toDTO(savedLocation);
    }

    public LocationDTO patchLocation(Long locationId, LocationPatchRequestDTO locationPatchRequestDTO) {
        Location location = locationRepository.findById(locationId).orElseThrow();
        validateLocationPatchRequest(locationPatchRequestDTO);

        String toBeName = locationPatchRequestDTO.name;
        String toBeAddress = locationPatchRequestDTO.address;
        if (toBeName != null && !toBeName.isBlank())
            location.name = toBeName;
        if (toBeAddress != null && !toBeAddress.isBlank())
            location.address = toBeAddress;

        Location savedLocation = locationRepository.save(location);

        return locationMapper.toDTO(savedLocation);
    }

    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public void validateLocationPatchRequest(LocationPatchRequestDTO locationPatchRequestDTO) {
        if ((locationPatchRequestDTO.name == null && !locationPatchRequestDTO.name.isBlank()) &&
        (locationPatchRequestDTO.address == null && !locationPatchRequestDTO.address.isBlank())) {
            throw new ValidationException("Either one of name, address or active is required and cannot be blank");
        }
    }
}
