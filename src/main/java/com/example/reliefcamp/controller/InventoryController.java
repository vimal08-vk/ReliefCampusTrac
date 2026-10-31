package com.example.reliefcamp.controller;

import com.example.reliefcamp.dto.InventoryItemDTO;
import com.example.reliefcamp.service.SupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    private final SupplyService supplyService;

    public InventoryController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemDTO>> getInventory() {
        List<InventoryItemDTO> inventory = supplyService.getInventory();
        return ResponseEntity.ok(inventory);
    }
}
