package gift;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtProvider;
import gift.config.WebConfig;
import gift.controller.ProductController;
import gift.dto.request.ProductRequest;
import gift.dto.response.ProductResponse;
import gift.service.MemberService;
import gift.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(ProductControllerTest.TestConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("상품명 15자 초과 시 400BAD_REQUEST 에러가 발생한다.")
    @Test
    void 상품명_15자_초과시_400이_반환된다() throws Exception {

        ProductRequest request = new ProductRequest(
                " 0123456789abcdef",
                1000,
                "http://image.jpg"
        );

        assertBadRequest(request, "상품명은 공백 포함 최대 15자까지 입력할 수 있습니다.");
    }

    @DisplayName("허용되지 않은 특수문자 입력 시 400BAD_REQUEST 에러가 발생한다.")
    @Test
    void 허용되지_않은_특수문자_입력시_400이_반환된다() throws Exception{

        ProductRequest request = new ProductRequest(
                " 012345!!!!",
                1000,
                "http://image.jpg"
        );

        assertBadRequest(request,"상품명에는 특수문자 (),[],+,-,&,/,_ 만 포함될 수 있습니다.");
    }

    @DisplayName("상품명에 '카카오' 포함 시 400BAD_REQUEST 에러가 발생한다.")
    @Test
    void 상품명에_카카오_포함시_400이_반환된다() throws Exception{

        ProductRequest request = new ProductRequest(
                "카카오97 초콜릿",
                1000,
                "http://image.jpg"
        );

        assertBadRequest(request,"상품명에 '카카오'를 포함할 수 없습니다. 담당자에게 문의하세요.");
    }

    @Test
    @DisplayName("상품 목록 페이지네이션 + 상품명 내림차순 정렬 응답 성공")
    void getProductsWithPaginationAndSort() throws Exception {

        List<ProductResponse> content = List.of(
                new ProductResponse(2L, "나", 2000, "img2"),
                new ProductResponse(1L, "가", 1000, "img1")
        );

        PageImpl<ProductResponse> page = new PageImpl<>(
                content,
                PageRequest.of(0, 2, Sort.by("name").descending()),
                3
        );

        when(productService.getAllProducts(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "name,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name").value("나"))
                .andExpect(jsonPath("$.content[1].name").value("가"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.hasPrevious").value(false));
    }

    @Test
    @DisplayName("상품 검색 결과 페이지네이션 + 정렬 응답 성공")
    void searchProductsWithPaginationAndSort() throws Exception {

        List<ProductResponse> content = List.of(
                new ProductResponse(2L, "배민10000원상품권", 10000, "img2"),
                new ProductResponse(1L, "배민20000원상품권", 20000, "img1")
        );

        PageImpl<ProductResponse> page = new PageImpl<>(
                content,
                PageRequest.of(0, 2, Sort.by("price").ascending()),
                3
        );

        when(productService.searchByName(eq("배민"), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/api/products/search")
                        .param("name", "배민")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "price,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].price").value(10000))
                .andExpect(jsonPath("$.content[1].price").value(20000))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.hasPrevious").value(false));
    }

    private void assertBadRequest(ProductRequest request, String expectedMessage) throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer ")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name[0]").value(expectedMessage));
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public JwtProvider jwtProvider() {
            return mock(JwtProvider.class);
        }

        @Bean
        public MemberService memberService() {
            return mock(MemberService.class);
        }

        @Bean
        public WebConfig webConfig(JwtProvider jwtProvider, MemberService memberService) {
            return new WebConfig(jwtProvider, memberService);
        }
    }
}