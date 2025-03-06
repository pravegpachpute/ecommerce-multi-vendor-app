package com.praveg.controller;

import com.praveg.entity.Product;
import com.praveg.entity.Transaction;
import com.praveg.entity.User;
import com.praveg.entity.Wishlist;
import com.praveg.exception.ProductException;
import com.praveg.service.ProductService;
import com.praveg.service.UserService;
import com.praveg.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;
    private final ProductService productService;

//    @PostMapping("/create")
//    public ResponseEntity<Wishlist> createWishlist(@RequestBody User user){
//        Wishlist wishlist = wishlistService.createWishlist(user);
//        return ResponseEntity.ok(wishlist);
//    }

    @GetMapping
    public ResponseEntity<Wishlist> getWishlistByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishlistService.getWishlistByUserId(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(@PathVariable Long productId,
                                                         @RequestHeader("Authorization") String jwt)
            throws Exception {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        Wishlist updatedWishlist = wishlistService.addProductToWishlist(
                user,
                product
        );
        return ResponseEntity.ok(updatedWishlist);
    }
}
