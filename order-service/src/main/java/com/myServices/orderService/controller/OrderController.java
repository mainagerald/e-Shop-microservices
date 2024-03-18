package com.myServices.orderService.controller;

import com.myServices.orderService.dto.OrderRequest;
import com.myServices.orderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order placed successfully!";
    }

    public String fallback(OrderRequest orderRequest, RuntimeException runtimeException)
    {
        return "Uh oh! Something went wrong. Try again shortly.";
    }
}
