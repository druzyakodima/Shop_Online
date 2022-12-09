package com.example.lesson7_spring_data.service.redis_service;

import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import com.example.lesson7_spring_data.repository.redis_repository.IRedisRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Service
public class RedisService implements IRedisService {

    private IRedisRepository redisRepository;

    @Autowired
    public RedisService(IRedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public RedisService() {
    }

    @Override
    public void addProductForUser(ProductRepr productRepr, UserRepr userRepr) {

        List<LineItem> lineItems = findAllItems(userRepr.getId());

        var qty = lineItems
                .stream()
                .filter(item -> item.equals(new LineItem(productRepr.getId(), userRepr.getId())))
                .mapToInt(LineItem::getQty)
                .sum();


        if (qty >= 1) {

            for (LineItem item : lineItems) {

                if (item.getProduct().equals(productRepr)) {

                    item.setQty(Math.toIntExact(++qty));

                    var newPrice = item.getProduct().getPrice() + productRepr.getPrice();
                    item.getProduct().setPrice(newPrice);

                    redisRepository.save(item);
                    break;
                }
            }

        } else {
            LineItem lineItem = new LineItem(productRepr, userRepr, 1);
            redisRepository.save(lineItem);
        }

    }

    @Override
    public void removeProductForUser(ProductRepr product, UserRepr user) {

        List<LineItem> allItems = findAllItems(user.getId());

        for (LineItem lineItem : allItems) {

            if (lineItem.getProduct().equals(product)) {
                var qty = lineItem.getQty();

                if (qty == 1) {
                    redisRepository.delete(lineItem);
                } else {

                    lineItem.setQty(--qty);

                    var newPrice = lineItem.getProduct().getPrice() - product.getPrice();
                    lineItem.getProduct().setPrice(newPrice);

                    redisRepository.save(lineItem);
                }

                break;
            }

        }
    }

    @Override
    public void clearCart(long userId) {
        List<LineItem> allItems = findAllItems(userId);
        redisRepository.deleteAll(allItems);
        totalPrice(userId);
    }

    @Override
    public int totalPrice(long userId) {

        List<LineItem> allItems = findAllItems(userId);
        var totalPrice = 0;

        for (LineItem allItem : allItems) {
            totalPrice += allItem.getProduct().getPrice();
        }

        return totalPrice;
    }

    @Override
    public List<LineItem> findAllItems(long userId) {
        ArrayList<LineItem> lineItems = new ArrayList<>();

        redisRepository.findAll().forEach(e -> {
            if (e.getUser().getId().equals(userId)) {
                lineItems.add(e);
            }
        });

        return lineItems;
    }

}
