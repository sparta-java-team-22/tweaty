![header](https://capsule-render.vercel.app/api?type=waving&color=A3DCBE&height=200&section=header&text=DiningPick:%20식당%20예약%20플랫폼&fontSize=40)

---

### 1. 프로젝트 소개

> 🍽️ 원하는 식당을 검색하고 실시간으로 예약 가능 여부를 확인한 뒤 손쉽게 예약할 수 있는 식당 예약 서비스입니다.

<p align="center">
<img src="https://github.com/user-attachments/assets/59050f5d-8742-4b33-a169-b0859fa9471c" width="250" height="250"/> 
</p>

---

<br>

### 2. 주요 목적

> * 서비스 간 분산 구조를 통해 확장성과 유연한 트래픽 분산을 확보
> * Redis와 Kafka를 활용하여 고속 처리 및 비동기 메시징 기반 아키텍처 구현
> * 동시 접속 및 요청 폭주 상황에서 안정적인 서비스 유지
> * 부하 테스트를 통해 병목 구간 사전 식별 및 최적화
> * Docker 기반 컨테이너화로 환경 일관성과 배포 자동화 지원
> * Github Actions를 활용한 CI/CD 파이프라인 구성으로 배포 자동화
> * Prometheus와 Grafana를 활용한 실시간 모니터링으로 시스템 안정성 확보

<br>

### 👤 3. 팀원소개

|      김은수      |          박동휘          |          손세라          |      이승욱       |
|:-------------:|:---------------------:|:---------------------:|:--------------:|
| Coupon 서비스 담당 | Store, Payment 서비스 담당 | User, Notification 담당 | Reservation 담당 |

---

### 4. 시스템 특징

**🏪 가게 관리 시스템**
> 
> * 점주는 자신의 가게 정보를 등록하고 수정할 수 있으며, 사용자는 전체 가게 목록을 검색하거나 특정 가게의 상세 정보를 열람할 수 있습니다.
> * 가게별로 메뉴를 자유롭게 추가, 수정, 삭제할 수 있으며, 사용자는 가게 상세 페이지에서 메뉴를 함께 확인할 수 있습니다.
> * 지역, 음식 카테고리 등의 조건을 통해 원하는 가게를 빠르게 찾아볼 수 있습니다.

<br>

**💳 결제 처리 시스템**
> 
> * 예약 정보를 기반으로 결제가 생성되며, 예약 상태를 실시간으로 검증합니다.
> * 결제의 성공 여부를 확인하고 상태를 갱신하며, 예약 취소 시 환불 조건에 따라 환불 처리됩니다.

<br>

**🎁 프로모션 및 쿠폰 시스템**
> 
> * 이벤트성 마케팅을 위한 프로모션을 등록하고 종료할 수 있습니다.
> * 정해진 수량 내에서 선착순으로 쿠폰을 발급하여, 트래픽을 유도하거나 특정 시간대 예약을 유도하는 데 활용할 수 있습니다.
> * 쿠폰 등록, 발급, 사용, 사용 취소, 수정, 삭제 등 쿠폰 라이프사이클을 지원합니다.

<br>

**👥 사용자 관리**
> * 일반 사용자와 가게 주인으로 나누어 가입할 수 있으며, 각각의 권한에 맞는 기능을 제공합니다.
> * 관리자는 전체 사용자 목록을 조회하고 필요한 정보를 검색할 수 있습니다.

<br>

**🔔 알림 시스템**
>
> * 회원가입, 가게 등록 승인/거절, 예약 생성/변경/취소 등 주요 활동에 대한 알림을 자동으로 발송합니다.

<br>

**📅 예약 관리**
>
> * 점주는 예약 가능한 시간대를 설정하고 관리할 수 있습니다.
> * 손님은 원하는 가게에 예약을 생성하고, 수정 또는 취소할 수 있습니다

<br>


## 🗃️ 5. ERD

> PostgreSQL 기반 ERD

<img width="700" alt="image" src="https://github.com/user-attachments/assets/cccf11bb-60e6-4d61-b950-d88de0438b1a" />

## 🛠 6. 기술 스택

<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Java-007396?&style=flat&logo=java&logoColor=white" style="margin-right: 10px;"> <br>
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?&style=flat&logo=springboot&logoColor=white" style="margin-right: 10px;"> <br>
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=flat&logo=&logoColor=white" style="margin-right: 10px;"/> <br>
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=Postgresql&logoColor=white" style="margin-right: 10px;"/> <br>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>

</div>
