package com.library.service

import com.library.controller.response.StatResponse
import com.library.repository.DailyStatRepository
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class DailyStatQueryServiceTest extends Specification {
    DailyStatRepository dailyStatRepository = Mock(DailyStatRepository)
    
    DailyStatQueryService dailyStatQueryService

    void setup() {
        dailyStatQueryService = new DailyStatQueryService(dailyStatRepository)
    }

    def "findQueryCount 조회 시 하루의 쿼리갯수가 반환된다."() {
        given:
        def givenQuery = "HTTP"
        def givenDate = LocalDate.of(2025, 3, 13)
        def expectedCount = 10

        when:
        def response = dailyStatQueryService.findQueryCount(givenQuery, givenDate)

        then:
        1 * dailyStatRepository.countByQueryAndEventDateTimeBetween(
                givenQuery,
                LocalDateTime.of(2025,3,13,0,0,0),
                LocalDateTime.of(2025,3,13,23,59,59,999_999_999),

        ) >> expectedCount

        and:
        response.count() == expectedCount
    }
}
