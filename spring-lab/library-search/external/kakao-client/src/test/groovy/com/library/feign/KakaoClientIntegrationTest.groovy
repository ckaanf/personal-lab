package com.library.feign

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.test.context.ActiveProfiles
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
@SpringBootTest(classes = Testconfig.class)
@ActiveProfiles("test")
class KakaoClientIntegrationTest extends Specification {

    @EnableAutoConfiguration
    @EnableFeignClients(clients = KakaoClient.class)
    static class Testconfig{}

    @Autowired
    KakaoClient kakaoClient

    def "카카오 호출"() {
        given:

        when:
        def response = kakaoClient.searchBook("HTTP",1,10)

        then:
        response.meta().totalCount() == 78
    }
}

