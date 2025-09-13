# SmartSub Backend

Spring Boot 기반의 정기 구독 서비스 백엔드 시스템입니다.  
회원 관리, 상품 관리, 결제, 리뷰, 정기 결제 스케줄링 및 Slack 알림 기능 등을 제공합니다.

---

## 프로젝트 주요 기술 및 구현 사례
<img width="800" height="785" alt="image" src="https://github.com/user-attachments/assets/33bebf96-470f-42f2-8656-d4e4132b26f2" />

---

## 시스템 구성도
<img width="800" height="797" alt="image" src="https://github.com/user-attachments/assets/fa2c848f-6e68-429c-a5b0-40b5006fce27" />

---

## 시스템 구성 요소 및 역할
<img width="800" height="796" alt="image" src="https://github.com/user-attachments/assets/72b9fe1f-22ef-4e62-80e4-36423595eefc" />

---

## 주요 기능 구현
<img width="800" height="798" alt="image" src="https://github.com/user-attachments/assets/e91efea1-38ae-48ee-9097-fd2278c2532b" />

---

## 문제 해결 및 리팩토링 경험
<img width="800" height="790" alt="image" src="https://github.com/user-attachments/assets/4d1e0496-d1a7-4f7e-8032-dd8076b53909" />

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
