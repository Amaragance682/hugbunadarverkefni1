package com.hugbo.clock_in.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hugbo.clock_in.domain.entity.Shift;
import com.hugbo.clock_in.dto.filters.ShiftFilterDTO;

public interface ShiftRepository extends JpaRepository<Shift, Long>, JpaSpecificationExecutor<Shift> {
    @Query("""
        SELECT s FROM Shift s
        WHERE s.endTs IS NULL
        """)
    List<Shift> findOngoingShifts();

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
    @Query("""
        SELECT s FROM Shift s
        WHERE s.contract.company.id = :companyId
        """)
    List<Shift> findByCompanyId(
        @Param("companyId") Long companyId
    );

    @Query("""
        SELECT s FROM Shift s
        WHERE s.contract.company.id = :companyId
        AND s.endTs IS NULL
        """)
    List<Shift> findOngoingShiftsByCompany(
        @Param("companyId") Long companyId
    );
}
