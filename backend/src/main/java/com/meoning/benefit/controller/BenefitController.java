package com.meoning.benefit.controller;

import com.meoning.benefit.dto.response.BenefitCalendarResponseDTO;
import com.meoning.benefit.dto.response.BenefitCardResponseDTO;
import com.meoning.benefit.dto.response.BenefitDetailResponseDTO;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.service.BenefitService;
import com.meoning.benefit.service.BenefitSort;
import com.meoning.common.response.PageResponseDTO;
import com.meoning.common.response.ResponseDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class BenefitController {

    private final BenefitService benefitService;

    @GetMapping("/benefits")
    public ResponseEntity<ResponseDTO<PageResponseDTO<BenefitCardResponseDTO>>> getBenefits(
            @RequestParam(required = false) Set<BenefitCategory> category,
            @RequestParam(defaultValue = "DEADLINE") BenefitSort sort,
            @RequestParam(required = false) @Size(min = 2, max = 50) String keyword,
            @RequestParam(defaultValue = "false") boolean includeExpired,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(50) int size
    ) {
        PageResponseDTO<BenefitCardResponseDTO> response =
                benefitService.getBenefits(category, sort, keyword, includeExpired, page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.success("조회 성공", response));
    }

    @GetMapping("/benefits/calendar")
    public ResponseEntity<ResponseDTO<BenefitCalendarResponseDTO>> getCalendar(
            @RequestParam YearMonth yearMonth,
            @RequestParam(required = false) Set<BenefitCategory> category
    ) {
        BenefitCalendarResponseDTO response = benefitService.getCalendar(yearMonth, category);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.success("조회 성공", response));
    }

    @GetMapping("/benefits/{benefitId}")
    public ResponseEntity<ResponseDTO<BenefitDetailResponseDTO>> getBenefit(
            @PathVariable Long benefitId
    ) {
        BenefitDetailResponseDTO response = benefitService.getBenefit(benefitId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.success("조회 성공", response));
    }
}