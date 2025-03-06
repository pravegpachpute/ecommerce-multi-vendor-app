package com.praveg.service;

import com.praveg.entity.Deal;

import java.util.List;

public interface DealService {

    // admin can change the deals on home page frontend

    Deal createDeal(Deal deal);
    List<Deal> getDeals();
    Deal updateDeal(Deal deal, Long id) throws Exception;
    void deleteDeal(Long id) throws Exception;
}
