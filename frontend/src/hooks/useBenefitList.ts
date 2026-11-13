import { keepPreviousData, useQuery } from '@tanstack/react-query'
import { fetchBenefits } from '../api/benefits'
import type { BenefitListParams } from '../types/benefit'

export function useBenefitList(params: BenefitListParams) {
    return useQuery({
        queryKey: ['benefits', params],
        queryFn: () => fetchBenefits(params),
        placeholderData: keepPreviousData,
    })
}