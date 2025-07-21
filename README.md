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
- [x] `/wishes` API에 Pageable 적용
- [x] 페이지네이션 테스트 작성

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

**3. 위시리스트 목록 조회 페이지네이션** (`GET /api/wishes?page=1&size=2&sort=product.price,desc`) <br>
: 1페이지, 한 페이지 당 2개 , `price` 기준 내림차순으로 정렬

### Response
```json
{
  "content": [
    {
      "id": 3,
      "productId": 6,
      "productName": "멜론스트리밍권",
      "price": 7900,
      "imageUrl": "img2"
    }
  ],
  "page": 1,
  "size": 2,
  "totalElements": 3,
  "hasNext": false,
  "hasPrevious": true,
  "totalPages": 2
}
```

# [step3] 상품 옵션

## 구현 기능 목록
- [x] Option 엔티티 추가 및 Product와 연관관계 매핑(1:N)
- [x] 상품에 옵션 1개 이상 필수 조건 적용
- [x] 옵션 이름 제약(최대 50자, 허용 특수문자만 사용 가능)
- [x] 상품 내 옵션 이름 중복 등록 방지
- [x] 옵션 수량 제약 (1이상, 1억 미만)
- [x] 수량 차감 기능 및 예외 처리
- [x] 옵션 목록 조회 api 구현
- [ ] 상품 옵션 테스트 작성 