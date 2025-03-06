package com.praveg.controller;

import com.praveg.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResponse homeControllerHandler(){
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setMessage("Welcome to Ecommerce multi vendor system");
        return apiResponse;
    }
}
