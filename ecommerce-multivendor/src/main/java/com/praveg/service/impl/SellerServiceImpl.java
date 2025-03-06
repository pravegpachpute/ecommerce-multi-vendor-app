package com.praveg.service.impl;

import com.praveg.config.JwtProvider;
import com.praveg.domain.AccountStatus;
import com.praveg.domain.USER_ROLE;
import com.praveg.entity.Address;
import com.praveg.entity.Seller;
import com.praveg.exception.SellerException;
import com.praveg.repository.AddressRepo;
import com.praveg.repository.SellerRepo;
import com.praveg.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepo sellerRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepo addressRepo;


    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {

        // check already present in db or not
        Seller sellerExist = sellerRepo.findByEmail(seller.getEmail());

        if(sellerExist!=null){
            throw new Exception("Seller already exist, used different email");
        }

        // delivery address for delivery boy pickup order
        Address saveAddress = addressRepo.save(seller.getPickupAddress());

        // seller not exist providing email then create new seller
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(saveAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setMobile(seller.getMobile());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBuisnessDetails(seller.getBuisnessDetails());
        return sellerRepo.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepo.findById(id).orElseThrow(()-> new SellerException("Seller not found with id" +id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepo.findByEmail(email);

        if(seller==null){
            throw new Exception("Seller not found with email -" +email);
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepo.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existSeller = sellerRepo.findById(id).orElseThrow(()->
                new Exception("Seller not found with id" +id)); //this.getSellerById(id);

        if(seller.getSellerName() != null){
            existSeller.setSellerName(seller.getSellerName());
        }
        if(seller.getMobile() != null){
            existSeller.setMobile(seller.getMobile());
        }
        if(seller.getEmail() != null){
            existSeller.setEmail(seller.getEmail());
        }
        if(seller.getBuisnessDetails() != null &&
                seller.getBuisnessDetails().getBusinessName() != null){
            existSeller.getBuisnessDetails().setBusinessName(seller.getBuisnessDetails().getBusinessName());
        }
        if(seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() !=null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountNumber() != null){
            existSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            existSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
        }
        if(seller.getPickupAddress() != null
        && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null){
            existSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }
        if(seller.getGSTIN() != null){
            existSeller.setGSTIN(seller.getGSTIN());
        }
        return sellerRepo.save(existSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = getSellerById(id);
        sellerRepo.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {  //otp compare in controller part
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepo.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepo.save(seller);
    }
}
