package com.meoning.benefit.service;

import com.meoning.benefit.dto.response.BenefitCalendarResponseDTO;
import com.meoning.benefit.dto.response.BenefitCardResponseDTO;
import com.meoning.benefit.dto.response.BenefitDetailResponseDTO;
import com.meoning.benefit.dto.response.CalendarDayResponseDTO;
import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.BenefitStatus;
import com.meoning.benefit.repository.BenefitRepository;
import com.meoning.benefit.repository.BenefitSpecs;
import com.meoning.common.error.BusinessException;
import com.meoning.common.error.ErrorCode;
import com.meoning.common.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BenefitService {

    private final BenefitRepository benefitRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public PageResponseDTO<BenefitCardResponseDTO> getBenefits(Set<BenefitCategory> categories,
                                                               BenefitSort sort,
                                                               String keyword,
                                                               boolean includeExpired,
                                                               int page,
                                                               int size) {
        LocalDate today = LocalDate.now(clock);

        Specification<Benefit> spec = withCategories(BenefitSpecs.visible(), categories);
        if (!includeExpired) {
            spec = spec.and(BenefitSpecs.notExpired(today));
        }
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(BenefitSpecs.keywordContains(keyword.trim()));
        }

        Page<BenefitCardResponseDTO> result = benefitRepository
                .findAll(spec, PageRequest.of(page, size, sort.getSort()))
                .map(benefit -> BenefitCardResponseDTO.from(benefit, today));

        return PageResponseDTO.from(result);
    }

    @Transactional(readOnly = true)
    public BenefitCalendarResponseDTO getCalendar(YearMonth yearMonth, Set<BenefitCategory> categories) {
        LocalDate today = LocalDate.now(clock);

        Specification<Benefit> spec = withCategories(
                BenefitSpecs.visible().and(BenefitSpecs.endDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth())),
                categories
        );

        Map<LocalDate, List<BenefitCardResponseDTO>> benefitsByDate = benefitRepository
                .findAll(spec, BenefitSort.DEADLINE.getSort())
                .stream()
                .collect(Collectors.groupingBy(
                        Benefit::getEndDate,
                        LinkedHashMap::new,
                        Collectors.mapping(benefit -> BenefitCardResponseDTO.from(benefit, today), Collectors.toList())
                ));

        List<CalendarDayResponseDTO> days = benefitsByDate.entrySet().stream()
                .map(entry -> CalendarDayResponseDTO.builder()
                        .date(entry.getKey())
                        .count(entry.getValue().size())
                        .items(entry.getValue())
                        .build())
                .toList();

        return BenefitCalendarResponseDTO.builder()
                .yearMonth(yearMonth.toString())
                .days(days)
                .build();
    }

    @Transactional
    public BenefitDetailResponseDTO getBenefit(Long benefitId) {
        int updated = benefitRepository.increaseViewCount(benefitId);
        if (updated == 0) {
            throw new BusinessException(ErrorCode.BENEFIT_NOT_FOUND);
        }

        Benefit benefit = benefitRepository
                .findByIdAndStatusAndDeletedAtIsNull(benefitId, BenefitStatus.PUBLISHED)
                .orElseThrow(() -> new BusinessException(ErrorCode.BENEFIT_NOT_FOUND));

        return BenefitDetailResponseDTO.from(benefit, LocalDate.now(clock));
    }

    private Specification<Benefit> withCategories(Specification<Benefit> spec, Set<BenefitCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            return spec;
        }

        return spec.and(BenefitSpecs.categoryIn(categories));
    }
}