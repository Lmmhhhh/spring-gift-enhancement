# spring-gift-enhancement

# [step1] Entity Mapping

**과제 요구 사항**: 
- 기존 JDBC 코드 → JPA 기반으로 리팩터링
- 객체 참조 기반 연관관계 매핑

## 구현 기능 목록

### Product
- [x] JPA 기반 ProductRepository 구현
- [x] @DataJpaTest로 ProductRepository 테스트 작성

### Member
- [x] JPA 기반 MemberRepository 구현
- [x] @DataJpaTest로 MemberRepository 테스트 작성

### Wish
- [x] JPA 기반 WishRepository 구현
- [x] @DataJpaTest로 WishRepository 테스트 작성

# [step2] 페이지네이션

**과제 요구 사항**: 
- 상품과 위시리스트 보기 기능에 페이지네이션 기능 추가

## 구현 기능 목록
- [ ] 공통 response 포맷 작성
- [ ] `/products` API에 Pageable 적용
- [ ] `/wishes` API에 Pageable 적용
- [ ] 페이지네이션 테스트 작성
