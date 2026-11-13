import type { TargetCondition } from '../types/benefit'

function formatAge(ageMin: number | null, ageMax: number | null): string | null {
    if (ageMin !== null && ageMax !== null) {
        return `만 ${ageMin}~${ageMax}세`
    }
    if (ageMin !== null) {
        return `만 ${ageMin}세 이상`
    }
    if (ageMax !== null) {
        return `만 ${ageMax}세 이하`
    }

    return null
}

export function formatTargetCondition(condition: TargetCondition): string {
    const parts = [
        formatAge(condition.ageMin, condition.ageMax),
        condition.regions.length > 0 ? condition.regions.join(', ') : null,
        condition.incomeLimit ? '소득 기준 있음' : null,
    ].filter((part): part is string => part !== null)

    return parts.length > 0 ? parts.join(' · ') : '누구나'
}