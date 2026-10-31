package com.example.reliefcamp.controller;

import com.example.reliefcamp.dto.CampRequest;
import com.example.reliefcamp.dto.CampResponse;
import com.example.reliefcamp.service.CampService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camps")
@CrossOrigin(origins = "*")
public class CampController {

    private final CampService campService;

    public CampController(CampService campService) {
        this.campService = campService;
    }

    @PostMapping
    public ResponseEntity<CampResponse> createCamp(@Valid @RequestBody CampRequest request) {
        CampResponse response = campService.createCamp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CampResponse>> getAllCamps() {
        List<CampResponse> camps = campService.getAllCamps();
        return ResponseEntity.ok(camps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampResponse> getCampById(@PathVariable Long id) {
        CampResponse camp = campService.getCampById(id);
        return ResponseEntity.ok(camp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampResponse> updateCamp(@PathVariable Long id, @Valid @RequestBody CampRequest request) {
        CampResponse updatedCamp = campService.updateCamp(id, request);
        return ResponseEntity.ok(updatedCamp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamp(@PathVariable Long id) {
        campService.deleteCamp(id);
        return ResponseEntity.noContent().build();
    }
}
