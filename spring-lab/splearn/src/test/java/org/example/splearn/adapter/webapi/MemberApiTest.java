package org.example.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.splearn.adapter.webapi.dto.MemberRegisterResponse;
import org.example.splearn.application.member.provided.MemberRegister;
import org.example.splearn.application.member.required.MemberRepository;
import org.example.splearn.domain.MemberFixture;
import org.example.splearn.domain.member.Member;
import org.example.splearn.domain.member.MemberRegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.splearn.AssertThatUtils.equalsTo;
import static org.example.splearn.AssertThatUtils.notNull;
import static org.example.splearn.domain.member.MemberStatus.PENDING;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
public class MemberApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void testRegister() throws JsonProcessingException, UnsupportedEncodingException {

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/members")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestJson).exchange();


        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);
        Member member = memberRepository.findById(response.memberId())
                .orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(PENDING);
    }

    @Test
    void duplicateEmail() throws JsonProcessingException {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/members")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(requestJson).exchange();


        assertThat(result)
                .apply(print())
                .hasStatus4xxClientError();

    }
}
