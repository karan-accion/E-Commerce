package com.example.demo.service;

import com.example.demo.dto.CartItemRequest;
import com.example.demo.dto.CartResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        return new CartResponse(getOrCreateCart(user));
    }

    @Transactional
    public CartResponse addItem(User user, CartItemRequest request) {
        Cart cart = getOrCreateCart(user);
        Product product = productService.findProductById(request.getProductId());

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }

        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            existingItem.setQuantity(newQuantity);
        } else {
            cart.addCartItem(new CartItem(product, request.getQuantity()));
        }

        return new CartResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse updateItemQuantity(User user, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productService.findProductById(productId);

        CartItem item = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
        } else {
            if (product.getStockQuantity() < quantity) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            item.setQuantity(quantity);
        }

        return new CartResponse(cartRepository.save(cart));
    }

    @Transactional
    public CartResponse removeItem(User user, Long productId) {
        Cart cart = getOrCreateCart(user);

        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

        return new CartResponse(cartRepository.save(cart));
    }

    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }
}
