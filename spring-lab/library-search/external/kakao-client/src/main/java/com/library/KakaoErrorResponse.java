package com.library;

public record KakaoErrorResponse(
        String errorType,
        String message
) {
}
