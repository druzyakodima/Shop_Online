package com.example.lesson7_spring_data.service;

import com.example.lesson7_spring_data.entity.ProductRepr;
import com.example.lesson7_spring_data.repository.ProductSpecification;
import com.example.lesson7_spring_data.repository.ProductsRepository;
import com.example.lesson7_spring_data.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Service
public class ProductService implements IProductService {

    private ProductsRepository productRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository) {
        this.productRepository = productsRepository;
    }

    @Transactional
    @Override
    public void save(ProductRepr productRepr) {
        Product product = new Product(productRepr);
        productRepository.save(product);
        if (productRepr.getId() == null) productRepr.setId(product.getId());
    }


    @Transactional
    @Override
    public Optional<ProductRepr> findById(Long id) {
        return productRepository.findById(id).map(ProductRepr::new);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductRepr> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductRepr::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductRepr> findWithFilter(Integer priceMinFilter,
                                            Integer priceMaxFilter,
                                            Integer currentPage,
                                            Integer countPage) {

        Specification<Product> spec = Specification.where(null);
        if (priceMinFilter != null) {
            spec = spec.and(ProductSpecification.priceMin(priceMinFilter));
        }
        if (priceMaxFilter != null) {
            spec = spec.and(ProductSpecification.priceMax(priceMaxFilter));
        }

        return productRepository.findAll(spec, PageRequest.of(currentPage, countPage)).map(ProductRepr::new);
    }

    public ProductService() {
    }

}
