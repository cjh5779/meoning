import { useQuery } from '@tanstack/react-query'
import { fetchBenefit } from '../api/benefits'
import { getApiErrorCode } from '../api/error'

export function useBenefitDetail(benefitId: number) {
    return useQuery({
        queryKey: ['benefit', benefitId],
        queryFn: () => fetchBenefit(benefitId),
        enabled: Number.isInteger(benefitId) && benefitId > 0,
        retry: (failureCount, error) =>
            getApiErrorCode(error) !== 'BENEFIT_NOT_FOUND' && failureCount < 1,
    })
}