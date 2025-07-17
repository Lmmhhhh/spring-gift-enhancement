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
- [x] 공통 response 포맷 작성
- [x] `/products` API에 Pageable 적용
- [ ] `/wishes` API에 Pageable 적용
- [ ] 페이지네이션 테스트 작성

## 구현 기능 
**1. 상품 목록 조회 페이지네이션** (`GET /api/products?page=0&size=2&sort=price,desc`) <br>
: 0페이지 (첫 페이지), 한 페이지 당 2개 , `price` 기준 내림차순으로 정렬

### Response
```json
{
  "content": [
    {
      "id": 1,
      "name": "aaa",
      "price": 2000,
      "imageUrl": "img1"
    },
    {
      "id": 2,
      "name": "bbb",
      "price": 1500,
      "imageUrl": "img2"
    }
  ],
  "page": 0,
  "size": 2,
  "totalPages": 3,
  "totalElements": 6,
  "hasNext": true,
  "hasPrevious": false
}
```

**2.상품 검색 페이지네이션** (`GET /api/products/search?name=배민&page=0&size=2&sort=name,asc`) <br>
: `배민`이 포함된 상품명을 검색, 0페이지 (첫 페이지), 한 페이지당 2개, `name` 기준 오름차순 정렬

### Response
```json
{
  "content": [
    {
      "id": 1,
      "name": "배민10000원상품권",
      "price": 10000,
      "imageUrl": "img1"
    },
    {
      "id": 2,
      "name": "배민20000원상품권",
      "price": 20000,
      "imageUrl": "img2"
    }
  ],
  "page": 0,
  "size": 2,
  "totalPages": 2,
  "totalElements": 3,
  "hasNext": true,
  "hasPrevious": false
}
```