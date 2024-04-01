# MoaMoa
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/9fe65f5e-c41d-4532-bd5f-f3fb2578e41d)


# Introduction

합리적인 소비를 하기 위해 모인 사람들과 필요한 것을 공동으로 구매할 수 있는 사이트

개발 기간 : 2024.02.26 ~ 2024.04.05

---
# Service Architecture
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/efc3a023-6b81-4f8d-933d-3ed4d22bf4e3)





# Technology
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/b4e80e98-1e41-4017-82db-62c0e979b3ce)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/4b47b474-05d2-4dee-975f-ca9e879e8c91)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/147768a3-2363-4e36-9da3-172c958cdadc)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/e812a620-036e-417b-99b4-a1224ebe4917)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/b6d68008-7a2a-42ff-8099-51d353297fcf)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/4338160c-2384-43b5-9372-96ee0d29f8fe)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/49499ac3-577b-4a2f-8fd7-7e040c6ed04e)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/03309061-23d6-4713-a7b0-fd95ac957827)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/b0635d06-9c88-415b-93c4-9e8279c813c1)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/f335bc87-c354-4662-9c0f-9d1b27d943e1)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/fccabf1b-0925-479f-bd2d-f9d264cbfed8)
![image](https://github.com/cwc-moamoa/moamoa/assets/149165093/ec44aa65-c9d0-40f3-8565-7ee8abea021e)













---
# DB modeling

<img width="1152" alt="image" src="https://github.com/cwc-moamoa/moamoa/assets/120659405/18f0343f-2184-45a4-af35-c0c9eb9b6858">


---
# Features

- products
    - 전체 상품 조회 
    - 제품 상세 정보 조회
    - 상품 등록
    - 상품 수정
    - 상품 삭제
- groupPurchase
    - 공동구매 참여
    - 개인 일반 구매
- image
    - 이미지 업로드
- payment
    - 결제
    - 결제 검증
- order
    - 전체 주문 조회
    - 주문 상세 정보 조회
    - 주문 생성
    - 주문 수정
    - 주문 취소
    - 주문 추적 상태 변경(판매자)
    - 판매 내역 조회(판매자)
- review
    - 전체 리뷰 조회 
    - 리뷰 등록
    - 리뷰 수정
    - 리뷰 조회
    - 리뷰 삭제
- like
    - 상품 좋아요
    - 상품 좋아요 취소
    - 리뷰 좋아요
    - 리뷰 좋아요 취소
- search
    - 상품 검색
    - 리뷰 검색
    - 좋아요 순 상품 검색
    - 좋아요 순 리뷰 검색
    - 별점 순 상품 검색
    - 인기 검색어 조회
- seller
    - 판매자 가입
    - 판매자 로그인
    - 판매자 탈퇴
- socialUser
    - 카카오 소셜 로그인
    - 조건을 만족하는 데이터로 회원가입
 
---
# Project Structure
**Domain** 패키지 
- Controller: 클라이언트의 요청을 받고 응답을 반환하는 역할. 각각의 엔드포인트에 대한 요청을 처리, 비즈니스 로직 호출.
- Dto: 클라이언트와 서버 간의 데이터 전송을 위한 객체들이 위치. 
- Model(Entity): 비즈니스 객체들을 나타내는 클래스들이 위치. 사용자(User), 제품(Product), 주문(Order) 등의 엔티티 포함.
- Repository: 데이터베이스와 상호작용하기 위한 인터페이스들이 위치. 각 엔티티에 대한 CRUD 동작을 정의하고 구현.
- Services: 비즈니스 로직을 수행하는 메소드들이 위치.

**Infra** 패키지 
- Amazon S3(Simple Storage Service) : 이미지 및 멀티미디어 콘텐츠를 저장하기 위한 Amazon S3 설정 포함.
- Security : 사용자 인증 및 권한 부여를 위한 시큐리티 설정. JWT를 통한 인증 관련 내용도 포함.
- Swagger : API 문서 자동화 및 테스트를 위한 스웨거 설정.
- Exception : 예외 처리와 관련된 클래스들을 담당. 프로그램 실행 중 발생할 수 있는 예외 상황을 처리하기 위한 클래스들 위치.





---
# Built With
 [김선병](https://github.com/Karox1234) - 팀장, 주문, 결제 시스템, 동시성 제어, 프론트

 [김보성](https://github.com/96KimBoseong)  - 부팀장, 주문, CI/CD, 프론트

 [강민지](https://github.com/mingdorri) - 팀원, 리뷰, 멀티미디어 업로드, PPT 제작

 [이 율](https://github.com/dyorcat) - 팀원, 회원가입, 소셜로그인, 에러처리, 프론트

 [홍성욱](https://github.com/suh75321) - 팀원, 상품, 좋아요, 검색
