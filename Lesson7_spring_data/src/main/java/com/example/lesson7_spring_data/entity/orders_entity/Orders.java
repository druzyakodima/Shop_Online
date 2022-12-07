package com.example.lesson7_spring_data.entity.orders_entity;

import com.example.lesson7_spring_data.entity.user_entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany
    private List<OrderItem> items;

    @Column(length = 128)
    private Integer totalPrice;

    @Column(length = 128)
    private String address;

    @Column(length = 128)
    private String phone;

    @CreationTimestamp
    @Column(length = 128)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(length = 128)
    private LocalDateTime updateAt;

    public Orders(
            User user,
                  List<OrderItem> items,
                  Integer totalPrice,
                  String address,
                  String phone
    ) {

        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
        this.address = address;
        this.phone = phone;
    }

    public Orders(User user, List<OrderItem> items, Integer totalPrice, String address, String phone, LocalDateTime createAt, LocalDateTime updateAt) {
        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
        this.address = address;
        this.phone = phone;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
