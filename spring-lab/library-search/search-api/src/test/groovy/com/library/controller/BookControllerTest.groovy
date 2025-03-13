package com.library.controller

import com.library.service.BookApplicationService
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDate

class BookControllerTest extends Specification {
    BookApplicationService bookApplicationService = Mock(BookApplicationService)

    BookController bookController
    MockMvc mockMvc

    void setup() {
        bookController = new BookController(bookApplicationService)
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .build()
    }

    def "search"() {
        given:
        def givenQuery = "HTTP"
        def givenPage = 1
        def givenSize = 10

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books?query=${givenQuery}&page=${givenPage}&size=${givenSize}")
        ).andReturn().response


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books")
                        .param("query", givenQuery)
                        .param("page", givenPage as String)
                        .param("size", givenSize as String)
        ).andReturn().response

        then:
        verifyAll {
            and:
            2 * bookApplicationService.search(*_) >> {
                String query, int page, int size ->
                    assert query == givenQuery
                    assert page == givenPage
                    assert size == givenSize
            }
        }
    }

    def "findStat"() {
        given:
        def givenQuery = "HTTP"
        def givenDate = LocalDate.of(2025, 3, 13)

        when:
        def response = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books/stats?query=${givenQuery}&date=${givenDate}")
        ).andReturn().response


        then:
        response.status == HttpStatus.OK.value()
        verifyAll {
            and:
            1 * bookApplicationService.findQueryCount(*_) >> {
                String query, LocalDate date ->
                    assert query == givenQuery
                    assert date == givenDate
            }
        }
    }
}
