package com.example.lesson7_spring_data.entity.orders_entity;

import com.example.lesson7_spring_data.entity.product_entity.Product;
import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Column(length = 128)
    private Integer qty;

    @Column(length = 128)
    private Integer price;

    @Column(length = 128)
    private Long totalPrice;

    @CreationTimestamp
    @Column(length = 128)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(length = 128)
    private LocalDateTime updateAt;

    public OrderItem(
            ProductRepr productRepr,
            Orders order,
            Integer qty,
            Integer price,
            Long totalPrice
    ) {

        this.product = new Product(productRepr);
        this.order = order;
        this.qty = qty;
        this.price = price;
        this.totalPrice = totalPrice;
        this.createAt = product.getCreateAt();
        this.updateAt = product.getUpdateAt();
    }

    public OrderItem(ProductRepr productRepr, Orders order, Integer qty, Integer price, Long totalPrice, LocalDateTime createAt, LocalDateTime updateAt) {
        this.product = new Product(productRepr);
        this.order = order;
        this.qty = qty;
        this.price = price;
        this.totalPrice = totalPrice;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public OrderItem(OrderItem orderItem) {
        this.product = orderItem.getProduct();
        this.order = orderItem.getOrder();
        this.qty = orderItem.getQty();
        this.price = orderItem.getPrice();
        this.totalPrice = orderItem.getTotalPrice();
        this.createAt = orderItem.getProduct().getCreateAt();
        this.updateAt = orderItem.getProduct().getUpdateAt();
    }
}
