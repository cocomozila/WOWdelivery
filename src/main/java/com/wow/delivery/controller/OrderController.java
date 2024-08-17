package com.wow.delivery.controller;

import com.wow.delivery.dto.order.OrderAcceptDTO;
import com.wow.delivery.dto.order.OrderCancelDTO;
import com.wow.delivery.dto.order.OrderCreateDTO;
import com.wow.delivery.dto.order.OrderResponse;
import com.wow.delivery.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid OrderCreateDTO createDTO) {
        orderService.createOrder(createDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> cancelOrder(@RequestBody OrderCancelDTO cancelDTO) {
        orderService.cancelOrder(cancelDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/accecpt")
    public void acceptOrder(@RequestBody @Valid OrderAcceptDTO orderAcceptDTO) {
        orderService.acceptOrder(orderAcceptDTO);
    }

    @PostMapping("/reject")
    public void rejectOrder(@RequestBody @Valid OrderAcceptDTO orderAcceptDTO) {
        orderService.rejectOrder(orderAcceptDTO);
    }
}
