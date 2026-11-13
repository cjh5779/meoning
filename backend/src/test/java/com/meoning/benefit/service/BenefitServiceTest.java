package com.meoning.benefit.service;

import com.meoning.benefit.dto.response.BenefitCalendarResponseDTO;
import com.meoning.benefit.dto.response.BenefitCardResponseDTO;
import com.meoning.benefit.dto.response.BenefitDetailResponseDTO;
import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.BenefitStatus;
import com.meoning.benefit.repository.BenefitRepository;
import com.meoning.common.error.BusinessException;
import com.meoning.common.error.ErrorCode;
import com.meoning.common.response.PageResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

public class BenefitServiceTest {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final Instant NOW = Instant.parse("2026-11-14T03:00:00Z");
    private static final LocalDate TODAY = LocalDate.of(2026, 11, 14);
    private static final Long BENEFIT_ID = 1L;

    private BenefitRepository benefitRepository;
    private BenefitService service;

    @BeforeEach
    void setup() {
        benefitRepository = mock(BenefitRepository.class);
        service = new BenefitService(benefitRepository, Clock.fixed(NOW, SEOUL));
    }

    private Benefit benefitEndingAt(Long id, String title, LocalDate endDate) {
        Benefit benefit = Benefit.create(
                title,
                BenefitCategory.FINANCE,
                title + " 요약",
                null,
                endDate,
                null,
                null,
                "https://example.com/benefit",
                null,
                null,
                BenefitStatus.PUBLISHED
        );
        ReflectionTestUtils.setField(benefit, "id", id);

        return benefit;
    }

    @Test
    @DisplayName("목록 조회는 요청한 정렬로 조회하고 오늘 기준 D-day를 계산해 페이지로 반환한다")
    void get_benefits_returns_page_with_d_day() {
        // given
        Benefit benefit = benefitEndingAt(BENEFIT_ID, "청년 미래적금", TODAY.plusDays(2));
        given(benefitRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(benefit), PageRequest.of(0, 20), 1));

        // when
        PageResponseDTO<BenefitCardResponseDTO> result =
                service.getBenefits(Set.of(), BenefitSort.DEADLINE, null, false, 0, 20);

        // then
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        then(benefitRepository).should().findAll(any(Specification.class), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getSort()).isEqualTo(BenefitSort.DEADLINE.getSort());

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getDDay()).isEqualTo(2);
        assertThat(result.getItems().get(0).isDeadlineSoon()).isTrue();
    }

    @Test
    @DisplayName("캘린더 조회는 혜택을 마감일별로 묶고 날짜 순서를 유지한다")
    void get_calendar_groups_by_end_date() {
        // given
        LocalDate nov15 = LocalDate.of(2026, 11, 15);
        LocalDate nov20 = LocalDate.of(2026, 11, 20);
        given(benefitRepository.findAll(any(Specification.class), any(Sort.class)))
                .willReturn(List.of(
                        benefitEndingAt(1L, "15일 마감", nov15),
                        benefitEndingAt(2L, "20일 마감 A", nov20),
                        benefitEndingAt(3L, "20일 마감 B", nov20)
                ));

        // when
        BenefitCalendarResponseDTO result = service.getCalendar(YearMonth.of(2026, 11), Set.of());

        // then
        assertThat(result.getYearMonth()).isEqualTo("2026-11");
        assertThat(result.getDays()).hasSize(2);
        assertThat(result.getDays().get(0).getDate()).isEqualTo(nov15);
        assertThat(result.getDays().get(0).getCount()).isEqualTo(1);
        assertThat(result.getDays().get(1).getDate()).isEqualTo(nov20);
        assertThat(result.getDays().get(1).getCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("상세 조회는 조회수를 올린 뒤 혜택 상세를 반환한다")
    void get_benefit_increases_view_count_and_returns_detail() {
        // given
        Benefit benefit = benefitEndingAt(BENEFIT_ID, "청년 미래적금", TODAY.minusDays(1));
        given(benefitRepository.increaseViewCount(BENEFIT_ID)).willReturn(1);
        given(benefitRepository.findByIdAndStatusAndDeletedAtIsNull(BENEFIT_ID, BenefitStatus.PUBLISHED))
                .willReturn(Optional.of(benefit));

        // when
        BenefitDetailResponseDTO result = service.getBenefit(BENEFIT_ID);

        // then
        then(benefitRepository).should().increaseViewCount(BENEFIT_ID);
        assertThat(result.getBenefitId()).isEqualTo(BENEFIT_ID);
        assertThat(result.isExpired()).isTrue();
        assertThat(result.getDDay()).isEqualTo(-1);
    }

    @Test
    @DisplayName("공개 혜택이 아니면 조회수를 올리지 못하고 BENEFIT_NOT_FOUND를 던진다")
    void get_benefit_not_visible_throws_not_found() {
        // given
        given(benefitRepository.increaseViewCount(BENEFIT_ID)).willReturn(0);

        // when
        Throwable thrown = catchThrowable(() -> service.getBenefit(BENEFIT_ID));

        // then
        assertThat(thrown).isInstanceOf(BusinessException.class);
        assertThat(((BusinessException) thrown).getErrorCode()).isEqualTo(ErrorCode.BENEFIT_NOT_FOUND);
        then(benefitRepository).should(never()).findByIdAndStatusAndDeletedAtIsNull(any(), any());
    }
}