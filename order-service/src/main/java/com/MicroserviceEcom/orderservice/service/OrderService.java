package com.MicroserviceEcom.orderservice.service;

import com.MicroserviceEcom.orderservice.dto.InventoryResponseDto;
import com.MicroserviceEcom.orderservice.dto.OrderLineItemsDto;
import com.MicroserviceEcom.orderservice.dto.OrderRequest;
import com.MicroserviceEcom.orderservice.model.Order;
import com.MicroserviceEcom.orderservice.model.OrderLineItems;
import com.MicroserviceEcom.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        //Get the List of skuCode to Pass to the Inventory Class
        List<String> skuCode = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //call inventory service and place order if product is in stock
        InventoryResponseDto[] inventoryResponseArray= webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCode)
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponseDto[].class)
                .block();
        System.out.println(inventoryResponseArray.length);


        //Check if the product is there on stock else return error
        boolean result = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponseDto::isInStock);
        if (result) {
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException("The product is not in stock Please try again later");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems=new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuality(orderLineItemsDto.getQuality());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
