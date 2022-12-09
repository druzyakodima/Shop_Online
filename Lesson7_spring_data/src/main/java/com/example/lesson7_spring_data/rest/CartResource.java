package com.example.lesson7_spring_data.rest;

import com.example.lesson7_spring_data.entity.product_entity.ProductRepr;
import com.example.lesson7_spring_data.entity.user_entity.UserRepr;
import com.example.lesson7_spring_data.exception.NotFoundException;
import com.example.lesson7_spring_data.entity.LineItem;
import com.example.lesson7_spring_data.service.product_service.IProductService;
import com.example.lesson7_spring_data.service.redis_service.IRedisService;
import com.example.lesson7_spring_data.service.user_service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//swagger:
//http://localhost:8080/swagger-ui/index.html#/

@RestController
@RequestMapping("/api/v1/cart")
public class CartResource {

    private IRedisService redisService;
    private IProductService productService;
    private IUserService userService;

    @Autowired
    public CartResource(IRedisService redisService, IProductService productService, IUserService userService) {
        this.redisService = redisService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/user/{userId}/product/{productId}")
    public void addToCart(@PathVariable("userId") Long userId, @PathVariable("productId") Long productId) {

        ProductRepr productRepr = productService.findById(productId).orElseThrow(() -> new NotFoundException("Продукт не найден" + productId));
        UserRepr userRepr = userService.findById(userId).orElseThrow(() -> new NotFoundException("Потребитель не найден" + productId));

        redisService.addProductForUser(productRepr,userRepr);
    }

    @GetMapping("/user/{userId}")
    public List<LineItem> findItemForUser(@PathVariable("userId") Long userId) {
        return redisService.findAllItems(userId);
    }

    @PostMapping("/remove/user/{userId}/product/{productId}")
    public void removeProduct(@PathVariable("userId") Long userId,
                              @PathVariable("productId") Long productId,
                              @RequestParam("qty") Integer qty) {

        ProductRepr productRepr = productService.findById(productId).orElseThrow(() -> new NotFoundException("Продукт не найден" + productId));
        UserRepr userRepr = userService.findById(userId).orElseThrow(() -> new NotFoundException("Потребитель не найден" + productId));

        redisService.removeProductForUser(productRepr,userRepr);
    }

    @ExceptionHandler
    public ResponseEntity<String> notFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
