package com.hiran.restaurantService.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiran.restaurantService.dto.RestaurantDTO;
import com.hiran.restaurantService.entity.MenuItem;
import com.hiran.restaurantService.entity.Restaurant;
import com.hiran.restaurantService.service.MenuService;
import com.hiran.restaurantService.service.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    @Autowired
    public RestaurantController(RestaurantService restaurantService, ObjectMapper objectMapper) {
        this.restaurantService = restaurantService;
        this.objectMapper = objectMapper;
    }

    @PutMapping("/{id}/availability")
    public Restaurant toggleAvailability(@PathVariable String id) {

        return restaurantService.toggleAvailability(id);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestPart("restaurant") String restaurantJson,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {

        try {
            // Add proper logging
            logger.debug("Received restaurant JSON: {}", restaurantJson);

            // Configure ObjectMapper to fail on unknown properties
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            RestaurantDTO restaurantDTO = objectMapper.readValue(restaurantJson, RestaurantDTO.class);
            logger.debug("Parsed restaurant DTO: {}", restaurantDTO);

            Restaurant createdRestaurant = restaurantService.createRestaurant(restaurantDTO, coverImage);

            return ResponseEntity.ok(createdRestaurant);

        } catch (Exception e) {
            logger.error("Error creating restaurant", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/view/{id}")
    public Restaurant getById(@PathVariable String id) {
        return restaurantService.getRestaurantById(id);
    }

    @PutMapping("/update/{id}")
    public Restaurant update(@PathVariable String id, @RequestBody Restaurant restaurant) {
        return restaurantService.updateRestaurant(id, restaurant);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
    }

}
