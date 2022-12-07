package com.example.lesson7_spring_data.repository.orders_repository;

import com.example.lesson7_spring_data.entity.orders_entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdersItemRepository extends JpaRepository<OrderItem, Long> {
}
