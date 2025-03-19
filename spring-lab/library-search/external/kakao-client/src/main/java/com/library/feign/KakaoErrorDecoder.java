package com.library.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.exception.ApiException;
import com.library.KakaoErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.library.exception.ErrorType.EXTERNAL_API_ERROR;
import static com.library.exception.ErrorType.UNKNOWN;

@Slf4j
public class KakaoErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    public KakaoErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) throws ApiException {
        try {
            String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            KakaoErrorResponse errorResponse = objectMapper.readValue(body, KakaoErrorResponse.class);
            throw new ApiException(errorResponse.message(), EXTERNAL_API_ERROR, HttpStatus.valueOf(response.status()));
        } catch (IOException e) {
            log.error("[Kakao] 에러 메세지 파싱 에러 code={}, request={}, methodKey={},  errorMessage={}",
                    response.status(),
                    response.request(),
                    methodKey,
                    e.getMessage());
            throw new ApiException("카카오 메세지 파싱에러", UNKNOWN, HttpStatus.valueOf(response.status()));
        }
    }
}
