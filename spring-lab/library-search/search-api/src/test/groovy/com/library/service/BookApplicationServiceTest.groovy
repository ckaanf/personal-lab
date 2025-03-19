package com.library.service

import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.service.event.SearchEvent
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import java.time.LocalDate

class BookApplicationServiceTest extends Specification {
    BookQueryService bookQueryService = Mock(BookQueryService)
    DailyStatQueryService dailyStatQueryService = Mock(DailyStatQueryService)
    ApplicationEventPublisher eventPublisher = Mock(ApplicationEventPublisher)

    BookApplicationService bookApplicationService

    void setup() {
        bookApplicationService = new BookApplicationService(bookQueryService, dailyStatQueryService, eventPublisher)
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

                new PageResult<>(1,10,1, [Mock(SearchResponse)])
        }

        and :"저장 이벤트를 발행한다."
        1* eventPublisher.publishEvent(_ as SearchEvent)
    }

    def "findQueryCount() 호출 시 인자를 그대로 넘긴다."() {
        given:
        def givenQuery = "HTTP"
        def givenDate = LocalDate.of(2025, 3, 13)

        when:
        bookApplicationService.findQueryCount(givenQuery, givenDate)

        then:
        1 * dailyStatQueryService.findQueryCount(*_) >> {
            String query, LocalDate date ->
                assert query == givenQuery
                assert date == givenDate
        }
    }

    def "findTopNQuery() 호출 시 dailyStatQueryService의 findTop5Query가 호출된다."() {
        when:
        bookApplicationService.findTopNQuery(0, 3)

        then:
        1 * dailyStatQueryService.findTopNQuery(*_)
    }
}
