package com.library.service

import com.library.controller.response.PageResult
import com.library.repository.KakaoBookRepository
import com.library.repository.NaverBookRepository
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
class BookQueryServiceItTest extends Specification {

    @Autowired
    BookQueryService bookQueryService

    @Autowired
    CircuitBreakerRegistry circuitBreakerRegistry

    @SpringBean
    KakaoBookRepository kakaoBookRepository = Mock()

    @SpringBean
    NaverBookRepository naverBookRepository = Mock()

    def "정상상황에서는 Circuit의 상태가 CLOSED이고 naver 쪽으로 호출이 들어간다."() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 10

        when:
        bookQueryService.search(givenQuery, givenPage, givenSize)

        then:
        1 * naverBookRepository.search(givenQuery, givenPage, givenSize) >> new PageResult<>(1, 10, 0, [])

        and:
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.CLOSED

        and:
        0 * kakaoBookRepository.search(givenQuery, givenPage, givenSize)

    }

    def "circuit이 open 되면 kakao쪽으로 요청한다."() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 10
        def kakaoResponse = new PageResult<>(1, 10, 0, [])

        def config = CircuitBreakerConfig.custom()
                .slidingWindowSize(1)
                .minimumNumberOfCalls(1)
                .failureRateThreshold(50)
                .build()
        circuitBreakerRegistry.circuitBreaker("naverSearch", config)

        and: "naver쪽은 항상 예외가 발생한다."
        naverBookRepository.search(givenQuery, givenPage, givenSize) >> { throw new RuntimeException("Naver API Error") }

        when:
        def result = bookQueryService.search(givenQuery, givenPage, givenSize)

        then: "kakao쪽으로 Fallback 된다."
        1 * kakaoBookRepository.search(givenQuery, givenPage, givenSize) >> kakaoResponse

        and: "circuit이 OPEN된다."
        def circuitBreaker = circuitBreakerRegistry.getAllCircuitBreakers().stream().findFirst().get()
        circuitBreaker.state == CircuitBreaker.State.OPEN

        and:
        result == kakaoResponse
    }

}
