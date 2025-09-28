package com.hugbo.clock_in.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hugbo.clock_in.domain.entity.ShiftTask;

public interface ShiftTaskRepository extends JpaRepository<ShiftTask, Long> {
    @Query("""
        SELECT s FROM ShiftTask s
        WHERE s.shift.id = :shiftId
        AND s.endTs IS NULL
        ORDER BY s.startTs DESC
        """)
    Optional<ShiftTask> findOngoingByShift(@Param("shiftId") Long shiftId);
}
