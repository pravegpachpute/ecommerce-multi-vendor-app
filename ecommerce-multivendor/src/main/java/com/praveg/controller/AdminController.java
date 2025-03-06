package com.praveg.controller;

import com.praveg.domain.AccountStatus;
import com.praveg.entity.Seller;
import com.praveg.service.HomeCategoryService;
import com.praveg.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    // admin can ban seller, verify seller, (admin change seller status)

    private final SellerService sellerService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long id,
                                                     @PathVariable AccountStatus status) throws Exception {
        Seller updateSeller = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(updateSeller);
    }
}
