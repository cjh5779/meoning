export type ApiResponse<T> = {
    code: string
    message: string
    data: T
}

export type FieldError = {
    field: string
    reason: string
}

export type ErrorDetails = {
    requestId: string
    serverTime: string
    fieldErrors: FieldError[]
}

export type PageResponse<T> = {
    items: T[]
    page: number
    size: number
    totalElements: number
    totalPages: number
}