package com.meoning.benefit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BenefitTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 14);

    private Benefit benefitEndingAt(LocalDate endDate) {
        return Benefit.create(
                "테스트 혜택",
                BenefitCategory.ETC,
                "테스트 요약",
                null,
                endDate,
                null,
                null,
                "https://example.com/benefit",
                null,
                null,
                BenefitStatus.PUBLISHED
        );
    }

    @Test
    @DisplayName("마감일까지 남은 일수를 계산한다")
    void d_day_counts_days_until_end_date() {
        // given
        Benefit benefit = benefitEndingAt(TODAY.plusDays(2));

        // when
        Integer dDay = benefit.dDay(TODAY);

        // then
        assertThat(dDay).isEqualTo(2);
    }

    @Test
    @DisplayName("상시 혜택은 D-day가 없고 마감 임박도 마감 지남도 아니다")
    void always_open_has_no_d_day() {
        // given
        Benefit benefit = benefitEndingAt(null);

        // when, then
        assertThat(benefit.dDay(TODAY)).isNull();
        assertThat(benefit.isDeadlineSoon(TODAY)).isFalse();
        assertThat(benefit.isExpired(TODAY)).isFalse();
    }

    @Test
    @DisplayName("오늘부터 3일 남은 혜택까지 마감 임박이다")
    void deadline_soon_from_today_to_three_days() {
        // given
        Benefit today = benefitEndingAt(TODAY);
        Benefit threeDaysLeft = benefitEndingAt(TODAY.plusDays(3));
        Benefit fourDaysLeft = benefitEndingAt(TODAY.plusDays(4));

        // when, then
        assertThat(today.isDeadlineSoon(TODAY)).isTrue();
        assertThat(threeDaysLeft.isDeadlineSoon(TODAY)).isTrue();
        assertThat(fourDaysLeft.isDeadlineSoon(TODAY)).isFalse();
    }

    @Test
    @DisplayName("오늘 마감은 아직 마감 전이고 어제 마감은 마감 지남이다")
    void expired_only_after_end_date() {
        // given
        Benefit today = benefitEndingAt(TODAY);
        Benefit yesterday = benefitEndingAt(TODAY.minusDays(1));

        // when, then
        assertThat(today.isExpired(TODAY)).isFalse();
        assertThat(yesterday.isExpired(TODAY)).isTrue();
        assertThat(yesterday.isDeadlineSoon(TODAY)).isFalse();
        assertThat(yesterday.dDay(TODAY)).isEqualTo(-1);
    }
}