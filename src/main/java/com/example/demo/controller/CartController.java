package com.example.demo.controller;

import com.example.demo.dto.CartItemRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public CartResponse getCart(@AuthenticationPrincipal UserPrincipal principal) {
        return cartService.getCart(principal.getUser());
    }

    @PostMapping("/items")
    public CartResponse addItem(@AuthenticationPrincipal UserPrincipal principal,
                                @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(principal.getUser(), request);
    }

    @PutMapping("/items/{productId}")
    public CartResponse updateItemQuantity(@AuthenticationPrincipal UserPrincipal principal,
                                           @PathVariable Long productId,
                                           @RequestParam Integer quantity) {
        return cartService.updateItemQuantity(principal.getUser(), productId, quantity);
    }

    @DeleteMapping("/items/{productId}")
    public CartResponse removeItem(@AuthenticationPrincipal UserPrincipal principal,
                                   @PathVariable Long productId) {
        return cartService.removeItem(principal.getUser(), productId);
    }

    @DeleteMapping
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void clearCart(@AuthenticationPrincipal UserPrincipal principal) {
        cartService.clearCart(principal.getUser());
    }
}
