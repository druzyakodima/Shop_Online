package com.example.lesson7_spring_data.controllers;

import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.User;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import com.example.lesson7_spring_data.exception.NotFoundException;
import com.example.lesson7_spring_data.security.UserAuthService;
import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.service.product_service.IProductService;
import com.example.lesson7_spring_data.service.lineItem_service.ILineItemService;
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
    private ILineItemService lineItemService;
    private IUserService userService;

    @Autowired
    public CartController(IProductService productService,
                          UserAuthService userAuthService,
                          ILineItemService lineItemService,
                          IUserService userService) {

        this.productService = productService;
        this.userAuthService = userAuthService;
        this.lineItemService = lineItemService;
        this.userService = userService;
    }

    @GetMapping()
    public String showCart(Model model) {

        User user = userAuthService.getCurrentUser();

        List<LineItem> listCart = lineItemService.findAllItems(user.getId());
        Integer sumInCart = lineItemService.sumInCart(user.getId());

        model.addAttribute("cart", listCart);
        model.addAttribute("sumInCart", sumInCart);

        log.info( "User id: " + user.getId() + " looked at the products in the cart");
        return "cart_templates/cart";
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productId") Long productId) {


        User user = userAuthService.getCurrentUser();

        ProductRepr productRepr = productService.findById(productId).orElseThrow(() -> new NotFoundException("Продукт не найден" + productId));
        UserRepr userRepr = userService.findById(user.getId()).orElseThrow(() -> new NotFoundException("Потребитель не найден" + productId));

        lineItemService.addProductForUser(productRepr, userRepr);

        log.info("User id: " + user.getId() + " add product in cart");
        return "redirect:/product";
    }

    @PostMapping("/delete")
    public String deleteProductFromCart(@RequestParam("productId") Long productId) {


        ProductRepr productRepr = productService.findById(productId).orElseThrow(() -> new NotFoundException("Продукт не найден" + productId));
        UserRepr userRepr = new UserRepr(userAuthService.getCurrentUser());

        lineItemService.removeProductForUser(productRepr, userRepr);

        log.info("User id: " +  userRepr.getId() + " product delete request from shopping cart");
        return "redirect:/cart";
    }

    @DeleteMapping("/clearCart")
    public String clearCartForUser() {

        User user = userAuthService.getCurrentUser();
        lineItemService.clearCart(user.getId());
        log.info("User id: " +  user.getId() + " clear shopping cart");
        return "redirect:/cart";
    }
}
