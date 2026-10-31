package com.example.reliefcamp.service.impl;

import com.example.reliefcamp.dto.CampResponse;
import com.example.reliefcamp.dto.DashboardSummaryDTO;
import com.example.reliefcamp.dto.DistributionResponse;
import com.example.reliefcamp.dto.FamilyResponse;
import com.example.reliefcamp.repository.CampRepository;
import com.example.reliefcamp.repository.DistributionRepository;
import com.example.reliefcamp.repository.FamilyRepository;
import com.example.reliefcamp.repository.SupplyRepository;
import com.example.reliefcamp.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final CampRepository campRepository;
    private final FamilyRepository familyRepository;
    private final SupplyRepository supplyRepository;
    private final DistributionRepository distributionRepository;

    public DashboardServiceImpl(CampRepository campRepository,
                                FamilyRepository familyRepository,
                                SupplyRepository supplyRepository,
                                DistributionRepository distributionRepository) {
        this.campRepository = campRepository;
        this.familyRepository = familyRepository;
        this.supplyRepository = supplyRepository;
        this.distributionRepository = distributionRepository;
    }

    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();

        summary.setTotalCamps(campRepository.count());
        summary.setTotalFamilies(familyRepository.count());
        summary.setTotalPersonsAccommodated(campRepository.getTotalCurrentOccupancy());
        summary.setTotalInventoryItems(supplyRepository.count());
        summary.setTotalStockQuantity(supplyRepository.getTotalAvailableStock());
        summary.setTotalDistributions(distributionRepository.count());
        summary.setLowStockCount(supplyRepository.countByQuantityLessThanEqual(50));

        summary.setRecentCamps(
                campRepository.findTop5ByOrderByIdDesc().stream()
                        .map(CampResponse::fromEntity)
                        .collect(Collectors.toList())
        );

        summary.setRecentFamilies(
                familyRepository.findTop5ByOrderByIdDesc().stream()
                        .map(FamilyResponse::fromEntity)
                        .collect(Collectors.toList())
        );

        summary.setRecentDistributions(
                distributionRepository.findTop5ByOrderByIdDesc().stream()
                        .map(DistributionResponse::fromEntity)
                        .collect(Collectors.toList())
        );

        return summary;
    }
}
