package com.example.lesson7_spring_data.service.orders_service;

import com.example.lesson7_spring_data.entity.orders_entity.OrderItem;
import com.example.lesson7_spring_data.repository.orders_repository.IOrdersItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServer implements IOrderItemService{

    private IOrdersItemRepository ordersItemRepository;

    public OrderItemServer(IOrdersItemRepository ordersItemRepository) {
        this.ordersItemRepository = ordersItemRepository;
    }

    @Override
    public void save(OrderItem orderItem) {
        ordersItemRepository.save(orderItem);
    }
}
