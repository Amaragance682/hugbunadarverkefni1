package com.hugbo.clock_in.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hugbo.clock_in.domain.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCompanyId(Long companyId);
}
