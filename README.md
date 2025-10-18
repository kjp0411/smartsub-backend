# SmartSub Backend

AI 기반의 정기 구독 서비스 백엔드 시스템입니다.  
회원 관리, 상품 관리, 결제, 리뷰, 정기 결제 스케줄링 및 Slack 알림 기능 등을 제공합니다.
SmartSub은 사용자의 구독 패턴을 분석하고, 정기 결제와 알림을 자동으로 처리하는 AI 기반 구독 서비스입니다.

---

## System Architecture
SmartSub의 전체 인프라 및 데이터 흐름 구조입니다.
AWS 기반 배포, Kafka 메시징, Redis 캐시, Slack 알림, Python 분석 연동 등
실무형 백엔드 아키텍처를 설계했습니다.
<p align="center"> 
  <img width="1747" height="825" alt="image" src="https://github.com/user-attachments/assets/9cbd1532-2d6b-4fd1-81cc-c05729b36c1c"/> 
</p> 
<p align="center"> 
  <em>Spring Boot 기반 백엔드 – AWS, Redis, Kafka, Slack 통합 구조</em> 
</p>

---

## Slack Notification
Spring Batch와 Kafka를 통해 주기적으로 결제 데이터를 처리하고,
Slack API를 통해 결제 결과 및 정기 구독 알림을 실시간 전송합니다.
<p align="center"> 
  <img width="940" height="939" alt="image" src="https://github.com/user-attachments/assets/30cc5b79-b515-4f38-bfd1-0632be2b83c4" />
</p> 
<p align="center"> 
  <em>Kafka Consumer(Python) → Slack Webhook 실시간 알림</em> 
</p>

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
