import { CATEGORIES, CATEGORY_LABELS } from '../../constants/benefit'
import type { BenefitCategory } from '../../types/benefit'

type CategoryChipsProps = {
    selected: BenefitCategory[]
    onChange: (selected: BenefitCategory[]) => void
}

function chipClass(active: boolean): string {
    return `shrink-0 rounded-full border px-3.5 py-1.5 text-sm ${
        active ? 'border-gray-900 bg-gray-900 text-white' : 'border-gray-300 bg-white text-gray-700'
    }`
}

export default function CategoryChips({ selected, onChange }: CategoryChipsProps) {
    const toggle = (category: BenefitCategory) => {
        onChange(
            selected.includes(category)
                ? selected.filter((item) => item !== category)
                : [...selected, category],
        )
    }

    return (
        <div className="flex gap-2 overflow-x-auto">
            <button type="button" className={chipClass(selected.length === 0)} onClick={() => onChange([])}>
                전체
            </button>
            {CATEGORIES.map((category) => (
                <button
                    key={category}
                    type="button"
                    className={chipClass(selected.includes(category))}
                    onClick={() => toggle(category)}
                >
                    {CATEGORY_LABELS[category]}
                </button>
            ))}
        </div>
    )
}