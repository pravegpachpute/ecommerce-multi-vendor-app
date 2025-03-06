package com.praveg.service.impl;

import com.praveg.domain.OrderStatus;
import com.praveg.domain.PaymentStatus;
import com.praveg.entity.*;
import com.praveg.repository.AddressRepo;
import com.praveg.repository.OrderItemRepo;
import com.praveg.repository.OrderRepo;
import com.praveg.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final AddressRepo addressRepo;
    private final OrderItemRepo orderItemRepo;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        if(!user.getAddresses().contains(shippingAddress)){
            user.getAddresses().add(shippingAddress);
        }
        Address address = addressRepo.save(shippingAddress);

        // user choose multiple item different brands and sellers
        // create different orders each sellers
        // list of order key-value pair key-sellerId value-cart_item

        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item-> item.getProduct()
                        .getSeller().getId()));

        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();

            // calculate price & quantity
            int totalOrderPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

            // create order

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

            Order saveOrder = orderRepo.save(createdOrder);
            orders.add(saveOrder);

            // create order items to set

            List<OrderItem> orderItems = new ArrayList<>();

            for(CartItem item : items){
                // create order item
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(saveOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                saveOrder.getOrderItems().add(orderItem);

                OrderItem saveOrderItem = orderItemRepo.save(orderItem);
                orderItems.add(saveOrderItem);
            }
        }
        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepo.findById(id).orElseThrow(()-> new Exception("Order not found..." +id));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepo.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepo.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order = findOrderById(orderId);

        // user order check same order cancel or not match cancel order
        if(!user.getId().equals(order.getUser().getId())){
            throw new Exception("You don't have access to this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepo.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepo.findById(id).orElseThrow(()-> new Exception("Order Item not exist..."));
    }
}
