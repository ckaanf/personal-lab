package org.example.splean.application.provided;

import org.example.splean.domain.Member;

/**
 * 회원을 조회한다
 */
public interface MemberFinder {
    Member find(Long memberId);
}
