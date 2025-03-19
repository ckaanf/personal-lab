package com.library.service.event

import com.library.entity.DailyStat
import com.library.service.DailyStatCommandService
import spock.lang.Specification

import java.time.LocalDateTime

class SearchEventHandlerTest extends Specification {
    def "handleEvent"() {
        given:
        def commandService = Mock(DailyStatCommandService)
        def eventHandler = new SearchEventHandler(commandService)
        def event = new SearchEvent("HTTP", LocalDateTime.now())

        when:
        eventHandler.handleEvent(event)

        then:
        1 * commandService.save(_ as DailyStat)
    }
}
