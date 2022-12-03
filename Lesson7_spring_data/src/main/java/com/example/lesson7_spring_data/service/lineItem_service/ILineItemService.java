package com.example.lesson7_spring_data.service.lineItem_service;

import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;

import java.util.List;

public interface ILineItemService {

    void addProductForUser(ProductRepr productRepr, UserRepr userRepr);

    void removeProductForUser(ProductRepr productId, UserRepr useId);

    int sumInCart(long userId);

    List<LineItem> findAllItems(long userId);

    void clearCart(long userId);
}
