package com.praveg.service.impl;

import com.praveg.entity.Cart;
import com.praveg.entity.Coupon;
import com.praveg.entity.User;
import com.praveg.repository.CartRepo;
import com.praveg.repository.CouponRepo;
import com.praveg.repository.UserRepo;
import com.praveg.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepo couponRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
        // 1st find coupon
        Coupon coupon = couponRepo.findByCode(code);

        // find cart
        Cart cart = cartRepo.findByUserId(user.getId());

        if(coupon == null){
            throw new Exception("Coupon not valid");
        }
        // user use coupon at once time only
        if(user.getUsedCoupons().contains(coupon)){
            throw new Exception("Coupon already use");
        }
        // order value = 500 & coupon = 10%()
        if(orderValue < coupon.getMinimumOrderValue()){
            throw new Exception("Valid for minimum order value" + coupon.getMinimumOrderValue());
        }
        // seller can deactivate coupon & coupon start date - 1/02/25 & end date - 2/02/25
        if(coupon.isActive() &&
                LocalDate.now().isAfter(coupon.getValidityStartDate()) &&
                        LocalDate.now().isBefore(coupon.getValidityEndDate())){
            user.getUsedCoupons().add(coupon);
            userRepo.save(user);

            // update User Cart & coupon logic for apply user

            double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;

            cart.setTotalSellingPrice(cart.getTotalSellingPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepo.save(cart);
            return cart;
        }
        throw new Exception("Coupon not valid");
    }

    @Override
    public Cart removeCoupon(String code, User user) throws Exception {
        // 1st find coupon
        Coupon coupon = couponRepo.findByCode(code);

        // coupon is null throw error
        if(coupon == null){
            throw new Exception("Coupon not found");
        }
        // find cart
        Cart cart = cartRepo.findByUserId(user.getId());

        // coupon is present then remove apply coupon logic
        double discountedPrice = (cart.getTotalSellingPrice() * coupon.getDiscountPercentage())/100;
        cart.setTotalSellingPrice(cart.getTotalSellingPrice() + discountedPrice);
        cart.setCouponCode(null);

        return cartRepo.save(cart);
    }

    @Override
    public Coupon findCouponById(Long id) throws Exception {
        return couponRepo.findById(id).orElseThrow(()-> new Exception("Coupon not found..."));
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")          // only admin can create coupon
    public Coupon createCoupon(Coupon coupon) {
        return couponRepo.save(coupon);
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")          // only admin can delete coupon
    public void deleteCoupon(Long id) throws Exception {
        // 1st find coupon
        findCouponById(id);
        couponRepo.deleteById(id);  // delete this coupon with using id
    }
}
