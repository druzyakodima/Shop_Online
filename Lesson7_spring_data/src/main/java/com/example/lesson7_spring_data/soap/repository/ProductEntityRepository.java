package com.example.lesson7_spring_data.soap.repository;

import com.example.lesson7_spring_data.entity.product_entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductEntityRepository extends JpaRepository<Product, Long> {
}
