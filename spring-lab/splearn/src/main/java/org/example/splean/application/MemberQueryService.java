package org.example.splean.application;

import lombok.RequiredArgsConstructor;
import org.example.splean.application.provided.MemberFinder;
import org.example.splean.application.required.MemberRepository;
import org.example.splean.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {
    private final MemberRepository memberRepository;

    @Override
    public Member find(Long memberId) {
        // 버그가 있는 상황을 친절하게 내보낼 필요가 있을까??
        // 사용자에게 보여주면 안된다
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id" + memberId));
    }
}
