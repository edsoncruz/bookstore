package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.dto.LoyaltyRequestDTO;
import com.bookstore.dto.LoyaltyResponseDTO;
import com.bookstore.service.LoyaltyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loyalties")
public class LoyaltyController implements BaseController<LoyaltyRequestDTO, LoyaltyResponseDTO> {

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService){
        this.loyaltyService = loyaltyService;
    }

    @Override
    public LoyaltyResponseDTO create(LoyaltyRequestDTO loyaltyRequestDTO) {
        return this.loyaltyService.create(loyaltyRequestDTO) ;
    }

    @Override
    public LoyaltyResponseDTO update(LoyaltyRequestDTO loyaltyRequestDTO) {
        return this.loyaltyService.update(loyaltyRequestDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.loyaltyService.delete(id);

        return new ResponseEntity<>(String.format("Loyalty id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public LoyaltyResponseDTO find(Long id) {
        return this.loyaltyService.find(id);
    }

    @Override
    public List<LoyaltyResponseDTO> findAll() {
        return this.loyaltyService.findAll();
    }
}
