package com.example.lesson7_spring_data.rest;


import com.example.lesson7_spring_data.entity.user_entity.User;
import com.example.lesson7_spring_data.exception.NotFoundException;
import com.example.lesson7_spring_data.service.lineItem_service.ILineItemService;
import com.example.lesson7_spring_data.service.orders_service.IOrdersService;
import com.example.lesson7_spring_data.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
public class OrderResource {

    private IUserService userService;
    private IOrdersService ordersService;

    private ILineItemService lineItemService;

    @Autowired
    public OrderResource(IUserService userService, IOrdersService ordersService) {
        this.userService = userService;
        this.ordersService = ordersService;
    }

    @PostMapping("/orders/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(
            @PathVariable("userId") Long userId,
            @PathVariable("phone") String phone,
            @PathVariable("address") String address
    ) {
        User user = userService.findByIdUser(userId).orElseThrow(() -> new NotFoundException("Такой пользовательне найден"));
        Integer sumInCart = lineItemService.sumInCart(user.getId());

        ordersService.createOrder(user, phone, address, sumInCart);
    }
}
