package com.smartsub.service.purchase;

import com.smartsub.domain.member.Member;
import com.smartsub.domain.product.Product;
import com.smartsub.domain.purchase.PurchaseHistory;
import com.smartsub.dto.purchase.PurchaseRequest;
import com.smartsub.dto.purchase.PurchaseResponse;
import com.smartsub.repository.member.MemberRepository;
import com.smartsub.repository.product.ProductRepository;
import com.smartsub.repository.purchase.PurchaseHistoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository; // 구매 이력 정보를 저장하는 레포지토리
    private final MemberRepository memberRepository; // 회원 정보를 저장하는 레포지토리
    private final ProductRepository productRepository; // 상품 정보를 저장하는 레포지토리

    public Long createPurchase(PurchaseRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")); // 회원 정보 조회

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다.")); // 상품 정보 조회

        PurchaseHistory history = PurchaseHistory.builder()
            .member(member) // 회원 정보 설정
            .product(product) // 상품 정보 설정
            .purchasedAt(request.getPurchasedAt()) // 구매 날짜 설정
            .quantity(request.getQuantity()) // 구매 수량 설정
            .price(request.getPrice()) // 구매 가격 설정
            .build();

        return purchaseHistoryRepository.save(history).getId(); // 구매 이력 저장 후 생성된 ID 반환
    }

    public List<PurchaseResponse> findByMemberId(Long memberID) {
        return purchaseHistoryRepository.findByMemberId(memberID).stream()
            .map(PurchaseResponse::from) // 구매 이력 정보를 PurchaseResponse로 변환
            .collect(Collectors.toList()); // 리스트로 변환
    }
}
