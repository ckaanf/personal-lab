package com.library.controller.response

import spock.lang.Specification

class PageResultTest extends Specification {
    def "pageResult 객체 생성."() {
        given:
        def givenPage = 1;
        def givenSize = 10;
        def givenTotalElements = 2;
        def searchResponse1 = Mock(SearchResponse)
        def searchResponse2 = Mock(SearchResponse)

        when:
        def result = new PageResult<>(givenPage, givenSize, givenTotalElements, [searchResponse1, searchResponse2])

        then:
        verifyAll {
            result.page() == givenPage
            result.size() == givenSize
            result.totalElements() == givenTotalElements
            result.contents().size() == 2
        }
    }
}
