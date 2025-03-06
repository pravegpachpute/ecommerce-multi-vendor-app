package com.praveg.controller;

import com.praveg.entity.Deal;
import com.praveg.response.ApiResponse;
import com.praveg.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeals(@RequestBody Deal deals){
        Deal createDeals = dealService.createDeal(deals);
        return new ResponseEntity<>(createDeals, HttpStatus.ACCEPTED);
    }

    // get all deals
    @GetMapping
    public ResponseEntity<List<Deal>> getDeals(){
        List<Deal> createDeals = dealService.getDeals();
        return new ResponseEntity<>(createDeals, HttpStatus.ACCEPTED);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable Long id,@RequestBody Deal deal) throws Exception {
        Deal updateDeals = dealService.updateDeal(deal, id);
        return ResponseEntity.ok(updateDeals);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeals(@PathVariable Long id) throws Exception {
        dealService.deleteDeal(id);

        ApiResponse response = new ApiResponse();
        response.setMessage("Delete deals successfully...");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
