package com.meoning.common.error;

import com.meoning.common.filter.RequestIdFilter;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.slf4j.MDC;

public record ErrorDetails(String requestId,
                           OffsetDateTime serverTime,
                           List<FieldErrorDetail> fieldErrors) {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    public static ErrorDetails of(List<FieldErrorDetail> fieldErrors) {
        return new ErrorDetails(
            MDC.get(RequestIdFilter.MDC_KEY),
            OffsetDateTime.now(SEOUL),
            fieldErrors
        );
    }

    public static ErrorDetails empty() {
        return of(List.of());
    }
}
