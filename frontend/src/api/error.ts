import axios from 'axios'
import type { ApiResponse, ErrorDetails } from '../types/api'

export function getApiErrorCode(error: unknown): string | null {
    if (axios.isAxiosError<ApiResponse<ErrorDetails>>(error)) {
        return error.response?.data?.code ?? null
    }

    return null
}