package com.meoning.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BENEFIT_EXPIRED(
            HttpStatus.CONFLICT,
            "마감된 혜택은 찜할 수 없습니다."
    ),
    BENEFIT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "혜택을 찾을 수 없습니다."
    ),
    CURRENT_PASSWORD_MISMATCH(
            HttpStatus.BAD_REQUEST,
            "현재 비밀번호가 올바르지 않습니다."
    ),
    EMAIL_DUPLICATED(
            HttpStatus.CONFLICT,
            "이미 가입된 이메일입니다."
    ),
    EMAIL_NOT_VERIFIED(
            HttpStatus.FORBIDDEN,
            "이메일 인증 후 로그인할 수 있습니다."
    ),
    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "접근 권한이 없습니다."
    ),
    IMAGE_TOO_LARGE(
            HttpStatus.CONTENT_TOO_LARGE,
            "이미지는 5MB 이하만 올릴 수 있습니다."
    ),
    INTERNAL_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
    ),
    INVALID_CREDENTIALS(
            HttpStatus.UNAUTHORIZED,
            "이메일 또는 비밀번호가 올바르지 않습니다."
    ),
    INVALID_DATE_RANGE(
            HttpStatus.BAD_REQUEST,
            "마감일은 시작일보다 빠를 수 없습니다."
    ),
    INVALID_REFRESH_TOKEN(
            HttpStatus.UNAUTHORIZED,
            "다시 로그인해 주세요."
    ),
    INVALID_RESET_TOKEN(
            HttpStatus.BAD_REQUEST,
            "재설정 링크가 만료되었거나 이미 사용되었습니다."
    ),
    INVALID_STATUS_TRANSITION(
            HttpStatus.CONFLICT,
            "현재 상태에서는 변경할 수 없는 상태입니다."
    ),
    INVALID_TARGET_CONDITION(
            HttpStatus.BAD_REQUEST,
            "대상 조건 형식이 올바르지 않습니다."
    ),
    INVALID_UNSUBSCRIBE_TOKEN(
            HttpStatus.BAD_REQUEST,
            "수신 거부 링크가 올바르지 않습니다."
    ),
    INVALID_VERIFICATION_TOKEN(
            HttpStatus.BAD_REQUEST,
            "인증 링크가 만료되었거나 이미 사용되었습니다."
    ),
    LAST_ADMIN_CANNOT_WITHDRAW(
            HttpStatus.CONFLICT,
            "마지막 관리자 계정은 탈퇴할 수 없습니다."
    ),
    METHOD_NOT_ALLOWED(
            HttpStatus.METHOD_NOT_ALLOWED,
            "지원하지 않는 요청 방식입니다."
    ),
    NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "요청한 리소스를 찾을 수 없습니다."
    ),
    PUBLISH_REQUIRED_FIELDS_MISSING(
            HttpStatus.BAD_REQUEST,
            "공개하려면 카테고리, 한 줄 요약, 공식 링크가 필요합니다."
    ),
    TOO_MANY_REQUESTS(
            HttpStatus.TOO_MANY_REQUESTS,
            "요청이 너무 많습니다. 잠시 후 다시 시도해 주세요."
    ),
    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "로그인이 필요합니다."
    ),
    UNSUPPORTED_IMAGE_TYPE(
            HttpStatus.BAD_REQUEST,
            "JPEG, PNG, WebP 이미지만 올릴 수 있습니다."
    ),
    VALIDATION_ERROR(
            HttpStatus.BAD_REQUEST,
            "요청 값이 올바르지 않습니다."
    );

    private final HttpStatus status;
    private final String message;
}