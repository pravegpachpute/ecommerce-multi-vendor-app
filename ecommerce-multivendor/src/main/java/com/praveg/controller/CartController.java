package com.praveg.controller;

import com.praveg.entity.Cart;
import com.praveg.entity.CartItem;
import com.praveg.entity.Product;
import com.praveg.entity.User;
import com.praveg.request.AddItemRequest;
import com.praveg.response.ApiResponse;
import com.praveg.service.CartItemService;
import com.praveg.service.CartService;
import com.praveg.service.ProductService;
import com.praveg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    // check
    @GetMapping("/get-user-cart")
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        System.out.println("cart -" +cart.getUser().getEmail());
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    // check
    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());

        CartItem item = cartService.addCartItem(user,
                product,
                req.getSize(),
                req.getQuantity());

        ApiResponse response = new ApiResponse();
        response.setMessage("Item added to cart successfully");
        return new ResponseEntity<>(item, HttpStatus.CREATED);
    }

    // check
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(@PathVariable Long cartItemId,
                                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse response = new ApiResponse();
        response.setMessage("Item remove from Cart");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    // check
    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(@PathVariable Long cartItemId,
                                                             @RequestBody CartItem cartItem,
                                                             @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;

        if(cartItem.getQuantity() > 0){
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }
        return new ResponseEntity<>(updatedCartItem , HttpStatus.ACCEPTED);
    }
}
