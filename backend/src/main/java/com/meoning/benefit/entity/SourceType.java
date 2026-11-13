package com.meoning.benefit.entity;

// 혜택 등록 출처
public enum SourceType {
    MANUAL,     // 관리자가 직접 등록 (MVP에서는 전부 이것)
    PUBLIC_API  // 공공데이터 API로 자동 수집 (2단계 로드맵용으로 미리 자리만 마련)
}