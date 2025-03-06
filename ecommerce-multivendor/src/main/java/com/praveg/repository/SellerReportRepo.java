package com.praveg.repository;

import com.praveg.entity.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepo extends JpaRepository<SellerReport, Long> {

    SellerReport findBySellerId(Long sellerId);
}
