package com.meoning.benefit.repository;

import com.meoning.TestcontainersConfiguration;
import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.BenefitStatus;
import com.meoning.benefit.support.BenefitTestDataHelper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
public class BenefitRepositoryQueryTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 14);
    private static final Sort DEADLINE_SORT = Sort.by(Sort.Order.asc("endDate"), Sort.Order.asc("id"));

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private EntityManager entityManager;

    private BenefitTestDataHelper dataHelper;

    @BeforeEach
    void setup() {
        dataHelper = new BenefitTestDataHelper(entityManager);
    }

    @Test
    @DisplayName("공개되고 삭제되지 않은 혜택만 조회된다")
    void visible_returns_only_published_and_not_deleted() {
        // given
        dataHelper.createPublished("공개 혜택", BenefitCategory.FINANCE, TODAY.plusDays(5));
        dataHelper.create("임시저장 혜택", BenefitCategory.FINANCE, TODAY.plusDays(5), BenefitStatus.DRAFT);
        dataHelper.create("비공개 혜택", BenefitCategory.FINANCE, TODAY.plusDays(5), BenefitStatus.HIDDEN);
        Long deletedId = dataHelper.createPublished("삭제된 혜택", BenefitCategory.FINANCE, TODAY.plusDays(5));
        dataHelper.softDelete(deletedId);
        dataHelper.flushAndClear();

        // when
        List<Benefit> result = benefitRepository.findAll(BenefitSpecs.visible(), DEADLINE_SORT);

        // then
        assertThat(result).extracting(Benefit::getTitle).containsExactly("공개 혜택");
    }

    @Test
    @DisplayName("마감 전 조건은 오늘 마감과 상시 혜택을 포함하고 지난 혜택은 제외한다")
    void not_expired_includes_today_and_always_open() {
        // given
        dataHelper.createPublished("어제 마감", BenefitCategory.DISCOUNT, TODAY.minusDays(1));
        dataHelper.createPublished("오늘 마감", BenefitCategory.DISCOUNT, TODAY);
        dataHelper.createPublished("내일 마감", BenefitCategory.DISCOUNT, TODAY.plusDays(1));
        dataHelper.createPublished("상시 혜택", BenefitCategory.DISCOUNT, null);
        dataHelper.flushAndClear();

        Specification<Benefit> spec = BenefitSpecs.visible()
                .and(BenefitSpecs.notExpired(TODAY));

        // when
        List<Benefit> result = benefitRepository.findAll(spec, DEADLINE_SORT);

        // then
        assertThat(result).extracting(Benefit::getTitle)
                .containsExactly("오늘 마감", "내일 마감", "상시 혜택");
    }

    @Test
    @DisplayName("마감 임박순으로 정렬하면 상시 혜택은 맨 뒤에 온다")
    void deadline_sort_puts_always_open_last() {
        // given
        dataHelper.createPublished("상시 혜택", BenefitCategory.ETC, null);
        dataHelper.createPublished("7일 남음", BenefitCategory.ETC, TODAY.plusDays(7));
        dataHelper.createPublished("1일 남음", BenefitCategory.ETC, TODAY.plusDays(1));
        dataHelper.flushAndClear();

        // when
        List<Benefit> result = benefitRepository.findAll(BenefitSpecs.visible(), DEADLINE_SORT);

        // then
        assertThat(result).extracting(Benefit::getTitle)
                .containsExactly("1일 남음", "7일 남음", "상시 혜택");
    }

    @Test
    @DisplayName("카테고리와 키워드 조건을 함께 걸면 둘 다 만족하는 혜택만 조회된다")
    void category_and_keyword_filter_together() {
        // given
        dataHelper.createPublished("청년 미래적금", BenefitCategory.FINANCE, TODAY.plusDays(3));
        dataHelper.createPublished("파킹통장 금리 비교", BenefitCategory.FINANCE, TODAY.plusDays(3));
        dataHelper.createPublished("적금 우대 쿠폰", BenefitCategory.DISCOUNT, TODAY.plusDays(3));
        dataHelper.flushAndClear();

        Specification<Benefit> spec = BenefitSpecs.visible()
                .and(BenefitSpecs.categoryIn(Set.of(BenefitCategory.FINANCE)))
                .and(BenefitSpecs.keywordContains("적금"));

        // when
        List<Benefit> result = benefitRepository.findAll(spec, DEADLINE_SORT);

        // then
        assertThat(result).extracting(Benefit::getTitle).containsExactly("청년 미래적금");
    }

    @Test
    @DisplayName("캘린더 조건은 해당 기간에 마감하는 혜택만 조회하고 상시 혜택은 제외한다")
    void end_date_between_returns_only_in_range() {
        // given
        dataHelper.createPublished("10월 마감", BenefitCategory.SUPPORT, LocalDate.of(2026, 10, 31));
        dataHelper.createPublished("11월 1일 마감", BenefitCategory.SUPPORT, LocalDate.of(2026, 11, 1));
        dataHelper.createPublished("11월 30일 마감", BenefitCategory.SUPPORT, LocalDate.of(2026, 11, 30));
        dataHelper.createPublished("12월 마감", BenefitCategory.SUPPORT, LocalDate.of(2026, 12, 1));
        dataHelper.createPublished("상시 혜택", BenefitCategory.SUPPORT, null);
        dataHelper.flushAndClear();

        Specification<Benefit> spec = BenefitSpecs.visible()
                .and(BenefitSpecs.endDateBetween(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30)));

        // when
        List<Benefit> result = benefitRepository.findAll(spec, DEADLINE_SORT);

        // then
        assertThat(result).extracting(Benefit::getTitle)
                .containsExactly("11월 1일 마감", "11월 30일 마감");
    }

    @Test
    @DisplayName("공개 혜택 상세는 조회되고 임시저장 혜택은 조회되지 않는다")
    void find_visible_by_id() {
        // given
        Long publishedId = dataHelper.createPublished("공개 혜택", BenefitCategory.ETC, TODAY.plusDays(3));
        Long draftId = dataHelper.create("임시저장 혜택", BenefitCategory.ETC, TODAY.plusDays(3), BenefitStatus.DRAFT);
        dataHelper.flushAndClear();

        // when
        Optional<Benefit> published = benefitRepository.findByIdAndStatusAndDeletedAtIsNull(publishedId, BenefitStatus.PUBLISHED);
        Optional<Benefit> draft = benefitRepository.findByIdAndStatusAndDeletedAtIsNull(draftId, BenefitStatus.PUBLISHED);

        // then
        assertThat(published).isPresent();
        assertThat(draft).isEmpty();
    }

    @Test
    @DisplayName("공개 혜택의 조회수는 1 증가하고 임시저장 혜택은 변경되지 않는다")
    void increase_view_count_only_for_visible() {
        // given
        Long publishedId = dataHelper.createPublished("공개 혜택", BenefitCategory.ETC, TODAY.plusDays(3));
        Long draftId = dataHelper.create("임시저장 혜택", BenefitCategory.ETC, TODAY.plusDays(3), BenefitStatus.DRAFT);
        dataHelper.flushAndClear();

        // when
        int publishedUpdated = benefitRepository.increaseViewCount(publishedId);
        int draftUpdated = benefitRepository.increaseViewCount(draftId);
        dataHelper.flushAndClear();

        // then
        assertThat(publishedUpdated).isEqualTo(1);
        assertThat(draftUpdated).isZero();
        assertThat(benefitRepository.findById(publishedId).orElseThrow().getViewCount()).isEqualTo(1);
        assertThat(benefitRepository.findById(draftId).orElseThrow().getViewCount()).isZero();
    }
}