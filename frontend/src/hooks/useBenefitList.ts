import { keepPreviousData, useInfiniteQuery } from '@tanstack/react-query'
import { fetchBenefits } from '../api/benefits'
import type { BenefitListParams } from '../types/benefit'

const PAGE_SIZE = 20

export function useBenefitList(params: Omit<BenefitListParams, 'page' | 'size'>) {
    return useInfiniteQuery({
        queryKey: ['benefits', params],
        queryFn: ({ pageParam }) => fetchBenefits({ ...params, page: pageParam, size: PAGE_SIZE }),
        initialPageParam: 0,
        getNextPageParam: (lastPage) =>
            lastPage.page + 1 < lastPage.totalPages ? lastPage.page + 1 : undefined,
        placeholderData: keepPreviousData,
    })
}