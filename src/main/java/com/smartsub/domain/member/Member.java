package com.smartsub.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원(Member) 엔티티 클래스
 * - DB의 member 테이블과 매핑됨
 * - 회원 이메일, 이름, 비밀번호, 생성 시간 정보를 포함
 */

@Entity // 이 클래스는 JPA의 엔티티임을 명시
@Table(name = "member", indexes = {
    @Index(name = "uk_member_email", columnList = "email", unique = true) // 이메일에 대한 유니크 인덱스 설정
})
@Getter // 모든 필드에 대한 getter 자동 생성 (Lombok)
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 기본 생성자 (외부 생성을 제한)
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
@Builder    // Builder 패턴을 사용하여 객체 생성 가능
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략을 IDENTITY로 설정
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // 이메일은 null이 아니고, 유일해야 하며, 최대 길이는 100
    private String email;

    @Column(nullable = false, length = 50) // 이름은 null이 아니고, 최대 길이는 50
    private String name;

    @Column(name = "password_hash", nullable = false) // 비밀번호는 null이 아니어야 함
    private String password;

    @Column(nullable = false, updatable = false) // 생성 시간은 null이 아니고, 수정 불가
    private LocalDateTime createdAt; // @Column(nullable = false)를 붙이지 않은 이유는, 객체를 생성할 때는 null 상태일 수 있음

    @Column(name = "slack_user_id", length = 255)
    private String slackUserId; // Slack 사용자 고유 ID (외래 키 X)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; // 회원의 역할 (USER, ADMIN 등)

    // 토큰 최신성 체크용
    @Column(nullable = false)
    @Builder.Default
    private Integer tokenVersion = 0; // 토큰 버전, 기본값은 0

    @PrePersist // 엔티티가 DB에 저장되기 전에 호출되는 메서드
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now(); // 객체가 DB에 저장되기 전에 현재 시간을 생성 시간으로 설정
        if (this.role == null) this.role = Role.USER; // 기본 역할을 USER로 설정
        if (this.tokenVersion == null) this.tokenVersion = 0; // 기본 토큰 버전을 0으로 설정
    }

    public void updateName(String name) {
        this.name = name; // 이름 업데이트
    }

    public void updatePassword(String password) {
        this.password = password; // 비밀번호 업데이트
    }

    public void setSlackUserId(String slackUserId) {
        this.slackUserId = slackUserId;
    }

    // 토큰 버전 증가 메서드(관리자 전용)
    public void increaseTokenVersion() {
        this.tokenVersion ++; // 토큰 버전 증가
    }
}
