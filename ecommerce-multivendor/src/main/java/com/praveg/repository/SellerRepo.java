package com.praveg.repository;

import com.praveg.domain.AccountStatus;
import com.praveg.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerRepo extends JpaRepository<Seller , Long> {

    Seller findByEmail(String email);

    // get all sellers with active deactivate accounts seller
    List<Seller> findByAccountStatus(AccountStatus status);
}
