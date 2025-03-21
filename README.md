# 와우배달
![대지 3](https://github.com/user-attachments/assets/43ee19c7-c677-4e55-a7bf-5a49d0f96f85)
   
**개인 프로젝트로 전국에 있는 음식점과 소비자와 배달기사를 중개하는 RESTful API 서버 입니다.**
- 코드 한 줄에도 이유와 목적이 있도록 작성했습니다.

# 프로젝트 고민사항 (링크)
### - [가게검색 시 (위도, 경도) 좌표 검색보다 더 효율적인 지리정보 데이터는?](https://destiny-nylon-d49.notion.site/10ac20cd297280d2ac77cd65e79ab7ec?pvs=4)
### - [많은 수의 라이더가 동시에 한 가게의 배차 요청을 신청할 경우의 동시성 이슈 해결방법은?](https://destiny-nylon-d49.notion.site/181c20cd2972808aa946ea2221f4fcb4?pvs=4)
### - [불필요한 외부 정보 노출을 최소화한 가게번호와 주문번호 전략 고민](https://destiny-nylon-d49.notion.site/120c20cd297280eb87ead81d9ec1d6d6?pvs=4)
### - [피크 시간대 대규모 트래픽에도 DB 부하를 줄이고 빠른 조회를 유지하는 방법은?](https://destiny-nylon-d49.notion.site/DB-184c20cd29728036b4dacd5c871aede1?pvs=4)
### - [MSA로 전환할때 기존의 글로벌 Redis를 활용한 세션 방식의 로그인을 그대로 가져가도 좋을까?](https://destiny-nylon-d49.notion.site/MSA-Redis-110c20cd297280629519f18ea906d84e?pvs=4)
### - JPA의 의존적인 연관관계 매핑을 피하면서도 성능과 확장성을 고려한 설계하기
### - 외부 API에 의존적이지 않은 단위 테스트를 구현하는 방법은?
<br/>
<br/>
<br/>

# 사용 기술 및 환경
- Java 17
- Spring boot 3.2
- MySQL 8.0
- Jpa
- Redis
- Docker
- Kafka
- Mockito
- AWS
- ALB, NLB
- EC2
- jenkins
  
<br/>
<br/>
<br/>
  
# 프로젝트 전체 구조

![Image](https://github.com/user-attachments/assets/546cccc6-5ff9-41ea-b0d0-ecf91bb3efb1)

## AWS 아키텍처 설계 개요 및 개선 방안

>***🚀ps) m2.medium 한번 사용했다가 감당할 수 없는 요금에 현실과 타협했습니다.  
하지만 개념적으로는 확실히 공부하고 적용했습니다.***
<br/>
## 1. 아키텍처 설계 배경

현재 AWS 아키텍처는 **Wow Delivery** 서비스의 운영을 위해 설계되었습니다.  
고가용성, 보안성, 운영 효율성을 고려하여 다음과 같은 구조로 구성되었습니다.

### 1) 고정 IP 제공을 위한 NLB + ALB 구조
- **Network Load Balancer (NLB)를 추가한 이유**는 **고정 IP 제공**이 필요했기 때문입니다.
- 기본적으로 **Application Load Balancer (ALB)는 고정 IP를 제공하지 않음**  
  → 특정 외부 시스템(고객사 API, 보안 정책 등)에서 고정 IP를 요구하는 경우 NLB가 필요함.
- NLB가 요청을 받아 **ALB로 전달**, ALB에서 HTTP/S 요청을 처리하는 방식.

### 2) NAT Instance를 사용한 인터넷 접근
- Private Subnet의 EC2 인스턴스들이 **외부 네트워크에 접근할 필요가 있음**  
  (예: 패키지 업데이트, 외부 API 호출 등)
- AWS에서 관리형 서비스인 **NAT Gateway** 대신 **NAT Instance를 사용**  
  → **비용 절감이 주된 이유**

### 3) Bastion 서버를 통한 보안 강화
- 개발자가 내부 EC2 서버에 접근해야 할 경우, **Bastion Server를 경유하도록 설정**
- 직접 EC2에 SSH 접속하지 않고, **보안 그룹을 통해 접근 제한**하여 보안 강화

### 4) EC2 내부에서 애플리케이션 및 데이터베이스 운영
- 현재 **EC2 내부에서 Docker 컨테이너로 MySQL, Redis, Kafka 등을 운영**
- 이는 **비용 절감을 위해** 선택된 방식  
  → AWS의 관리형 서비스(RDS, ElastiCache, MSK 등)를 사용하면 운영 부담이 줄어들지만, 비용이 증가하기 때문

### 5) CI/CD 서버 (Jenkins) 운영
- CI/CD 파이프라인을 위해 Jenkins를 **Private Subnet의 EC2 인스턴스에서 직접 운영**
- AWS CodePipeline 등의 대체 옵션도 있지만, **비용 및 기존 Jenkins 설정을 유지하기 위해 현재 방식 유지**

---

## 2. 개선할 수 있는 아키텍처 변경 사항
현재 구조는 비용 최적화를 위해 선택된 방식이지만, 몇 가지 개선이 가능합니다.

| 개선 포인트 | 현재 구조 | 개선 가능 옵션 | 예상 효과 |
|------------|---------|-------------|---------|
| **고정 IP 문제** | NLB → ALB 조합 사용 | **AWS Global Accelerator**를 사용하여 ALB에 고정 IP 제공 | 비용 절감, 아키텍처 단순화 |
| **NAT Instance 사용** | NAT Instance 운영 | **NAT Gateway 사용** | 자동 관리, 가용성 향상, 운영 부담 감소 |
| **Bastion Server 유지** | 퍼블릭 서브넷에서 운영 | **AWS SSM Session Manager 사용** | SSH 없이 보안성 강화 |
| **EC2 내부에서 DB 운영** | MySQL, Redis, Kafka를 직접 관리 | **AWS RDS, ElastiCache, MSK 사용** | 관리 부담 감소, 성능 최적화 |
| **Jenkins 직접 운영** | EC2에서 Docker로 운영 | **ECS Fargate 또는 AWS CodePipeline 사용** | 서버 유지보수 불필요, 운영 자동화 |

---

## 3. 최적화할 경우의 최종 아키텍처
- **NLB 제거**하고 **AWS Global Accelerator + ALB**를 활용하여 고정 IP 제공
- **NAT Instance 대신 NAT Gateway**를 사용하여 자동 확장 및 고가용성 확보
- **Bastion Server 대신 AWS SSM Session Manager**를 활용하여 SSH 없이 EC2 접속
- **MySQL, Redis, Kafka는 각각 AWS RDS, ElastiCache, MSK로 이전**하여 관리 부담 감소
- **Jenkins를 AWS CodePipeline 또는 ECS Fargate에서 운영**하여 CI/CD 인프라 유지 비용 절감

이런 방식으로 개선하면 **비용을 최적화하면서도 운영 효율성을 높일 수 있음**.  
현재 구조는 비용 절감을 최우선으로 고려한 아키텍처이며, 장기적으로는 관리형 서비스를 활용하는 방향을 검토할 수 있음.
  
<br/>
<br/>
<br/>
  
# 프로젝트 DB ERD
위 프로젝트는 JPA의 연관관계 매핑 대신 식별자를 이용한 조회를 선택했기 때문에 테이블간 연결구조는 이해를 돕기 위한 예시입니다.
  
![ERD](https://github.com/user-attachments/assets/ce388f11-b66e-499a-af43-2b8c2d2b3cb1)
