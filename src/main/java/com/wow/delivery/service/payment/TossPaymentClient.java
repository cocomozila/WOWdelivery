package com.wow.delivery.service.payment;

import com.wow.delivery.dto.payment.TossPaymentCancelRequest;
import com.wow.delivery.dto.payment.TossPaymentsRequest;
import com.wow.delivery.error.ErrorCode;
import com.wow.delivery.error.exception.PaymentException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TossPaymentClient {

    private final WebClient webClient;

    public TossPaymentClient(@Qualifier("tossPaymentWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Retryable(backoff = @Backoff(delay = 300))
    public String requestTossFinalPayment(String uri, String transactionId, Long amount) {
        return webClient.post()
            .uri(uri)
            .bodyValue(new TossPaymentsRequest(transactionId, amount))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @Retryable(backoff = @Backoff(delay = 300))
    public String requestTossCancelPayment(String uri, String cancelReason) {
        String cancelResponse = webClient.post()
            .uri(uri)
            .bodyValue(new TossPaymentCancelRequest(cancelReason))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        if (cancelResponse == null) {
            throw new PaymentException(ErrorCode.INVALID_PARAMETER, "잘못된 API 요청 입니다.");
        }

        return cancelResponse;
    }
}
