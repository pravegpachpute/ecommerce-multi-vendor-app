package com.praveg.service;


import com.praveg.entity.Seller;
import com.praveg.entity.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);

    SellerReport updateSellerReport(SellerReport sellerReport);
}
