import { CATEGORY_LABELS } from '../../constants/benefit'
import type { BenefitCategory } from '../../types/benefit'

type CategoryBadgeProps = {
    category: BenefitCategory
}

export default function CategoryBadge({ category }: CategoryBadgeProps) {
    return (
        <span className="rounded bg-gray-100 px-1.5 py-0.5 text-xs text-gray-600">
      {CATEGORY_LABELS[category]}
    </span>
    )
}