package com.praveg.service.impl;

import com.praveg.domain.USER_ROLE;
import com.praveg.entity.Seller;
import com.praveg.entity.User;
import com.praveg.repository.SellerRepo;
import com.praveg.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomeUserServiceImpl implements UserDetailsService {


    private final UserRepo userRepo;

    private final SellerRepo sellerRepo;

    private static final String SELLER_PREFIX ="seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepo.findByEmail(actualUsername);

            if (seller!=null){
                return builUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());
            }
        }
        else {
            User user = userRepo.findByEmail(username);
            if(user!=null){
                return builUserDetails(user.getEmail(),
                        user.getPassword(),
                        user.getRole());
            }
        }
        throw new UsernameNotFoundException("User or Seller not found with email - " + username);
    }

    private UserDetails builUserDetails(String email, String password, USER_ROLE role) {
   if(role==null) role=USER_ROLE.ROLE_CUSTOMER;
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email, password, authorityList);
    }
}
