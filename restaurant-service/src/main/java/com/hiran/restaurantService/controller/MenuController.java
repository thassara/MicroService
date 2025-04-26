package com.hiran.restaurantService.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiran.restaurantService.dto.MenuItemDTO;
import com.hiran.restaurantService.entity.MenuItem;
import com.hiran.restaurantService.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu")
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*", allowCredentials = "true")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItem> addMenuItem(
            @PathVariable String restaurantId,
            @RequestPart("menuItem") String menuItemJson,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MenuItemDTO menuItemDTO = objectMapper.readValue(menuItemJson, MenuItemDTO.class);

            MenuItem createdItem = menuService.addMenuItem(restaurantId, menuItemDTO, imageFile);
            return ResponseEntity.ok(createdItem);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItem> updateMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String itemId,
            @RequestBody MenuItemDTO menuItemDTO) {
        return ResponseEntity.ok(menuService.updateMenuItem(restaurantId, itemId, menuItemDTO));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String itemId) {
        menuService.deleteMenuItem(restaurantId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getMenuItems(
            @PathVariable String restaurantId) {
        return ResponseEntity.ok(menuService.getMenuItems(restaurantId));
    }

    @PatchMapping("/{itemId}/availability")
    public ResponseEntity<MenuItem> toggleItemAvailability(
            @PathVariable String restaurantId,
            @PathVariable String itemId) {
        try {
            MenuItem updatedItem = menuService.toggleItemAvailability(restaurantId, itemId);
            return ResponseEntity.ok(updatedItem);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
