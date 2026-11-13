package com.meoning.benefit.repository;

import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.BenefitStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Set;

public final class BenefitSpecs {

    private BenefitSpecs() {

    }

    public static Specification<Benefit> visible() {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), BenefitStatus.PUBLISHED),
                cb.isNull(root.get("deletedAt"))
        );
    }

    public static Specification<Benefit> notExpired(LocalDate today) {
        return (root, query, cb) -> cb.or(
                cb.isNull(root.get("endDate")),
                cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), today)
        );
    }

    public static Specification<Benefit> categoryIn(Set<BenefitCategory> categories) {
        return (root, query, cb) -> root.get("category").in(categories);
    }

    public static Specification<Benefit> keywordContains(String keyword) {
        String pattern = "%" + keyword.toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.<String>get("title")), pattern),
                cb.like(cb.lower(root.<String>get("summary")), pattern)
        );
    }

    public static Specification<Benefit> endDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> cb.between(root.<LocalDate>get("endDate"), from, to);
    }
}
