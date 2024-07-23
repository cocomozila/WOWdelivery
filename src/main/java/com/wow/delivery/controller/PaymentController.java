package com.wow.delivery.controller;

import com.wow.delivery.dto.payment.PaymentCancelRequest;
import com.wow.delivery.dto.payment.PaymentFailDTO;
import com.wow.delivery.dto.payment.PaymentRequest;
import com.wow.delivery.dto.payment.PaymentResponse;
import com.wow.delivery.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> requestPayment(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.ok(paymentService.requestPayment(request));
    }

    @GetMapping("/success")
    public ResponseEntity<String> successPayment(@RequestParam("paymentKey") String paymentKey,
                               @RequestParam("orderId") String transactionId,
                               @RequestParam("amount") Long amount) {
        paymentService.verifyRequest(paymentKey, transactionId, amount);
        String responseBody = paymentService.requestFinalPayment(paymentKey, transactionId, amount);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/fail")
    public ResponseEntity<PaymentFailDTO> failPayment(@RequestParam("code") String errorCode,
                                                      @RequestParam("message") String errorMessage,
                                                      @RequestParam("orderId") String transactionId) {
        return ResponseEntity.ok(paymentService.requestFail(errorCode, errorMessage, transactionId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> requestPaymentCancel(@RequestBody @Valid PaymentCancelRequest request) {
        return ResponseEntity.ok(paymentService.cancelPayment(request));
    }
}
