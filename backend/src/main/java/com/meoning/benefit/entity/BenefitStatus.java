package com.meoning.benefit.entity;

// 혜택 공개 상태
public enum BenefitStatus {
    DRAFT,      // 임시저장: 관리자만 보임. 제목만 있어도 저장 가능
    PUBLISHED,  // 공개: 사용자 화면에 노출, 알림 대상
    HIDDEN      // 비공개: 한 번 공개됐다가 내린 상태. 사용자 화면·알림에서 제외
}
