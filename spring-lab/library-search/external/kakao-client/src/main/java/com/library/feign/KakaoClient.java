package com.library.feign;

import com.library.KakaoBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoClient", url = "${external.kakao.url}", configuration = KakaoClientConfiguration.class)
public interface KakaoClient {

    @GetMapping("/v3/search/book")
    KakaoBookResponse searchBook(@RequestParam("query") String query,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size);
}
