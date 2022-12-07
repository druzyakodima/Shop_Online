package com.example.lesson7_spring_data.service.lineItem_service;

import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import com.example.lesson7_spring_data.repository.lineItem_repository.ILineItemRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Service
public class LineItemService implements ILineItemService {

    private ILineItemRepository lineItemRepository;

    @Autowired
    public LineItemService(ILineItemRepository lineItemRepository) {
        this.lineItemRepository = lineItemRepository;
    }

    public LineItemService() {
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

                    var newPrice = item.getProduct().getTotalPrice() + productRepr.getPrice();
                    item.getProduct().setTotalPrice(newPrice);

                    lineItemRepository.save(item);
                    break;
                }
            }

        } else {
            LineItem lineItem = new LineItem(productRepr, userRepr, 1);
            lineItemRepository.save(lineItem);
        }

    }

    @Override
    public void removeProductForUser(ProductRepr product, UserRepr user) {

        List<LineItem> allItems = findAllItems(user.getId());

        for (LineItem lineItem : allItems) {

            if (lineItem.getProduct().equals(product)) {
                var qty = lineItem.getQty();

                if (qty == 1) {
                    lineItemRepository.delete(lineItem);
                } else {

                    lineItem.setQty(--qty);

                    var newPrice = lineItem.getProduct().getTotalPrice() - product.getPrice();
                    lineItem.getProduct().setTotalPrice(newPrice);

                    lineItemRepository.save(lineItem);
                }

                break;
            }

        }
    }

    @Override
    public void clearCart(long userId) {
        List<LineItem> allItems = findAllItems(userId);
        lineItemRepository.deleteAll(allItems);
        sumInCart(userId);
    }

    @Override
    public int sumInCart(long userId) {

        List<LineItem> allItems = findAllItems(userId);
        var totalPrice = 0;

        for (LineItem allItem : allItems) {
            totalPrice += allItem.getProduct().getTotalPrice();
        }

        return totalPrice;
    }

    @Override
    public List<LineItem> findAllItems(long userId) {
        ArrayList<LineItem> lineItems = new ArrayList<>();

        lineItemRepository.findAll().forEach(e -> {
            if (e.getUser().getId().equals(userId)) {
                lineItems.add(e);
            }
        });

        return lineItems;
    }

}
