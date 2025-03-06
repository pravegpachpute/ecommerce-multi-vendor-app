package com.praveg.service.impl;

import com.praveg.domain.HomeCategorySection;
import com.praveg.entity.Deal;
import com.praveg.entity.Home;
import com.praveg.entity.HomeCategory;
import com.praveg.repository.DealRepo;
import com.praveg.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final DealRepo dealRepo;

    @Override
    public Home createHomePageData(List<HomeCategory> allCategories) {

        List<HomeCategory> gridCategories = allCategories.stream().filter(
                category -> category.getSection() == HomeCategorySection.GRID
        ).collect(Collectors.toList());

        List<HomeCategory> shopByCategories = allCategories.stream().filter(
                category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES
        ).collect(Collectors.toList());

        List<HomeCategory> electricCategories = allCategories.stream().filter(
                category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES
        ).collect(Collectors.toList());

        List<HomeCategory> dealCategories = allCategories.stream().filter(
                category -> category.getSection() == HomeCategorySection.DEALS
        ).toList();

        // create deal
        List<Deal> createdDeals = new ArrayList<>();
        if(dealRepo.findAll().isEmpty()){
            List<Deal> deals = allCategories.stream().filter(category -> category
                    .getSection() == HomeCategorySection.DEALS).map(category -> new
                    Deal(null, 10, category))  //
                    .collect(Collectors.toList());
            createdDeals = dealRepo.saveAll(deals);
        } else createdDeals = dealRepo.findAll();

        Home home = new Home();
        home.setGrid(gridCategories);
        home.setShopByCategories(shopByCategories);
        home.setElectricCategories(electricCategories);
        home.setDeals(createdDeals);
        home.setDealCategories(dealCategories);

        return home;
    }
}
