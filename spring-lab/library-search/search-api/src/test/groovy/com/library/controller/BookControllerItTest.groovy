package com.library.controller

import com.library.controller.request.SearchRequest
import com.library.controller.response.PageResult
import com.library.controller.response.SearchResponse
import com.library.service.BookQueryService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookControllerItTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    BookQueryService bookQueryService = Mock(BookQueryService)

    // 정상, 잘못된 인자 (3가지)
    def "정상인자로 요청 시 성공한다."() {
        given:
        def request = new SearchRequest(query: "HTTP", page: 1, size: 10)

        and:
        bookQueryService.search(*_) >> new PageResult<>(1, 10, 10, [Mock(SearchResponse)])

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString()))

        then:
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath('$.totalElements').value(10))
                .andExpect(jsonPath('$.page').value(1))
                .andExpect(jsonPath('$.size').value(10))
                .andExpect(jsonPath('$.contents').isArray())
    }

    def "query가 비어있을 때 BadRequest 응답을 반환한다."() {
        given:
        def request = new SearchRequest(query: "", page: 1, size: 10)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString()))

        then:
        result.andExpect(status().isBadRequest())
        .andExpect(jsonPath('$.errorMessage').value("입력은 비어있을 수 없습니다."))
    }

    def "page가 음수일 경우에 BadRequest 응답을 반환한다."(){
        given:
        def request = new SearchRequest(query: "HTTP", page: -1, size: 10)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString()))

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.errorMessage').value("페이지 번호는 1이상이어야 합니다."))
    }

    def "size가 50을 초과하면 BadRequest 응답을 반환한다."() {
        given:
        def request = new SearchRequest(query: "HTTP", page: 1, size: 51)

        when:
        def result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                .param("query", request.query)
                .param("page", request.page.toString())
                .param("size", request.size.toString()))

        then:
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.errorMessage').value("페이지 사이즈는 50이하이어야 합니다."))
    }
}
