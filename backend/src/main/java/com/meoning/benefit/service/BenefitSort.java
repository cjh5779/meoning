package com.meoning.benefit.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum BenefitSort {

    DEADLINE(Sort.by(Sort.Order.asc("endDate"), Sort.Order.asc("id"))),
    LATEST(Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"))),
    POPULAR(Sort.by(Sort.Order.desc("bookmarkCount"), Sort.Order.desc("id")));

    private final Sort sort;
}