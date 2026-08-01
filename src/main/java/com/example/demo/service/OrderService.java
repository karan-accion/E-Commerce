package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.OrderStatusUpdateRequest;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.Role;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.UserPrincipal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    @Transactional
    public OrderResponse checkout(User user) {
        log.info("Checkout requested by user id={}", user.getId());
        try {
        Cart cart = cartService.getOrCreateCart(user);

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                log.warn("Insufficient stock for product id={} requested={} available={}", product.getId(), cartItem.getQuantity(), product.getStockQuantity());
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), product.getPrice());
            order.addOrderItem(orderItem);
            totalAmount = totalAmount.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);

        log.info("Order created id={} userId={} total={}", savedOrder.getId(), user.getId(), savedOrder.getTotalAmount());

        return new OrderResponse(savedOrder);
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.warn("Optimistic lock failure during checkout for user id={}: {}", user.getId(), ex.getMessage());
            throw new BadRequestException("Concurrent stock update detected; please retry the checkout");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(UserPrincipal principal) {
        if (principal.getUser().getRole() == Role.ADMIN) {
            return orderRepository.findAll().stream()
                    .map(OrderResponse::new)
                    .toList();
        }
        return orderRepository.findByUserOrderByCreatedAtDesc(principal.getUser()).stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id, UserPrincipal principal) {
        Order order = findOrderById(id);
        validateOrderAccess(order, principal);
        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request, UserPrincipal principal) {
        if (principal.getUser().getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only admins can update order status");
        }

        Order order = findOrderById(id);
        order.setStatus(request.getStatus());
        return new OrderResponse(orderRepository.save(order));
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private void validateOrderAccess(Order order, UserPrincipal principal) {
        if (principal.getUser().getRole() != Role.ADMIN
                && !order.getUser().getId().equals(principal.getId())) {
            throw new UnauthorizedException("You can only view your own orders");
        }
    }
}
