type DdayBadgeProps = {
    dDay: number | null
    deadlineSoon: boolean
}

function toLabel(dDay: number | null): string {
    if (dDay === null) {
        return '상시'
    }
    if (dDay === 0) {
        return 'D-Day'
    }
    if (dDay < 0) {
        return '마감'
    }

    return `D-${dDay}`
}

export default function DdayBadge({ dDay, deadlineSoon }: DdayBadgeProps) {
    const color = deadlineSoon
        ? 'bg-red-500 text-white'
        : dDay === null || dDay < 0
            ? 'bg-gray-200 text-gray-600'
            : 'bg-gray-900 text-white'

    return (
        <span className={`rounded px-1.5 py-0.5 text-xs font-bold ${color}`}>
      {toLabel(dDay)}
    </span>
    )
}