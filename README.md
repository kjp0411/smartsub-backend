# SmartSub Backend

Spring Boot 기반의 정기 구독 서비스 백엔드 시스템입니다.  
회원 관리, 상품 관리, 결제, 리뷰, 정기 결제 스케줄링 및 Slack 알림 기능 등을 제공합니다.

---

## 주요 기능
- 회원 가입, 로그인 (JWT 인증)
- 상품 CRUD (중복 등록 방지 포함)
- 결제 등록 및 조회
- 리뷰 작성 및 조회
- 정기 결제 스케줄링 (Spring Scheduler & Batch)
- Slack 알림 연동 (Webhook, OAuth 2.0 기반 DM 확장 예정)
- Redis 기반 선착순 쿠폰 발급 (예정)
- Kafka 기반 비동기 메시징 (알림 이벤트 처리)

---

## 기술 스택
- **Backend**: Spring Boot, Spring Security, Spring Batch, Spring Scheduler
- **Database**: MySQL (H2 for local test), Redis
- **Messaging**: Kafka
- **Frontend**: React (별도 레포지토리)
- **DevOps**: Docker, AWS (배포 예정)
- **CI/CD**: GitHub Actions (예정)

---

## 실행 방법
```bash
# 1. 레포지토리 클론
git clone https://github.com/사용자명/smartsub-backend.git
cd smartsub-backend

# 2. 빌드 및 실행
./gradlew build
./gradlew bootRun
