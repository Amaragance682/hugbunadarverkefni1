package com.hugbo.clock_in.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hugbo.clock_in.domain.entity.ShiftBreak;

public interface ShiftBreakRepository extends JpaRepository<ShiftBreak, Long> {
    @Query("""
        SELECT s FROM ShiftBreak s
        WHERE s.shift.id = :shiftId
        AND s.endTs IS NULL
        ORDER BY s.startTs DESC
        """)
    Optional<ShiftBreak> findOngoingByShift(@Param("shiftId") Long shiftId);

    @Query("""
        SELECT s FROM ShiftTask s
        WHERE s.shift.id = :shiftId
        ORDER BY s.startTs DESC
        """)
    List<ShiftBreak> findByShiftId(@Param("shiftId") Long shiftId);
}
