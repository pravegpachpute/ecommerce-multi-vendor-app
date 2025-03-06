package com.praveg.request;


import lombok.Data;

import java.util.List;

@Data
public class CreateProductRequest {

    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private String color;
    private List<String> images;
    private String category;        //Mens , women, kids
    private String category2;       // party wear , top wear
    private String category3;       // men shirt, pant

    private String sizes;
}
