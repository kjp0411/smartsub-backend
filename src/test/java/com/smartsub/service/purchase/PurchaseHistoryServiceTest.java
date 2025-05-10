package com.smartsub.service.purchase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import com.smartsub.domain.purchase.PurchaseHistory;
import com.smartsub.dto.purchase.PurchaseRequest;
import com.smartsub.dto.purchase.PurchaseResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.repository.purchase.PurchaseHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseHistoryServiceTest {

    private PurchaseHistoryService purchaseService;
    private PurchaseHistoryRepository purchaseRepository;
    private MemberRepository memberRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        purchaseRepository = mock(PurchaseHistoryRepository.class);
        memberRepository = mock(MemberRepository.class);
        productRepository = mock(ProductRepository.class);
        purchaseService = new PurchaseHistoryService(purchaseRepository, memberRepository, productRepository);
    }

    @Test
    void 구매_이력_등록_성공() {
        // given
        Member member = Member.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        PurchaseRequest request = new PurchaseRequest(1L, 1L, LocalDateTime.now(), 2, 10000);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(purchaseRepository.save(any())).thenReturn(PurchaseHistory.builder().id(1L).build());

        // when
        Long id = purchaseService.createPurchase(request);

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 회원_구매_이력_조회_성공() {
        // given
        Member member = Member.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        PurchaseHistory history = PurchaseHistory.builder()
            .id(1L)
            .member(member)
            .product(product)
            .purchasedAt(LocalDateTime.now())
            .quantity(3)
            .price(9000)
            .build();

        when(purchaseRepository.findByMemberId(1L)).thenReturn(List.of(history));

        // when
        List<PurchaseResponse> responses = purchaseService.findByMemberId(1L);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getProductId()).isEqualTo(1L);
        assertThat(responses.get(0).getQuantity()).isEqualTo(3);
    }
}
