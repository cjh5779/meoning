import { apiClient } from './client'
import type { ApiResponse, PageResponse } from '../types/api'
import type { BenefitCard, BenefitDetail, BenefitListParams } from '../types/benefit'

export async function fetchBenefits(params: BenefitListParams): Promise<PageResponse<BenefitCard>> {
    const response = await apiClient.get<ApiResponse<PageResponse<BenefitCard>>>('/benefits', {
        params: {
            ...params,
            category: params.category?.length ? params.category.join(',') : undefined,
            keyword: params.keyword?.trim() || undefined,
        },
    })

    return response.data.data
}

export async function fetchBenefit(benefitId: number): Promise<BenefitDetail> {
    const response = await apiClient.get<ApiResponse<BenefitDetail>>(`/benefits/${benefitId}`)

    return response.data.data
}