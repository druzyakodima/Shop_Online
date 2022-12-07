package com.example.lesson7_spring_data.soap.service;

import com.example.lesson7_spring_data.entity.product_entity.Product;
import com.example.lesson7_spring_data.repository.product_repository.ProductsRepository;
import com.example.lesson7_spring_data.soap.product.ProductEntity;
import com.example.lesson7_spring_data.soap.repository.ProductEntityRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Service
public class ProductEntityService {
    private ProductEntityRepository productsRepository;

    public static final Function<Product, ProductEntity> functionEntityToSoap = se -> {
        ProductEntity productEntity = new ProductEntity();

        productEntity.setId(se.getId());
        productEntity.setProductId(se.getProductId());
        productEntity.setTitle(se.getTitle());
        productEntity.setPrice(se.getPrice());

        return productEntity;
    };

    @Autowired
    public ProductEntityService(ProductEntityRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<ProductEntity> getAllProducts() {
        return productsRepository.findAll().stream().map(functionEntityToSoap).collect(Collectors.toList());
    }
}
