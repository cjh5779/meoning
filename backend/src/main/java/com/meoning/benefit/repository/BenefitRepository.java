package com.meoning.benefit.repository;

import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BenefitRepository extends JpaRepository<Benefit, Long>, JpaSpecificationExecutor<Benefit> {

    Optional<Benefit> findByIdAndStatusAndDeletedAtIsNull(Long id, BenefitStatus status);

    @Modifying
    @Query(value = """
            UPDATE benefits
            SET view_count = view_count + 1
            WHERE id = :id
              AND status = 'PUBLISHED'
              AND deleted_at IS NULL
            """, nativeQuery = true)
    int increaseViewCount(@Param("id") Long id);
}