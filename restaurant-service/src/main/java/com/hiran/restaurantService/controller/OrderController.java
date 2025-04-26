package com.hiran.restaurantService.controller;

import com.hiran.restaurantService.entity.OrderItem;
import com.hiran.restaurantService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/orders")
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class OrderController {
    @Autowired
    private OrderService orderService;

    public ResponseEntity<String> handleOrder(@PathVariable String restaurantId, @RequestBody List<OrderItem> items) {

        return null;
    }
}
