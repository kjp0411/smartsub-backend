package com.smartsub.repository.member;

import com.smartsub.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Member 엔티티에 대한 데이터 접근 레이어
 * - JpaRepository를 상속받아 기본 CRUD 자동 생성
 * - 이메일로 회원 조회하는 메서드 추가
 */
// interface로 만든 이유: Spring Data JPA의 구조와 객체지향 설계 원칙을 반영
public interface MemberRepository extends JpaRepository<Member, Long> { // Member 엔티티에 대한 CRUD 메서드 자동 생성

    @Query("select m.tokenVersion from Member m where m.id = :id")
    int findTokenVersionById(@Param("id") Long id);

    Optional<Member> findByEmail(String email);
}
