package com.smartsub.repository.payment;

import com.smartsub.domain.payment.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // JpaRepository를 상속받아 기본적인 CRUD 메서드를 사용할 수 있습니다.
    // 추가적인 쿼리 메서드는 필요에 따라 정의할 수 있습니다.
    List<Payment> findByMemberId(Long memberId);
}
