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
}
