package com.example.lesson7_spring_data.service.orders_service;

import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.entity.orders_entity.OrderItem;
import com.example.lesson7_spring_data.entity.orders_entity.Orders;
import com.example.lesson7_spring_data.entity.user_entity.User;
import com.example.lesson7_spring_data.repository.orders_repository.IOrdersRepository;
import com.example.lesson7_spring_data.service.lineItem_service.ILineItemService;
import com.example.lesson7_spring_data.service.product_service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersService implements IOrdersService {

    private ILineItemService lineItemService;
    private IProductService productService;

    private IOrdersRepository ordersRepository;

    private IOrderItemService orderItemService;

    @Autowired
    public OrdersService(
            ILineItemService lineItemService,
            IProductService productService,
            IOrdersRepository ordersRepository,
            IOrderItemService orderItemService
    ) {

        this.lineItemService = lineItemService;
        this.productService = productService;
        this.ordersRepository = ordersRepository;
        this.orderItemService = orderItemService;
    }

    @Transactional
    @Override
    public void createOrder(User user, String phone, String address, Integer sumInCart) {

        List<LineItem> lineItems = lineItemService.findAllItems(user.getId());
        Orders order = new Orders();

        List<OrderItem> orderItems = lineItems
                .stream()
                .map(
                        lineItem -> new OrderItem(
                                productService.findById(lineItem.getProduct().getId()).orElseThrow(),
                                order,
                                lineItem.getQty(),
                                lineItem.getProduct().getPrice(),
                                lineItem.getProduct().getTotalPrice(),
                                lineItem.getCreateAt(),
                                lineItem.getUpdateAt()

                        )).collect(Collectors.toList());

        orderItems.forEach(orderItem -> orderItemService.save(orderItem));

        order.setUser(user);
        order.setPhone(phone);
        order.setAddress(address);
        order.setItems(orderItems);
        order.setTotalPrice(sumInCart);

        ordersRepository.save(order);
    }
}
