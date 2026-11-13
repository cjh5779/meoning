const WEEKDAYS = ['일', '월', '화', '수', '목', '금', '토']

function toLocalDate(date: string): Date {
    const [year, month, day] = date.split('-').map(Number)

    return new Date(year, month - 1, day)
}

export function formatMonthDay(date: string): string {
    const [, month, day] = date.split('-')

    return `${month}.${day}`
}

export function formatMonthDayWithWeekday(date: string): string {
    return `${formatMonthDay(date)} (${WEEKDAYS[toLocalDate(date).getDay()]})`
}

export function formatPeriod(startDate: string | null, endDate: string | null): string {
    if (startDate && endDate) {
        return `${formatMonthDay(startDate)} ~ ${formatMonthDay(endDate)}`
    }
    if (endDate) {
        return `마감 ${formatMonthDayWithWeekday(endDate)}`
    }

    return '상시'
}