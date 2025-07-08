package org.example.splean.application.provided;

import jakarta.validation.Valid;
import org.example.splean.domain.Member;
import org.example.splean.domain.MemberRegisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다
 */
public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);
}
