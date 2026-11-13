package com.meoning.benefit.entity;

import com.meoning.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "benefits")
public class Benefit extends BaseEntity {

    private static final int DEADLINE_SOON_DAYS = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30)
    private BenefitCategory category;

    @Column(name = "summary", length = 500)
    private String summary;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_condition")
    private TargetCondition targetCondition;

    @Column(name = "apply_method")
    private String applyMethod;

    @Column(name = "official_url", length = 1000)
    private String officialUrl;

    @Column(name = "insta_url", length = 1000)
    private String instaUrl;

    @Column(name = "thumbnail_url", length = 1000)
    private String thumbnailUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BenefitStatus status = BenefitStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 30)
    private SourceType sourceType = SourceType.MANUAL;

    @Column(name = "view_count", nullable = false, insertable = false, updatable = false)
    private long viewCount;

    @Column(name = "bookmark_count", nullable = false, insertable = false, updatable = false)
    private long bookmarkCount;

    @Column(name = "click_count", nullable = false, insertable = false, updatable = false)
    private long clickCount;

    @Column(name = "publish_at")
    private Instant publishAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public static Benefit create(String title,
                                 BenefitCategory category,
                                 String summary,
                                 LocalDate startDate,
                                 LocalDate endDate,
                                 TargetCondition targetCondition,
                                 String applyMethod,
                                 String officialUrl,
                                 String instaUrl,
                                 String thumbnailUrl,
                                 BenefitStatus status) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("혜택 제목은 필수입니다.");
        }

        Benefit benefit = new Benefit();
        benefit.title = title;
        benefit.category = category;
        benefit.summary = summary;
        benefit.startDate = startDate;
        benefit.endDate = endDate;
        benefit.targetCondition = (targetCondition == null) ? TargetCondition.none() : targetCondition;
        benefit.applyMethod = applyMethod;
        benefit.officialUrl = officialUrl;
        benefit.instaUrl = instaUrl;
        benefit.thumbnailUrl = thumbnailUrl;
        if (status != null) {
            benefit.status = status;
        }
        return benefit;
    }

    public Integer dDay(LocalDate today) {
        if (endDate == null) {
            return null;
        }

        return (int) ChronoUnit.DAYS.between(today, endDate);
    }

    public boolean isDeadlineSoon(LocalDate today) {
        Integer dDay = dDay(today);

        return dDay != null && dDay >= 0 && dDay <=DEADLINE_SOON_DAYS;
    }

    public boolean isExpired(LocalDate today) {
        return endDate != null && endDate.isBefore(today);
    }
}