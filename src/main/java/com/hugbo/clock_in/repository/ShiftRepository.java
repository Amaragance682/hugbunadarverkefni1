package com.hugbo.clock_in.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hugbo.clock_in.domain.entity.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> {
    @Query("""
        SELECT s FROM Shift s
        WHERE s.contract.user.id = :userId
        AND s.contract.company.id = :companyId
        AND s.endTs IS NULL
        ORDER BY s.startTs DESC
        """)
    Optional<Shift> findCurrentShift(
        @Param("userId") Long userId, 
        @Param("companyId") Long companyId
    );
}
