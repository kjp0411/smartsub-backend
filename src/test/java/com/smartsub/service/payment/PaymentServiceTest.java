package com.smartsub.service.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.payment.Payment;
import com.smartsub.domain.payment.PaymentStatus;
import com.smartsub.domain.product.Product;
import com.smartsub.dto.payment.PaymentRequest;
import com.smartsub.dto.payment.PaymentResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.payment.PaymentRepository;
import com.smartsub.repository.product.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentServiceTest {
    private PaymentService paymentService;
    private PaymentRepository paymentRepository;
    private MemberRepository memberRepository;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        memberRepository = mock(MemberRepository.class);
        productRepository = mock(ProductRepository.class);
        paymentService = new PaymentService(paymentRepository, memberRepository, productRepository);
    }

    @Test
    void 결제_등록_성공() {
        Member member = Member.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        PaymentRequest request = new PaymentRequest(1L, 25000, "카드");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(paymentRepository.save(any())).thenReturn(
            Payment.builder()
                .id(1L)
                .member(member)
                .product(product)
                .amount(25000)
                .paymentMethod("카드")
                .status(PaymentStatus.SUCCESS)
                .build()
        );

        PaymentResponse result = paymentService.createPayment(request, 1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
    }

    @Test
    void 결제_조회_성공() {
        Member member = Member.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        Payment payment = Payment.builder()
            .id(1L)
            .member(member)
            .product(product)
            .amount(25000)
            .paymentMethod("카드")
            .status(PaymentStatus.SUCCESS)
            .build();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponse result = paymentService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPaymentMethod()).isEqualTo("카드");
    }

    @Test
    void 회원별_결제_조회_성공() {
        Member member = Member.builder().id(1L).build();
        Product product = Product.builder().id(1L).build();
        Payment payment = Payment.builder()
            .id(1L)
            .member(member)
            .product(product)
            .amount(30000)
            .paymentMethod("계좌번호")
            .status(PaymentStatus.SUCCESS)
            .build();

        when(paymentRepository.findByMemberId(1L)).thenReturn(List.of(payment));

        List<PaymentResponse> results = paymentService.findByMemberId(1L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAmount()).isEqualTo(30000);
    }
}
