package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.entity.DailyStat
import spock.lang.Specification

class BookApplicationServiceTest extends Specification {
    BookQueryService bookQueryService = Mock(BookQueryService)
    DailyStatCommandService dailyStatCommandService = Mock(DailyStatCommandService)

    BookApplicationService bookApplicationService

    void setup() {
        bookApplicationService = new BookApplicationService(bookQueryService, dailyStatCommandService)
    }

    def "search() 호출 시 검색결과를 반환하면서 통계데이터를 저장한다."() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 10

        when:
        def response = bookApplicationService.search(givenQuery, givenPage, givenSize)

        then:
        1 * bookQueryService.search(*_) >> {
            String query, int page, int size ->
                assert query == givenQuery
                assert page == givenPage
                assert size == givenSize
        }

        and:
        1* dailyStatCommandService.save(*_) >> {
            DailyStat dailyStat ->
                assert dailyStat.query == givenQuery
        }
    }
}
