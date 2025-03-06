package com.praveg.service.impl;

import com.praveg.entity.Product;
import com.praveg.entity.User;
import com.praveg.entity.Wishlist;
import com.praveg.repository.WishlistRepo;
import com.praveg.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepo wishlistRepo;

    @Override
    public Wishlist createWishlist(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepo.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(User user) {
        Wishlist wishlist = wishlistRepo.findByUserId(user.getId());
        if(wishlist == null){
            wishlist = createWishlist(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishlist(User user, Product product) {
        Wishlist wishlist = getWishlistByUserId(user);

        if(wishlist.getProducts().contains(product)){
            wishlist.getProducts().remove(product);
        }
        else wishlist.getProducts().add(product);
        return wishlistRepo.save(wishlist);
    }
}
