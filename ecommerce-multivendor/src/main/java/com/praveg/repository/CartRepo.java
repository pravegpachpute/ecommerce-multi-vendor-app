package com.praveg.repository;

import com.praveg.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long id);
}
