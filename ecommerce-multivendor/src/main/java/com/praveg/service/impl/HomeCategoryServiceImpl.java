package com.praveg.service.impl;

import com.praveg.entity.HomeCategory;
import com.praveg.repository.HomeCategoryRepo;
import com.praveg.service.HomeCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCategoryServiceImpl implements HomeCategoryService {

    private final HomeCategoryRepo homeCategoryRepo;

    // *** frontend home page

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepo.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        // check home categories is empty or not
        if(homeCategoryRepo.findAll().isEmpty()){
           return homeCategoryRepo.saveAll(homeCategories);
        }
        return homeCategoryRepo.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory category, Long id) throws Exception {
        HomeCategory existingCategory = homeCategoryRepo.findById(id)
                .orElseThrow(() -> new Exception("Category not found"));

        if(category.getImages()!=null){
            existingCategory.setImages(category.getImages());
        }
        if(category.getCategoryId()!=null){
            existingCategory.setCategoryId(category.getCategoryId());
        }
        return homeCategoryRepo.save(existingCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepo.findAll();
    }
}
