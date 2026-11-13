package com.meoning.benefit.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitCardResponseDTO {
    private Long benefitId;
    private String title;
    private BenefitCategory category;
    private String summary;
    private LocalDate startDate;
    private LocalDate endDate;
    @JsonProperty("dDay")
    private Integer dDay;
    private boolean deadlineSoon;
    private String thumbnailUrl;
    private long bookmarkCount;

    public static BenefitCardResponseDTO from(Benefit benefit, LocalDate today) {
        return BenefitCardResponseDTO.builder()
                .benefitId(benefit.getId())
                .title(benefit.getTitle())
                .category(benefit.getCategory())
                .summary(benefit.getSummary())
                .startDate(benefit.getStartDate())
                .endDate(benefit.getEndDate())
                .dDay(benefit.dDay(today))
                .deadlineSoon(benefit.isDeadlineSoon(today))
                .thumbnailUrl(benefit.getThumbnailUrl())
                .bookmarkCount(benefit.getBookmarkCount())
                .build();
    }
}