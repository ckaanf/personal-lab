package com.library.feign

import feign.RequestTemplate
import spock.lang.Specification

class KakaoClientConfigurationTest extends Specification {
    KakaoClientConfiguration configuration

    void setup() {
        configuration = new KakaoClientConfiguration()
    }

    def "RequestInterceptor의 header에 키 값들이 적용된다"() {
        given:
        def template = new RequestTemplate()
        def restApiKey = "restApiKey"

        and: "interceptor를 타기전에 header가 존재하지 않는다"
        template.headers()["Authorization"] == null

        when: "interceptor를 탄다"
        def interceptor = configuration.requestInterceptor(restApiKey)
        interceptor.apply(template)

        then: "interceptor를 탄 이후에는 header가 추가된다."
        template.headers()["Authorization"].contains("KakaoAK "+ restApiKey)
    }
}
