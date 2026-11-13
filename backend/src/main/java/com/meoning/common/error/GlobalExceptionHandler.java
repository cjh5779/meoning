package com.meoning.common.error;

import com.meoning.common.response.ResponseDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 업무 오류: 서비스에서 직접 던진 BusinessException
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDTO<ErrorDetails>> handleBusiness(BusinessException e) {
        log.info("업무 오류: {}", e.getErrorCode());
        return toResponse(e.getErrorCode(), List.of());
    }

    // 2. 요청 본문 검증 실패: @Valid가 붙은 DTO의 필드가 규칙을 어김
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<ErrorDetails>> handleValidation(MethodArgumentNotValidException e) {
        List<FieldErrorDetail> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();
        return toResponse(ErrorCode.VALIDATION_ERROR, fieldErrors);
    }

    // 3. 그 밖의 잘못된 요청 -> 전부 VALIDATION_ERROR (400)
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HandlerMethodValidationException.class
    })
    public ResponseEntity<ResponseDTO<ErrorDetails>> handleBadRequest(Exception e) {
        log.info("잘못된 요청: {}", e.getMessage());
        return toResponse(ErrorCode.VALIDATION_ERROR, List.of());
    }

    // 4. 존재하지 않는 URL -> NOT_FOUND (404)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseDTO<ErrorDetails>> handleNotFound(NoResourceFoundException e) {
        return toResponse(ErrorCode.NOT_FOUND, List.of());
    }

    // 5. URL은 있는데 HTTP 메서드가 다름 -> METHOD_NOT_ALLOWED (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDTO<ErrorDetails>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return toResponse(ErrorCode.METHOD_NOT_ALLOWED, List.of());
    }

    // 6. 위에서 못 잡은 모든 예외 -> INTERNAL_ERROR (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<ErrorDetails>> handleUnexpected(Exception e) {
        log.error("예상하지 못한 오류", e);
        return toResponse(ErrorCode.INTERNAL_ERROR, List.of());
    }

    private ResponseEntity<ResponseDTO<ErrorDetails>> toResponse(ErrorCode errorCode,
                                                                 List<FieldErrorDetail> fieldErrors) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ResponseDTO.error(errorCode, ErrorDetails.of(fieldErrors)));
    }
}
