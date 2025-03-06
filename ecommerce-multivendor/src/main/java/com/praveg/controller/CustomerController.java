//package com.praveg.controller;
//
//import com.praveg.entity.Home;
//import com.praveg.entity.HomeCategory;
//import com.praveg.service.HomeCategoryService;
//import com.praveg.service.HomeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class CustomerController {
//
//    // same method Home Category controller tya mule he all comment karat ahe
//    private final HomeCategoryService homeCategoryService;
//    private final HomeService homeService;
//
//    @PostMapping("/home/categories")
//    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories){
//
//        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
//        Home home = homeService.createHomePageData(categories);
//        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
//    }
//
//    // no use
//    @GetMapping("/home-page")
//    public ResponseEntity<Home> getHomeData(){
////        Home home = homeService.getHomePageData();
////        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
//        return null;
//    }
//}
