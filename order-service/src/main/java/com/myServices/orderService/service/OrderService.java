package com.myServices.orderService.service;


import com.myServices.orderService.dto.OrderLineItemsDTO;
import com.myServices.orderService.dto.OrderRequest;
import com.myServices.orderService.model.Order;
import com.myServices.orderService.model.OrderLineItems;
import com.myServices.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        orderLineItems.setPrice(orderLineItems.getPrice());

        return orderLineItems;
    }
}
