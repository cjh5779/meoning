import type { BenefitCategory, BenefitSort } from '../types/benefit'

export const CATEGORY_LABELS: Record<BenefitCategory, string> = {
    SUPPORT: '지원금',
    DISCOUNT: '할인',
    FINANCE: '금융',
    ETC: '기타',
}

export const CATEGORIES: BenefitCategory[] = ['SUPPORT', 'DISCOUNT', 'FINANCE', 'ETC']

export const SORT_LABELS: Record<BenefitSort, string> = {
    DEADLINE: '마감 임박순',
    LATEST: '최신 등록순',
    POPULAR: '인기순',
}