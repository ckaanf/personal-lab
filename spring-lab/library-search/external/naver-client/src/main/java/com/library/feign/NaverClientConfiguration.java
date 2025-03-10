package com.library.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Configuration;

public class NaverClientConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor(@Value("${external.naver.headers.client-id}") String clientId,
                                                 @Value("${external.naver.headers.client-secret}") String clientSecret) {
        return requestTemplate -> {
            requestTemplate.header("X-Naver-Client-Id", clientId);
            requestTemplate.header("X-Naver-Client-Secret", clientSecret);
        };
    }

    @Bean
    public NaverErrorDecoder naverErrorDecoder(ObjectMapper objectMapper) {
        return new NaverErrorDecoder(objectMapper);

    }
}
