import { useState } from 'react'
import BenefitCard from '../components/benefit/BenefitCard'
import CategoryChips from '../components/benefit/CategoryChips'
import { SORT_LABELS } from '../constants/benefit'
import { useBenefitList } from '../hooks/useBenefitList'
import type { BenefitCategory, BenefitSort } from '../types/benefit'

export default function HomePage() {
    const [categories, setCategories] = useState<BenefitCategory[]>([])
    const [sort, setSort] = useState<BenefitSort>('DEADLINE')

    const { data, isPending, isError, refetch } = useBenefitList({ category: categories, sort })

    return (
        <main className="px-4 py-5">
            <h1 className="text-xl font-bold">머닝</h1>

            <div className="mt-4">
                <CategoryChips selected={categories} onChange={setCategories} />
            </div>

            <div className="mt-4 flex items-center justify-between">
                <p className="text-sm text-gray-500">
                    진행 중인 혜택 {data ? data.totalElements : 0}건
                </p>
                <select
                    value={sort}
                    onChange={(event) => setSort(event.target.value as BenefitSort)}
                    className="text-sm text-gray-700"
                >
                    {Object.entries(SORT_LABELS).map(([value, label]) => (
                        <option key={value} value={value}>
                            {label}
                        </option>
                    ))}
                </select>
            </div>

            <section className="mt-3">
                {isPending && <p className="py-10 text-center text-sm text-gray-400">불러오는 중...</p>}

                {isError && (
                    <div className="py-10 text-center">
                        <p className="text-sm text-gray-500">혜택을 불러오지 못했어요.</p>
                        <button
                            type="button"
                            onClick={() => refetch()}
                            className="mt-3 rounded-full border border-gray-300 px-4 py-1.5 text-sm"
                        >
                            다시 시도
                        </button>
                    </div>
                )}

                {data && data.items.length === 0 && (
                    <p className="py-10 text-center text-sm text-gray-400">조건에 맞는 혜택이 없어요.</p>
                )}

                {data && data.items.length > 0 && (
                    <ul className="flex flex-col gap-3">
                        {data.items.map((benefit) => (
                            <li key={benefit.benefitId}>
                                <BenefitCard benefit={benefit} />
                            </li>
                        ))}
                    </ul>
                )}
            </section>
        </main>
    )
}