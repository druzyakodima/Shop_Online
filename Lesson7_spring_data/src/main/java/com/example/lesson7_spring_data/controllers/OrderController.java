package com.example.lesson7_spring_data.controllers;

import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.entity.orders_entity.OrderItem;
import com.example.lesson7_spring_data.entity.orders_entity.Orders;
import com.example.lesson7_spring_data.entity.user_entity.User;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import com.example.lesson7_spring_data.exception.NotFoundException;
import com.example.lesson7_spring_data.security.UserAuthService;
import com.example.lesson7_spring_data.service.lineItem_service.ILineItemService;
import com.example.lesson7_spring_data.service.orders_service.IOrdersService;
import com.example.lesson7_spring_data.service.user_service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {

    private IOrdersService ordersService;
    private ILineItemService lineItemService;
    private UserAuthService userAuthService;

    @Autowired
    public OrderController(
            IOrdersService ordersService,
            UserAuthService userAuthService,
            ILineItemService lineItemService
    ) {

        this.ordersService = ordersService;
        this.userAuthService = userAuthService;
        this.lineItemService = lineItemService;
    }

    @PostMapping
    public String createOrder(Model model) {

        log.info("Create new order request");

        User user = userAuthService.getCurrentUser();
        List<LineItem> allItems = lineItemService.findAllItems(user.getId());
        Integer sumInCart = lineItemService.sumInCart(user.getId());

        model.addAttribute("sumInCart", sumInCart);
        model.addAttribute("orders", allItems);
        model.addAttribute("order", new Orders());


        return "order_templates/order";
    }

    @PostMapping("/create")
    public String placeOnOrder(@ModelAttribute("phone") String phone,
                               @ModelAttribute("address") String address) {

        User user = userAuthService.getCurrentUser();
        Integer sumInCart = lineItemService.sumInCart(user.getId());
        ordersService.createOrder(user, phone, address, sumInCart);
        lineItemService.clearCart(user.getId());

        return "order_templates/placed";
    }
}
