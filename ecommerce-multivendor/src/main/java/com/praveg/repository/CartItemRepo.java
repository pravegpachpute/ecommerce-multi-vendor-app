package com.praveg.repository;

import com.praveg.entity.Cart;
import com.praveg.entity.CartItem;
import com.praveg.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
