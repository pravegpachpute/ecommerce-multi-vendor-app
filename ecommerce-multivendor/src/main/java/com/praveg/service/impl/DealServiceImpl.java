package com.praveg.service.impl;

import com.praveg.entity.Deal;
import com.praveg.entity.HomeCategory;
import com.praveg.repository.DealRepo;
import com.praveg.repository.HomeCategoryRepo;
import com.praveg.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepo dealRepo;
    private final HomeCategoryRepo homeCategoryRepo;

    @Override
    public Deal createDeal(Deal deal) {
        // 1st find home category
        HomeCategory category = homeCategoryRepo.findById(deal.getCategory().getId()).orElse(null);

        Deal newDeal = dealRepo.save(deal);
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepo.save(newDeal);
    }

    @Override
    public List<Deal> getDeals() {
        return dealRepo.findAll();
    }

    @Override
    public Deal updateDeal(Deal deal, Long id) throws Exception {
        // find deal
        Deal existingDeal = dealRepo.findById(id).orElse(null);
        HomeCategory category = homeCategoryRepo.findById(deal.getCategory().getId()).orElse(null); // orElse****

        if (existingDeal != null) {
            if(deal.getDiscount()!=null){
                existingDeal.setDiscount(deal.getDiscount());
            }
            if(category!=null){
                existingDeal.setCategory(category);
            }
            return dealRepo.save(existingDeal);
        }

        throw new Exception("Deal not found");
    }

    @Override
    public void deleteDeal(Long id) throws Exception {
        Deal deal = dealRepo.findById(id).orElseThrow(()-> new Exception("Deal not Found"));
        dealRepo.delete(deal);
    }
}
