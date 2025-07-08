package org.example.splean.application;

import lombok.RequiredArgsConstructor;
import org.example.splean.application.provided.MemberRegister;
import org.example.splean.application.required.EmailSender;
import org.example.splean.application.required.MemberRepository;
import org.example.splean.domain.Member;
import org.example.splean.domain.MemberRegisterRequest;
import org.example.splean.domain.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check

        Member member = Member.register(registerRequest, passwordEncoder);
        
        memberRepository.save(member);

        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 클릭해서 등록을 완료해주세요");

        return member;
    }
}
