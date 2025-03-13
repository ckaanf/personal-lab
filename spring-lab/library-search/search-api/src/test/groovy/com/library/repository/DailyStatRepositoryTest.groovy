package com.library.repository

import com.library.entity.DailyStat
import com.library.feign.NaverClient
import jakarta.persistence.EntityManager
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
class DailyStatRepositoryTest extends Specification {

    @Autowired
    DailyStatRepository dailyStatRepository

    @Autowired
    EntityManager entityManager

    @SpringBean
    NaverClient naverClient = Mock(NaverClient)

    def "저장 후 조회가 된다."() {
        given:
        def givenQuery = "HTTP"
        def givenEventDateTime = LocalDateTime.now()

        when:
        DailyStat dailyStat = new DailyStat(query: givenQuery, eventDateTime: givenEventDateTime)
        def saved = dailyStatRepository.saveAndFlush(dailyStat)

        then: "실제 저장이 된다."
        saved.id != null

        when: "조회를 한다."
        entityManager.clear()
        def result = dailyStatRepository.findById(saved.id)

        then:
        verifyAll {
            result.isPresent()
            result.get().query == givenQuery
        }
    }

    def "쿼리의 카운트를 조회한다."() {
        given:
        def givenQuery = "HTTP"
        def now = LocalDateTime.of(2025, 3, 13, 0, 0, 0)

        def stat1 = new DailyStat(givenQuery, now.plusMinutes(10))
        def stat2 = new DailyStat(givenQuery, now.minusMinutes(1))
        def stat3 = new DailyStat(givenQuery, now.plusMinutes(10))
        def stat4 = new DailyStat('JAVA', now.plusMinutes(10))


        when:
        dailyStatRepository.saveAll(
                [stat1, stat2, stat3, stat4]
        )

        def result = dailyStatRepository.countByQueryAndEventDateTimeBetween(givenQuery, now, now.plusDays(1))

        then: "카운트를 조회한다."
        result == 2
    }
}
