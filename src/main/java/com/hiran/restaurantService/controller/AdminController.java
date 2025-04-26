package com.hiran.restaurantService.controller;

import com.hiran.restaurantService.entity.Restaurant;
import com.hiran.restaurantService.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class AdminController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/pending")
    public List<Restaurant> getPendingRegistrations() {
        return restaurantService.getPendingRestaurants();
    }

    // Approve/reject restaurant
    @PatchMapping("/{id}/approval")
    public Restaurant updateApprovalStatus(
            @PathVariable String id,
            @RequestParam boolean approved) {
        return restaurantService.updateApprovalStatus(id, approved);
    }
}
