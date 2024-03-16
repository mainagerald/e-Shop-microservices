package com.myServices.orderService.service;


import com.myServices.orderService.dto.InventoryResponse;
import com.myServices.orderService.dto.OrderLineItemsDTO;
import com.myServices.orderService.dto.OrderRequest;
import com.myServices.orderService.model.Order;
import com.myServices.orderService.model.OrderLineItems;
import com.myServices.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

//        collect all skucodes within the order
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        System.out.println("codes: "+ skuCodes);

//        check inventory for product first!
        InventoryResponse[] inventoryResponses =webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block(); //to make it sync and not async
        assert inventoryResponses != null;
        System.out.println("responses from inventory: "+Arrays.toString(inventoryResponses));
//convert array to stream to check availability for all products
        boolean AllProductsInStock=Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (AllProductsInStock){
            log.info("inside loop");
            orderRepository.save(order);

        }else {
            throw new IllegalArgumentException("Product out of stock! Try again later.");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());

        return orderLineItems;
    }
}
