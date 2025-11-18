package com.hugbo.clock_in.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hugbo.clock_in.domain.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("""
    SELECT c
    FROM Company c
    JOIN Contract ucc ON ucc.company.id = c.id
    WHERE ucc.user.id = :userId
    """)
    List<Company> findAllByUserId(Long userId);
}
