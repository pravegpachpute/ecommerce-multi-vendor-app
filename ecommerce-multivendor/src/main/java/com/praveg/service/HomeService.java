package com.praveg.service;

import com.praveg.entity.Home;
import com.praveg.entity.HomeCategory;

import java.util.List;

public interface HomeService {

    // **** fronted page

    public Home createHomePageData(List<HomeCategory> allCategories);
}
