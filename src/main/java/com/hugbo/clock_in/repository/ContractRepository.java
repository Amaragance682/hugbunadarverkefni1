package com.hugbo.clock_in.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hugbo.clock_in.domain.entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByUserId(Long userId);

    @Query("""
        SELECT COUNT(c) > 0 FROM Contract c WHERE 
        c.user.id = :userId AND c.company.id = :companyId
        """)
    boolean hasContractByUserIdAndCompanyId(
            @Param("userId") Long userId,
            @Param("companyId") Long companyId);

    @Query("""
        SELECT c FROM Contract c 
        WHERE c.user.id = :userId AND c.company.id = :companyId
        """)
    Optional<Contract> findByUserIdAndCompanyId(
            @Param("userId") Long userId,
            @Param("companyId") Long companyId);
}
