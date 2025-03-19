package com.library.controller.response

import spock.lang.Specification

import java.time.LocalDate

class SearchResponseTest extends Specification {

    def "searchResponse 객체가 생성."() {
        given:
        def givenTitle =  "HTTP 완벽가이드"
        def givenAuthor =  "데이빗 고울리"
        def givenPublisher =  "인사이트"
        def givenPubDate =  LocalDate.of(2014, 12, 15)
        def givenIsbn =  "9788966261208"

        when:
        def result = SearchResponse.builder()
                .title(givenTitle)
                .author(givenAuthor)
                .publisher(givenPublisher)
                .pubDate(givenPubDate)
                .isbn(givenIsbn)
                .build()

        then:
        verifyAll {
            result.title() == givenTitle
            result.author() == givenAuthor
            result.publisher() == givenPublisher
            result.pubDate() == givenPubDate
            result.isbn() == givenIsbn
        }
    }
}
