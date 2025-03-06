package com.praveg.repository;

import com.praveg.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepo extends JpaRepository<Wishlist, Long> {

    Wishlist findByUserId(Long userId);
}
