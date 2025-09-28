package com.hugbo.clock_in.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hugbo.clock_in.domain.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
