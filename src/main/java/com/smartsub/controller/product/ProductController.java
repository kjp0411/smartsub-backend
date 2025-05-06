package com.smartsub.controller.product;

import com.smartsub.dto.product.ProductRequest;
import com.smartsub.dto.product.ProductResponse;
import com.smartsub.dto.product.ProductUpdateRequest;
import com.smartsub.service.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // PostMapping, RequestBody 추가

@RestController // RestController 어노테이션을 사용하여 RESTful API를 제공하는 컨트롤러로 설정
@RequestMapping("/api/products") // API의 기본 URL 경로를 설정
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class ProductController {

    private final ProductService productService; // 상품 정보를 처리하는 서비스

    @GetMapping
    public List<ProductResponse> getAllProducts() { // 모든 상품 정보를 조회하는 API 엔드포인트
        return productService.findAll(); // 서비스에서 모든 상품 정보를 조회하여 반환
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) { // 상품 ID로 상품 정보를 조회하는 API 엔드포인트
        return productService.findById(id); // 서비스에서 상품 ID로 상품 정보를 조회하여 반환
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) { // 상품 정보를 등록하는 API 엔드포인트
        Long id = productService.createProduct(request.toEntity()); // DTO를 엔티티로 변환하여 서비스에 전달
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("상품 등록 완료 (ID: " + id + ")"); // 등록 성공 시 201 응답 반환
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductUpdateRequest request
    ) {
        productService.updateProduct(id, request); // 상품 정보를 업데이트하는 서비스 호출
        return ResponseEntity.ok("상품 정보 수정 완료"); // 수정 성공 시 200 응답 반환
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); // 상품 정보를 삭제하는 서비스 호출
        return ResponseEntity.ok("상품 삭제 완료"); // 삭제 성공 시 200 응답 반환
    }
}
