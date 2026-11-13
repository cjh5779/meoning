package com.meoning.benefit.entity;

import java.util.List;

public record TargetCondition(
        Integer ageMin,          // 최소 나이. null이면 제한 없음
        Integer ageMax,          // 최대 나이. null이면 제한 없음
        List<String> regions,    // 대상 지역(시·도). 빈 목록이면 전국
        boolean incomeLimit      // 소득 기준이 있는지
) {
    
    public TargetCondition {
        regions = (regions == null) ? List.of() : List.copyOf(regions);
    }

    // "조건 없음(누구나)"을 만드는 정적 메서드
    public static TargetCondition none() {
        return new TargetCondition(null, null, List.of(), false);
    }

    // 아무 조건도 없는지 확인
    public boolean hasNoRestriction() {
        return ageMin == null
                && ageMax == null
                && regions.isEmpty()
                && !incomeLimit;
    }
}
