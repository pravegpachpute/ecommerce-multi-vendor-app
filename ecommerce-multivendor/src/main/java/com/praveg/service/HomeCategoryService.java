package com.praveg.service;

import com.praveg.entity.HomeCategory;

import java.util.List;

public interface HomeCategoryService {

    // *** frontend home page

    HomeCategory createHomeCategory(HomeCategory homeCategory);

    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);

    HomeCategory updateHomeCategory(HomeCategory category, Long id) throws Exception;

    List<HomeCategory> getAllHomeCategories();

}
