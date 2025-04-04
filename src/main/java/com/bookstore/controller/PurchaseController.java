package com.bookstore.controller;

import com.bookstore.controller.base.BaseController;
import com.bookstore.dto.PurchaseRequestDTO;
import com.bookstore.dto.PurchaseResponseDTO;
import com.bookstore.entity.Purchase;
import com.bookstore.service.BookService;
import com.bookstore.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController implements BaseController<PurchaseRequestDTO, PurchaseResponseDTO> {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @Override
    public PurchaseResponseDTO create(PurchaseRequestDTO purchaseRequestDTO) {
        return this.purchaseService.create(purchaseRequestDTO) ;
    }

    @Override
    public PurchaseResponseDTO update(PurchaseRequestDTO purchaseRequestDTO) {
        return this.purchaseService.update(purchaseRequestDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        this.purchaseService.delete(id);

        return new ResponseEntity<>(String.format("Purchase id %s deleted successfully", id), HttpStatus.OK);
    }

    @Override
    public PurchaseResponseDTO find(Long id) {
        return this.purchaseService.find(id);

    }

    @Override
    public List<PurchaseResponseDTO> findAll() {
        return this.purchaseService.findAll();
    }

    @PostMapping("/order")
    public PurchaseResponseDTO order(@RequestBody PurchaseRequestDTO purchaseRequestDTO){
        return this.purchaseService.order(purchaseRequestDTO);
    }
}
