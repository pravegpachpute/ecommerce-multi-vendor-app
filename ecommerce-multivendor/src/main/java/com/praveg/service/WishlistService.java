package com.praveg.service;

import com.praveg.entity.Product;
import com.praveg.entity.User;
import com.praveg.entity.Wishlist;

public interface WishlistService {

    Wishlist createWishlist(User user);

    Wishlist getWishlistByUserId(User user);

    Wishlist addProductToWishlist(User user, Product product);
}
