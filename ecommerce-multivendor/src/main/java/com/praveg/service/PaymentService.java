package com.praveg.service;

import com.praveg.entity.Order;
import com.praveg.entity.PaymentOrder;
import com.praveg.entity.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;       // **** Long
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                String paymentId,
                                String paymentLinkId) throws RazorpayException;

    PaymentLink createRazorpayPaymentLink(User user,
                                          Long amount,
                                          Long orderId) throws RazorpayException;

    String createStripePaymentLink(User user,
                                          Long amount,
                                          Long orderId) throws StripeException;
}
