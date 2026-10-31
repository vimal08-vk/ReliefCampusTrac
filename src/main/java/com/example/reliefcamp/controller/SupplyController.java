package com.example.reliefcamp.controller;

import com.example.reliefcamp.dto.InventoryItemDTO;
import com.example.reliefcamp.dto.SupplyRequest;
import com.example.reliefcamp.dto.SupplyResponse;
import com.example.reliefcamp.service.SupplyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@CrossOrigin(origins = "*")
public class SupplyController {

    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping
    public ResponseEntity<SupplyResponse> createSupply(@Valid @RequestBody SupplyRequest request) {
        SupplyResponse response = supplyService.createSupply(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SupplyResponse>> getAllSupplies() {
        List<SupplyResponse> supplies = supplyService.getAllSupplies();
        return ResponseEntity.ok(supplies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyResponse> getSupplyById(@PathVariable Long id) {
        SupplyResponse supply = supplyService.getSupplyById(id);
        return ResponseEntity.ok(supply);
    }

    @GetMapping("/camp/{campId}")
    public ResponseEntity<List<SupplyResponse>> getSuppliesByCamp(@PathVariable Long campId) {
        List<SupplyResponse> supplies = supplyService.getSuppliesByCamp(campId);
        return ResponseEntity.ok(supplies);
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryItemDTO>> getInventory() {
        List<InventoryItemDTO> inventory = supplyService.getInventory();
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyResponse> updateSupply(@PathVariable Long id, @Valid @RequestBody SupplyRequest request) {
        SupplyResponse updatedSupply = supplyService.updateSupply(id, request);
        return ResponseEntity.ok(updatedSupply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupply(@PathVariable Long id) {
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }
}
