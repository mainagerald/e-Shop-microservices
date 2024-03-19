package com.myServices.orderService.controller;

import com.myServices.orderService.dto.OrderRequest;
import com.myServices.orderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
//    @TimeLimiter(name = "inventory")
//    @Retry(name="inventory")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        return orderService.placeOrder(orderRequest); //allow for timeouts, etc
    }
    public CompletableFuture<String> fallback(OrderRequest orderRequest, RuntimeException runtimeException)
    {
        return CompletableFuture.supplyAsync(()->"Uh oh! Something went wrong. Try again shortly.");
    }
}
