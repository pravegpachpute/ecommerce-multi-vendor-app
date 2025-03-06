package com.praveg.controller;

import com.praveg.entity.Product;
import com.praveg.entity.Seller;
import com.praveg.exception.ProductException;
import com.praveg.request.CreateProductRequest;
import com.praveg.service.ProductService;
import com.praveg.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    // check
    @GetMapping         // list of perticular seller
    public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization")
                                                                  String jwt) throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // check
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
                                                 @RequestHeader("Authorization") String jwt)
            throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request, seller);
//        Product product = new Product();
        System.out.println(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId){
        try{
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                 @RequestBody Product product){
            try{
                Product updateProduct = productService.updateProduct(productId, product);
                return new ResponseEntity<>(updateProduct, HttpStatus.OK);
            } catch(ProductException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }
}
