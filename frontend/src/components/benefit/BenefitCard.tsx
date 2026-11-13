import { Link } from 'react-router'
import CategoryBadge from './CategoryBadge'
import DdayBadge from './DdayBadge'
import { formatPeriod } from '../../utils/date'
import type { BenefitCard as BenefitCardType } from '../../types/benefit'

type BenefitCardProps = {
    benefit: BenefitCardType
}

export default function BenefitCard({ benefit }: BenefitCardProps) {
    return (
        <Link
            to={`/benefits/${benefit.benefitId}`}
            className="flex gap-3 rounded-xl border border-gray-200 p-3 active:bg-gray-50"
        >
            <div className="h-20 w-20 shrink-0 overflow-hidden rounded-lg bg-gray-100">
                {benefit.thumbnailUrl && (
                    <img src={benefit.thumbnailUrl} alt="" className="h-full w-full object-cover" />
                )}
            </div>

            <div className="min-w-0 flex-1">
                <div className="flex items-center gap-1.5">
                    <CategoryBadge category={benefit.category} />
                    <DdayBadge dDay={benefit.dDay} deadlineSoon={benefit.deadlineSoon} />
                </div>
                <h2 className="mt-1.5 line-clamp-2 font-bold leading-snug">{benefit.title}</h2>
                <p className="mt-1 line-clamp-2 text-sm text-gray-500">{benefit.summary}</p>
                <p className="mt-1 text-xs text-gray-400">{formatPeriod(benefit.startDate, benefit.endDate)}</p>
            </div>
        </Link>
    )
}