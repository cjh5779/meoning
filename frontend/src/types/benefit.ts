export type BenefitCategory = 'SUPPORT' | 'DISCOUNT' | 'FINANCE' | 'ETC'

export type BenefitSort = 'DEADLINE' | 'LATEST' | 'POPULAR'

export type BenefitCard = {
    benefitId: number
    title: string
    category: BenefitCategory
    summary: string
    startDate: string | null
    endDate: string | null
    dDay: number | null
    deadlineSoon: boolean
    thumbnailUrl: string | null
    bookmarkCount: number
}

export type TargetCondition = {
    ageMin: number | null
    ageMax: number | null
    regions: string[]
    incomeLimit: boolean
}

export type BenefitDetail = BenefitCard & {
    expired: boolean
    targetCondition: TargetCondition
    applyMethod: string | null
    officialUrl: string
    instaUrl: string | null
}

export type BenefitListParams = {
    category?: BenefitCategory[]
    sort?: BenefitSort
    keyword?: string
    includeExpired?: boolean
    page?: number
    size?: number
}