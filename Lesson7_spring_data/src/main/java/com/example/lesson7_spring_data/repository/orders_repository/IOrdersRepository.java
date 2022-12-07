package com.example.lesson7_spring_data.repository.orders_repository;

import com.example.lesson7_spring_data.entity.orders_entity.OrderItem;
import com.example.lesson7_spring_data.entity.orders_entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IOrdersRepository extends JpaRepository<Orders, Long> {
}
