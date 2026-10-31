package com.example.reliefcamp.controller;

import com.example.reliefcamp.dto.DistributionRequest;
import com.example.reliefcamp.dto.DistributionResponse;
import com.example.reliefcamp.service.DistributionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distributions")
@CrossOrigin(origins = "*")
public class DistributionController {

    private final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping
    public ResponseEntity<DistributionResponse> createDistribution(@Valid @RequestBody DistributionRequest request) {
        DistributionResponse response = distributionService.createDistribution(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DistributionResponse>> getAllDistributions() {
        List<DistributionResponse> distributions = distributionService.getAllDistributions();
        return ResponseEntity.ok(distributions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistributionResponse> getDistributionById(@PathVariable Long id) {
        DistributionResponse distribution = distributionService.getDistributionById(id);
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/camp/{campId}")
    public ResponseEntity<List<DistributionResponse>> getDistributionsByCamp(@PathVariable Long campId) {
        List<DistributionResponse> distributions = distributionService.getDistributionsByCamp(campId);
        return ResponseEntity.ok(distributions);
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<DistributionResponse>> getDistributionsByFamily(@PathVariable Long familyId) {
        List<DistributionResponse> distributions = distributionService.getDistributionsByFamily(familyId);
        return ResponseEntity.ok(distributions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        distributionService.deleteDistribution(id);
        return ResponseEntity.noContent().build();
    }
}
