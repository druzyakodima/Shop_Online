package com.example.lesson7_spring_data.controllers;

import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.User;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import com.example.lesson7_spring_data.exception.NotFoundException;
import com.example.lesson7_spring_data.security.UserAuthService;
import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.service.product_service.IProductService;
import com.example.lesson7_spring_data.service.redis_service.IRedisService;
import com.example.lesson7_spring_data.service.user_service.IUserService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@NoArgsConstructor
@Controller
@RequestMapping("/cart")
public class CartController {

    private IProductService productService;
    private UserAuthService userAuthService;
    private IRedisService redisService;
    private IUserService userService;

    @Autowired
    public CartController(IProductService productService,
                          UserAuthService userAuthService,
                          IRedisService redisService,
                          IUserService userService) {

        this.productService = productService;
        this.userAuthService = userAuthService;
        this.redisService = redisService;
        this.userService = userService;
    }

    @GetMapping()
    public String showCart(Model model) {

        log.info("Looked at the products in the cart");
        User user = userAuthService.getCurrentUser();

        List<LineItem> listCart = redisService.findAllItems(user.getId());
        Integer totalPrice = redisService.totalPrice(user.getId());

        model.addAttribute("cart", listCart);
        model.addAttribute("totalPrice", totalPrice);

        return "cart_templates/cart";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productId") Long productId) {

        log.info("Add product in cart");

        User user = userAuthService.getCurrentUser();

        ProductRepr productRepr = productService.findById(productId).orElseThrow(() -> new NotFoundException("Продукт не найден" + productId));
        UserRepr userRepr = userService.findById(user.getId()).orElseThrow(() -> new NotFoundException("Потребитель не найден" + productId));

        redisService.addProductForUser(productRepr, userRepr);

        return "redirect:/product";
    }

    @PostMapping("/delete")
    public String deleteProductFromCart(@RequestParam("productId") Long productId) {

        log.info("Product delete request from shopping cart");

        ProductRepr productRepr = productService.findById(productId).orElseThrow(() -> new NotFoundException("Продукт не найден" + productId));
        UserRepr userRepr = new UserRepr(userAuthService.getCurrentUser());

        redisService.removeProductForUser(productRepr, userRepr);

        return "redirect:/cart";
    }

    @DeleteMapping("/clearCart")
    public String clearCartForUser() {

        User user = userAuthService.getCurrentUser();
        redisService.clearCart(user.getId());

        return "redirect:/cart";
    }
}
