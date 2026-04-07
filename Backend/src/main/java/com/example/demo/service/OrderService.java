package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.enums.OrderStatus;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.repository.*;
import com.example.demo.util.OrderStateMachine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;
    private final RefundItemRepository refundItemRepository;
    

    // ================= PLACE ORDER =================

    public OrderResponseDTO placeOrder(String email, String paymentMethod) {

        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new BadRequestException("Payment method is required");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserEmailWithItems(email)
        .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod.toUpperCase());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());

            double subtotal = product.getPrice() * cartItem.getQuantity();
            total += subtotal;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(subtotal);

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // -------- PAYMENT CREATION --------

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setPaymentMethod(paymentMethod.toUpperCase());
        payment.setAmount(total);
        payment.setCreatedAt(LocalDateTime.now());

        if (paymentMethod.equalsIgnoreCase("COD")) {
            payment.setStatus(PaymentStatus.SUCCESS);
            savedOrder.setStatus(OrderStatus.CONFIRMED);
        } else {
            payment.setStatus(PaymentStatus.INITIATED);
            savedOrder.setStatus(OrderStatus.PENDING_PAYMENT);
        }

        paymentRepository.save(payment);
        savedOrder.setPayment(payment);
        orderRepository.save(savedOrder);

        logStatusChange(savedOrder, null, savedOrder.getStatus(), email);

        cart.getItems().clear();
        cartRepository.save(cart);
        return convertToDTO(savedOrder);
    }

    // ================= CONFIRM PAYMENT =================

    public String confirmPayment(Long orderId,
                                 String transactionId,
                                 boolean success) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        Payment payment = order.getPayment();

        if (payment == null) {
            throw new BadRequestException("Payment not found");
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment already completed");
        }

        payment.setTransactionId(transactionId);

        if (success) {
            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PLACED);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);

            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
            }
        }

        paymentRepository.save(payment);
        orderRepository.save(order);


        return "Payment updated successfully";
    }

    // ================= UPDATE ORDER STATUS =================

    public String updateOrderStatus(Long orderId,
                                    OrderStatus newStatus,
                                    String adminEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        if (!OrderStateMachine.canTransition(currentStatus, newStatus)) {
            throw new BadRequestException(
                    "Invalid transition from "
                            + currentStatus + " to " + newStatus);
        }

        order.setStatus(newStatus);

        logStatusChange(order, currentStatus, newStatus, adminEmail);

        return "Order status updated to " + newStatus;
    }

    // ================= GET USER ORDERS =================

    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getUserOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ================= GET ALL ORDERS =================

    @Transactional(readOnly = true)
    public List<AdminOrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::convertToAdminDTO)
                .toList();
    }

    // ================= FULL REFUND =================

    public String refundOrder(Long orderId,
                              String reason,
                              String adminEmail) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BadRequestException("Order not refundable");
        }

        Payment payment = order.getPayment();

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment not refundable");
        }

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setAmount(payment.getAmount());
        refund.setReason(reason);
        refund.setRefundTransactionId("REF-" + System.currentTimeMillis());
        refund.setRefundedAt(LocalDateTime.now());
        refund.setProcessedBy(adminEmail);

        refundRepository.save(refund);

        payment.setStatus(PaymentStatus.REFUNDED);
        order.setStatus(OrderStatus.REFUNDED);

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }

        logStatusChange(order,
                OrderStatus.CONFIRMED,
                OrderStatus.REFUNDED,
                adminEmail);

        return "Refund processed successfully";
    }

    // ================= DTO METHODS =================

    private OrderResponseDTO convertToDTO(Order order) {

        var items = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProduct() != null
                                ? item.getProduct().getId()
                                : null)
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }

    private AdminOrderResponseDTO convertToAdminDTO(Order order) {

        var items = order.getItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .productId(item.getProduct() != null
                                ? item.getProduct().getId()
                                : null)
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        return AdminOrderResponseDTO.builder()
                .orderId(order.getId())
                .customerEmail(order.getUser().getEmail())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .items(items)
                .build();
    }

    private void logStatusChange(Order order,
                                 OrderStatus oldStatus,
                                 OrderStatus newStatus,
                                 String changedBy) {

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .changedAt(LocalDateTime.now())
                .build();

        historyRepository.save(history);
    }


    public String cancelOrder(Long orderId, String userEmail) {

    Order order = orderRepository.findById(orderId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Order not found"));

    if (!order.getUser().getEmail().equals(userEmail)) {
        throw new UnauthorizedException("You cannot cancel this order");
    }

    if (order.getStatus() != OrderStatus.PENDING_PAYMENT &&
        order.getStatus() != OrderStatus.CONFIRMED) {

        throw new BadRequestException("Order cannot be cancelled at this stage");
    }

    OrderStatus oldStatus = order.getStatus();

    order.setStatus(OrderStatus.CANCELLED);

    // restore stock
    for (OrderItem item : order.getItems()) {
        Product product = item.getProduct();
        product.setStock(product.getStock() + item.getQuantity());
    }

    logStatusChange(order, oldStatus, OrderStatus.CANCELLED, userEmail);

    if (order.getPayment() != null &&
    order.getPayment().getStatus() == PaymentStatus.SUCCESS) {

    order.getPayment().setStatus(PaymentStatus.FAILED);

}
   orderRepository.save(order);
    return "Order cancelled successfully";
}
public String partialRefund(Long orderId,
                            Long orderItemId,
                            int quantity,
                            String reason,
                            String adminEmail) {

    Order order = orderRepository.findByIdForUpdate(orderId)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Order not found"));

    if (order.getStatus() != OrderStatus.CONFIRMED) {
        throw new BadRequestException("Order not eligible for refund");
    }

    OrderItem orderItem = order.getItems().stream()
            .filter(i -> i.getId().equals(orderItemId))
            .findFirst()
            .orElseThrow(() ->
                    new ResourceNotFoundException("Order item not found"));

    int alreadyRefunded =
            refundItemRepository.getTotalRefundedQuantity(orderItemId);

    if (quantity <= 0 ||
        quantity + alreadyRefunded > orderItem.getQuantity()) {

        throw new BadRequestException("Invalid refund quantity");
    }

    double refundAmount =
            quantity * orderItem.getPrice();

    Refund refund = new Refund();
    refund.setPayment(order.getPayment());
    refund.setAmount(refundAmount);
    refund.setReason(reason);
    refund.setRefundTransactionId("REF-" + System.currentTimeMillis());
    refund.setRefundedAt(LocalDateTime.now());
    refund.setProcessedBy(adminEmail);

    refundRepository.save(refund);

    RefundItem refundItem = new RefundItem();
    refundItem.setRefund(refund);
    refundItem.setOrderItem(orderItem);
    refundItem.setQuantityRefunded(quantity);

    refundItemRepository.save(refundItem);

    order.getPayment().setStatus(PaymentStatus.PARTIALLY_REFUNDED);

    return "Partial refund processed successfully";
}

}
