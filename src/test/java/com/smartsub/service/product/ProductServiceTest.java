package com.smartsub.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.smartsub.domain.product.Product;
import com.smartsub.repository.product.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 상품등록_성공() {
        // given
        Product product = Product.builder()
            .name("크리넥스 3겹")
            .category("화장지")
            .productCode("12345678")
            .unit("1개")
            .imageUrl("http://example.com/image.jpg")
            .build();

        given(productRepository.save(product)).willReturn(product);

        // when
        Long result = productService.createProduct(product);

        // then
        assertThat(result).isEqualTo(product.getId());
    }

    @Test
    void 상품조회_성공() {
        // given
        Product product = Product.builder()
            .name("크리넥스 3겹")
            .category("화장지")
            .productCode("12345678")
            .unit("1개")
            .imageUrl("http://example.com/image.jpg")
            .build();

        given(productRepository.findById(1L)).willReturn(java.util.Optional.of(product));

        // when
        var response = productService.findById(1L);

        // then
        assertThat(response.getName()).isEqualTo("크리넥스 3겹");
        assertThat(response.getCategory()).isEqualTo("화장지");
    }

    @Test
    void 상품_삭제_성공() {
        // given
        Long productId = 1L;
        Product product = Product.builder()
            .id(productId)
            .name("삭제할 상품")
            .build();

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        // when
        productService.deleteProduct(productId);

        // then
        verify(productRepository).delete(product);
    }

    @Test
    void 상품_삭제_실패_존재하지않는_상품() {
        // given
        Long productId = 999L;
        given(productRepository.findById(productId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.deleteProduct(productId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("해당 상품이 존재하지 않습니다.");
    }

    @Test
    void 상품코드_중복_예외() {
        // given
        Product product = Product.builder()
            .name("중복상품")
            .productCode("DUPLICATE123")
            .build();

        given(productRepository.existsByProductCode("DUPLICATE123")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> productService.createProduct(product))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("이미 존재하는 상품 코드입니다.");
    }
}