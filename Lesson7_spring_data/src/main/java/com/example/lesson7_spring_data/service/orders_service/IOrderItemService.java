package com.example.lesson7_spring_data.service.orders_service;

import com.example.lesson7_spring_data.entity.orders_entity.OrderItem;

public interface IOrderItemService {

    void save(OrderItem orderItem);
}
