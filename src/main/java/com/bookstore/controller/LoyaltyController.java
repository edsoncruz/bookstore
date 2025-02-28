package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.entity.Loyalty;
import com.bookstore.service.LoyaltyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loyalties")
public class LoyaltyController implements BaseController<Loyalty> {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService){
        this.loyaltyService = loyaltyService;
    }

    @Override
    public Loyalty create(Loyalty loyalty) {
        return this.loyaltyService.create(loyalty) ;
    }

    @Override
    public Loyalty update(Loyalty loyalty) {
        return this.loyaltyService.update(loyalty);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.loyaltyService.delete(id);

        return new ResponseEntity<>(String.format("Loyalty id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public Loyalty find(Long id) {
        return this.loyaltyService.find(id);
    }

    @Override
    public List<Loyalty> findAll() {
        return this.loyaltyService.findAll();
    }
}
