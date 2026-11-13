package com.meoning.benefit.support;

import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.BenefitStatus;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class BenefitTestDataHelper {

    public static final String OFFICIAL_URL = "https://example.com/benefit";

    private final EntityManager entityManager;

    public BenefitTestDataHelper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Long createPublished(String title, BenefitCategory category, LocalDate endDate) {
        return create(title, category, endDate, BenefitStatus.PUBLISHED);
    }

    public Long create(String title, BenefitCategory category, LocalDate endDate, BenefitStatus status) {
        Benefit benefit = Benefit.create(
                title,
                category,
                title + " 요약",
                null,
                endDate,
                null,
                null,
                OFFICIAL_URL,
                null,
                null,
                status
        );
        entityManager.persist(benefit);

        return benefit.getId();
    }

    public void softDelete(Long benefitId) {
        entityManager.createNativeQuery("UPDATE benefits SET deleted_at = now() WHERE id = :id")
                .setParameter("id", benefitId)
                .executeUpdate();
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
