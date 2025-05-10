package com.smartsub.controller.purchase;

import com.smartsub.dto.purchase.PurchaseRequest;
import com.smartsub.dto.purchase.PurchaseResponse;
import com.smartsub.service.purchase.PurchaseHistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // RestController 어노테이션을 사용하여 RESTful API를 제공하는 컨트롤러로 설정
@RequestMapping("/api/purchases") // API의 기본 URL 경로를 설정
@RequiredArgsConstructor // 생성자 주입을 위한 어노테이션
public class PurchaseHistoryController {

    private final PurchaseHistoryService purchaseHistoryService; // 구매 이력 정보를 처리하는 서비스

    @PostMapping
    public ResponseEntity<String> createPurchase(@RequestBody PurchaseRequest request) {
        Long id = purchaseHistoryService.createPurchase(request); // 구매 이력 등록 서비스 호출
        return ResponseEntity.ok("구매 이력 저장 완료 (ID: " + id + ")"); // 등록 성공 시 200 응답 반환
    }

    @GetMapping("/member/{memberId}/purchases")
    public ResponseEntity<List<PurchaseResponse>> getMemberPurchases(@PathVariable Long memberId) {
        return ResponseEntity.ok(purchaseHistoryService.findByMemberId(memberId)); // 회원 ID로 구매 이력 조회
    }
}