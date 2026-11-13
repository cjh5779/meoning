package com.meoning.benefit.repository;

import com.meoning.TestcontainersConfiguration;
import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.BenefitStatus;
import com.meoning.benefit.entity.SourceType;
import com.meoning.benefit.entity.TargetCondition;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class BenefitRepositoryTest {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private EntityManager entityManager;

    private TargetCondition condition;

    @BeforeEach
    void setup() {
        condition = new TargetCondition(19, 34, List.of("서울특별시"), true);
    }

    private Benefit publishedBenefit() {
        return Benefit.create(
                "청년 미래적금, 이번주 금요일 마감",
                BenefitCategory.FINANCE,
                "월 최대 50만 원 납입 시 정부 기여금 지원",
                null,
                LocalDate.of(2026, 10, 30),
                condition,
                "은행 앱에서 신청",
                "https://example.com/youth-saving",
                null,
                null,
                BenefitStatus.PUBLISHED
        );
    }

    private Benefit findAfterClear(Long id) {
        entityManager.flush();
        entityManager.clear();
        return benefitRepository.findById(id).orElseThrow();
    }

    @Test
    @DisplayName("혜택을 저장하면 대상 조건, 기본값, 생성 시각이 함께 저장된다")
    void save() {
        // given
        Benefit benefit = publishedBenefit();

        // when
        Benefit saved = benefitRepository.save(benefit);
        Benefit found = findAfterClear(saved.getId());

        // then
        assertThat(found.getTitle()).isEqualTo("청년 미래적금, 이번주 금요일 마감");
        assertThat(found.getStatus()).isEqualTo(BenefitStatus.PUBLISHED);
        assertThat(found.getSourceType()).isEqualTo(SourceType.MANUAL);
        assertThat(found.getTargetCondition()).isEqualTo(condition);
        assertThat(found.getViewCount()).isZero();
        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("상태와 대상 조건을 주지 않으면 임시저장, 조건 없음으로 저장된다")
    void saveWithDefaults() {
        // given
        Benefit benefit = Benefit.create(
                "[다음 주 혜택 제목]",
                null, null, null, null, null, null, null, null, null, null
        );

        // when
        Benefit saved = benefitRepository.save(benefit);
        Benefit found = findAfterClear(saved.getId());

        // then
        assertThat(found.getStatus()).isEqualTo(BenefitStatus.DRAFT);
        assertThat(found.getTargetCondition().hasNoRestriction()).isTrue();
    }
}