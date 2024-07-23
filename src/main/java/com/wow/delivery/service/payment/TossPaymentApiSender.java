package com.wow.delivery.service.payment;

import com.wow.delivery.dto.payment.TossPaymentCancelRequest;
import com.wow.delivery.dto.payment.TossPaymentsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class TossPaymentApiSender {

    private final WebClient webClient;

    public String requestTossFinalPayment(String uri, String auth, String transactionId, Long amount) {
        return webClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Basic " + auth)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(new TossPaymentsRequest(transactionId, amount))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String requestTossCancelPayment(String uri, String auth, String cancelReason) {
        return webClient.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Basic " + auth)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .bodyValue(new TossPaymentCancelRequest(cancelReason))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
