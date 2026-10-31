package com.example.reliefcamp.controller;

import com.example.reliefcamp.dto.FamilyRequest;
import com.example.reliefcamp.dto.FamilyResponse;
import com.example.reliefcamp.service.FamilyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/families")
@CrossOrigin(origins = "*")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping
    public ResponseEntity<FamilyResponse> createFamily(@Valid @RequestBody FamilyRequest request) {
        FamilyResponse response = familyService.createFamily(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FamilyResponse>> getAllFamilies() {
        List<FamilyResponse> families = familyService.getAllFamilies();
        return ResponseEntity.ok(families);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyResponse> getFamilyById(@PathVariable Long id) {
        FamilyResponse family = familyService.getFamilyById(id);
        return ResponseEntity.ok(family);
    }

    @GetMapping("/camp/{campId}")
    public ResponseEntity<List<FamilyResponse>> getFamiliesByCamp(@PathVariable Long campId) {
        List<FamilyResponse> families = familyService.getFamiliesByCamp(campId);
        return ResponseEntity.ok(families);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyResponse> updateFamily(@PathVariable Long id, @Valid @RequestBody FamilyRequest request) {
        FamilyResponse updatedFamily = familyService.updateFamily(id, request);
        return ResponseEntity.ok(updatedFamily);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        familyService.deleteFamily(id);
        return ResponseEntity.noContent().build();
    }
}
