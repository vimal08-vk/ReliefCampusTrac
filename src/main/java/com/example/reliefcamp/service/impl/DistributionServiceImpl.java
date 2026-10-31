package com.example.reliefcamp.service.impl;

import com.example.reliefcamp.dto.DistributionRequest;
import com.example.reliefcamp.dto.DistributionResponse;
import com.example.reliefcamp.exception.InsufficientInventoryException;
import com.example.reliefcamp.exception.InvalidOperationException;
import com.example.reliefcamp.exception.ResourceNotFoundException;
import com.example.reliefcamp.model.Camp;
import com.example.reliefcamp.model.Distribution;
import com.example.reliefcamp.model.Family;
import com.example.reliefcamp.model.Supply;
import com.example.reliefcamp.repository.DistributionRepository;
import com.example.reliefcamp.repository.SupplyRepository;
import com.example.reliefcamp.service.CampService;
import com.example.reliefcamp.service.DistributionService;
import com.example.reliefcamp.service.FamilyService;
import com.example.reliefcamp.service.SupplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DistributionServiceImpl implements DistributionService {

    private final DistributionRepository distributionRepository;
    private final SupplyRepository supplyRepository;
    private final CampService campService;
    private final FamilyService familyService;
    private final SupplyService supplyService;

    public DistributionServiceImpl(DistributionRepository distributionRepository,
                                   SupplyRepository supplyRepository,
                                   CampService campService,
                                   FamilyService familyService,
                                   SupplyService supplyService) {
        this.distributionRepository = distributionRepository;
        this.supplyRepository = supplyRepository;
        this.campService = campService;
        this.familyService = familyService;
        this.supplyService = supplyService;
    }

    @Override
    public DistributionResponse createDistribution(DistributionRequest request) {
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new InvalidOperationException("Distribution quantity must be greater than 0");
        }

        Camp camp = campService.getCampEntityById(request.getCampId());
        Family family = familyService.getFamilyEntityById(request.getFamilyId());
        Supply supply = supplyService.getSupplyEntityById(request.getSupplyId());

        // Validate family belongs to camp
        if (!family.getCamp().getId().equals(camp.getId())) {
            throw new InvalidOperationException("Selected family is not registered in " + camp.getName());
        }

        // Validate supply belongs to camp
        if (!supply.getCamp().getId().equals(camp.getId())) {
            throw new InvalidOperationException("Selected supply does not belong to " + camp.getName());
        }

        // Check inventory rule:
        int currentInventory = supply.getQuantity() != null ? supply.getQuantity() : 0;
        int requestedQuantity = request.getQuantity();

        if (currentInventory < requestedQuantity) {
            throw new InsufficientInventoryException("Insufficient inventory. Available: " +
                    currentInventory + " " + supply.getUnit() +
                    ", Requested: " + requestedQuantity + " " + supply.getUnit());
        }

        // Deduct inventory
        supply.setQuantity(currentInventory - requestedQuantity);
        supplyRepository.save(supply);

        // Record distribution
        Distribution distribution = new Distribution();
        distribution.setCamp(camp);
        distribution.setFamily(family);
        distribution.setSupply(supply);
        distribution.setQuantity(requestedQuantity);
        distribution.setDistributionDate(request.getDistributionDate() != null ? request.getDistributionDate() : LocalDate.now());
        distribution.setRemarks(request.getRemarks() != null ? request.getRemarks().trim() : null);
        distribution.setStatus("COMPLETED");

        Distribution savedDistribution = distributionRepository.save(distribution);
        return DistributionResponse.fromEntity(savedDistribution);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponse> getAllDistributions() {
        return distributionRepository.findAll().stream()
                .map(DistributionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponse> getDistributionsByCamp(Long campId) {
        campService.getCampEntityById(campId);
        return distributionRepository.findByCampId(campId).stream()
                .map(DistributionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributionResponse> getDistributionsByFamily(Long familyId) {
        familyService.getFamilyEntityById(familyId);
        return distributionRepository.findByFamilyId(familyId).stream()
                .map(DistributionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DistributionResponse getDistributionById(Long id) {
        Distribution distribution = distributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribution not found with ID: " + id));
        return DistributionResponse.fromEntity(distribution);
    }

    @Override
    public void deleteDistribution(Long id) {
        Distribution distribution = distributionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distribution not found with ID: " + id));

        // Restore inventory when distribution is deleted/canceled
        Supply supply = distribution.getSupply();
        if (supply != null) {
            supply.setQuantity(supply.getQuantity() + distribution.getQuantity());
            supplyRepository.save(supply);
        }

        distributionRepository.delete(distribution);
    }
}
