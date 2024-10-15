# 와우배달
![대지 3](https://github.com/user-attachments/assets/43ee19c7-c677-4e55-a7bf-5a49d0f96f85)  
   
**개인 프로젝트로 전국에 있는 음식점과 소비자와 배달기사를 중개하는 RESTful API 서버 입니다.**
- 코드 한 줄에도 이유와 목적이 있도록 작성했습니다.
- 가상의 다중 인스턴스를 기반하여 설계했습니다.

# 프로젝트 고민사항
### - 가게검색 시 (위도, 경도) 좌표 검색보다 더 효율적인 지리정보 데이터는?
### - 많은 수의 라이더가 동시에 한 가게의 배차 요청을 신청할 경우의 동시성 이슈 해결방법은?
### - [불필요한 외부 정보 노출을 최소화한 가게번호와 주문번호 전략 고민](https://destiny-nylon-d49.notion.site/120c20cd297280eb87ead81d9ec1d6d6?pvs=4)
### - 피크 시간대 대규모 트래픽에도 DB 부하를 줄이고 빠른 조회를 유지하는 방법은?
### - [MSA로 전환할때 기존의 글로벌 Redis를 활용한 세션 방식의 로그인을 그대로 가져가도 좋을까?](https://destiny-nylon-d49.notion.site/MSA-Redis-110c20cd297280629519f18ea906d84e?pvs=4)
### - JPA의 의존적인 연관관계 매핑을 피하면서도 성능과 확장성을 고려한 설계하기
### - 외부 API에 의존적이지 않은 단위 테스트를 구현하는 방법은?

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
- EC2
  
  
  
# 프로젝트 전체 구조
![프로젝트 구조](https://github.com/user-attachments/assets/8874d6e2-92fc-49d2-9378-abfe074deac8)
# 프로젝트 DB ERD
위 프로젝트는 JPA의 연관관계 매핑 대신 식별자를 이용한 조회를 선택했기 때문에 테이블간 연결구조는 이해를 돕기 위한 예시입니다.
  
![ERD](https://github.com/user-attachments/assets/ce388f11-b66e-499a-af43-2b8c2d2b3cb1)
