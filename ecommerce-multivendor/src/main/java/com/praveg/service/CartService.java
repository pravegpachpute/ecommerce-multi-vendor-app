package com.praveg.service;

import com.praveg.entity.Cart;
import com.praveg.entity.CartItem;
import com.praveg.entity.Product;
import com.praveg.entity.User;

public interface CartService {

    public CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    );

    public Cart findUserCart(User user);

}
