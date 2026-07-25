package com.example.demo.dto;

import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {

    private Long id;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
    private int totalItems;

    public CartResponse() {
    }

    public CartResponse(Cart cart) {
        this.id = cart.getId();
        this.items = cart.getCartItems().stream()
                .map(CartItemResponse::new)
                .toList();
        this.totalAmount = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalItems = cart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
