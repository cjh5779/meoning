package com.meoning.benefit.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meoning.benefit.entity.Benefit;
import com.meoning.benefit.entity.BenefitCategory;
import com.meoning.benefit.entity.TargetCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitDetailResponseDTO {
    private Long benefitId;
    private String title;
    private BenefitCategory category;
    private String summary;
    private LocalDate startDate;
    private LocalDate endDate;
    @JsonProperty("dDay")
    private Integer dDay;
    private boolean deadlineSoon;
    private boolean expired;
    private TargetCondition targetCondition;
    private String applyMethod;
    private String officialUrl;
    private String instaUrl;
    private String thumbnailUrl;
    private long bookmarkCount;

    public static BenefitDetailResponseDTO from(Benefit benefit, LocalDate today) {
        return BenefitDetailResponseDTO.builder()
                .benefitId(benefit.getId())
                .title(benefit.getTitle())
                .category(benefit.getCategory())
                .summary(benefit.getSummary())
                .startDate(benefit.getStartDate())
                .endDate(benefit.getEndDate())
                .dDay(benefit.dDay(today))
                .deadlineSoon(benefit.isDeadlineSoon(today))
                .expired(benefit.isExpired(today))
                .targetCondition(benefit.getTargetCondition())
                .applyMethod(benefit.getApplyMethod())
                .officialUrl(benefit.getOfficialUrl())
                .instaUrl(benefit.getInstaUrl())
                .thumbnailUrl(benefit.getThumbnailUrl())
                .bookmarkCount(benefit.getBookmarkCount())
                .build();
    }
}