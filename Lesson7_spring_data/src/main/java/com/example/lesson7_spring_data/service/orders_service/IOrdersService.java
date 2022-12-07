package com.example.lesson7_spring_data.service.orders_service;

import com.example.lesson7_spring_data.entity.user_entity.User;

public interface IOrdersService {
    void createOrder(User user, String phone, String address, Integer sumInCart);

}
