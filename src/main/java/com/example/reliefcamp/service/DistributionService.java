package com.example.reliefcamp.service;

import com.example.reliefcamp.dto.DistributionRequest;
import com.example.reliefcamp.dto.DistributionResponse;

import java.util.List;

public interface DistributionService {

    DistributionResponse createDistribution(DistributionRequest request);

    List<DistributionResponse> getAllDistributions();

    List<DistributionResponse> getDistributionsByCamp(Long campId);

    List<DistributionResponse> getDistributionsByFamily(Long familyId);

    DistributionResponse getDistributionById(Long id);

    void deleteDistribution(Long id);
}
