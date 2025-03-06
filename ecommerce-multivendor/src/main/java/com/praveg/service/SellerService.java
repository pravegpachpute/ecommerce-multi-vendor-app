package com.praveg.service;

import com.praveg.domain.AccountStatus;
import com.praveg.entity.Seller;
import com.praveg.exception.SellerException;

import java.util.List;

public interface SellerService {

    // separate log in for seller

    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;

    // active seller deactivate seller
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Long id, Seller seller) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;

    //Admin can ban seller account
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;
}
