package com.example.demo.controller;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.OrderStatusUpdateRequest;
import com.example.demo.security.UserPrincipal;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@AuthenticationPrincipal UserPrincipal principal) {
        return orderService.checkout(principal.getUser());
    }

    @GetMapping
    public List<OrderResponse> getOrders(@AuthenticationPrincipal UserPrincipal principal) {
        return orderService.getOrders(principal);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id,
                                  @AuthenticationPrincipal UserPrincipal principal) {
        return orderService.getOrderById(id, principal);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id,
                                           @Valid @RequestBody OrderStatusUpdateRequest request,
                                           @AuthenticationPrincipal UserPrincipal principal) {
        return orderService.updateOrderStatus(id, request, principal);
    }
}
