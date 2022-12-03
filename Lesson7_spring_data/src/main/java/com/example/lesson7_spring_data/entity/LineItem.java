package com.example.lesson7_spring_data.entity;

import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@RedisHash("lineItem")
public class LineItem implements Serializable {

    @Id
    private Long id;
    @Indexed
    private ProductRepr product;
    private UserRepr user;
    private Integer qty;

    @CreationTimestamp
    private LocalDateTime createAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;


    public LineItem(ProductRepr product, UserRepr user, Integer qty) {
        this.product = product;
        this.user = user;
        this.qty = qty;
    }


    public LineItem(long productId, long userId) {
        this.product = new ProductRepr();
        this.product.setId(productId);
        this.user = new UserRepr();
        this.user.setId(userId);
    }

    public LineItem() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return product.getId().equals(lineItem.product.getId()) && user.getId().equals(lineItem.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId(), user.getId());
    }

}
