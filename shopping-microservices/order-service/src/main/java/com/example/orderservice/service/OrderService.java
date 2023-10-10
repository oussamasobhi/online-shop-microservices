package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientbuilder;
    public String  placeorder(OrderRequest orderRequest){
        Order order=new Order();
        order.setOrdernNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(this::maptoDto)
                .toList();

        order.setOrderLineItems(orderLineItems);

        List<String> skucodes=order.getOrderLineItems().stream()
                .map(OrderLineItems::getSkuCode).toList();

        //check if product is in inventory
        InventoryResponse[] inventoryResponses= webClientbuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        UriBuilder-> UriBuilder.queryParam("skuCode",skucodes).build())
                        .retrieve()
                                .bodyToMono(InventoryResponse[].class)
                                        .block();

        boolean allproductsinStock= Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isIsinstock);

        if(allproductsinStock) {
            orderRepository.save(order);
            return "order places successuully";
        }
        else
            throw new IllegalArgumentException("product is not in stock");

    }

    private OrderLineItems maptoDto(OrderLineItemsDto orderLineItemsDto) {
            OrderLineItems orderLineItems=new OrderLineItems();
            orderLineItems.setPrice(orderLineItemsDto.getPrice());
            orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
            orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
            return orderLineItems;
    }
}
