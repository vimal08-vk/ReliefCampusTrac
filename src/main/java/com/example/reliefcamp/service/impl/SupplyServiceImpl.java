package com.example.reliefcamp.service.impl;

import com.example.reliefcamp.dto.InventoryItemDTO;
import com.example.reliefcamp.dto.SupplyRequest;
import com.example.reliefcamp.dto.SupplyResponse;
import com.example.reliefcamp.exception.InvalidOperationException;
import com.example.reliefcamp.exception.ResourceNotFoundException;
import com.example.reliefcamp.model.Camp;
import com.example.reliefcamp.model.Supply;
import com.example.reliefcamp.repository.SupplyRepository;
import com.example.reliefcamp.service.CampService;
import com.example.reliefcamp.service.SupplyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final CampService campService;

    public SupplyServiceImpl(SupplyRepository supplyRepository, CampService campService) {
        this.supplyRepository = supplyRepository;
        this.campService = campService;
    }

    @Override
    public SupplyResponse createSupply(SupplyRequest request) {
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new InvalidOperationException("Supply quantity cannot be negative");
        }

        Camp camp = campService.getCampEntityById(request.getCampId());

        Supply supply = new Supply();
        supply.setName(request.getName().trim());
        supply.setType(request.getType().trim());
        supply.setQuantity(request.getQuantity());
        supply.setUnit(request.getUnit().trim());
        supply.setReceivedDate(request.getReceivedDate() != null ? request.getReceivedDate() : LocalDate.now());
        supply.setCamp(camp);

        Supply savedSupply = supplyRepository.save(supply);
        return SupplyResponse.fromEntity(savedSupply);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplyResponse> getAllSupplies() {
        return supplyRepository.findAll().stream()
                .map(SupplyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplyResponse> getSuppliesByCamp(Long campId) {
        // Validate camp exists
        campService.getCampEntityById(campId);

        return supplyRepository.findByCampId(campId).stream()
                .map(SupplyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplyResponse getSupplyById(Long id) {
        Supply supply = getSupplyEntityById(id);
        return SupplyResponse.fromEntity(supply);
    }

    @Override
    public SupplyResponse updateSupply(Long id, SupplyRequest request) {
        Supply supply = getSupplyEntityById(id);

        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new InvalidOperationException("Supply quantity cannot be negative");
        }

        if (request.getCampId() != null && !request.getCampId().equals(supply.getCamp().getId())) {
            Camp newCamp = campService.getCampEntityById(request.getCampId());
            supply.setCamp(newCamp);
        }

        supply.setName(request.getName().trim());
        supply.setType(request.getType().trim());
        supply.setQuantity(request.getQuantity());
        supply.setUnit(request.getUnit().trim());
        if (request.getReceivedDate() != null) {
            supply.setReceivedDate(request.getReceivedDate());
        }

        Supply updatedSupply = supplyRepository.save(supply);
        return SupplyResponse.fromEntity(updatedSupply);
    }

    @Override
    public void deleteSupply(Long id) {
        Supply supply = getSupplyEntityById(id);
        supplyRepository.delete(supply);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryItemDTO> getInventory() {
        return supplyRepository.findAll().stream()
                .map(InventoryItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Supply getSupplyEntityById(Long id) {
        return supplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supply not found with ID: " + id));
    }
}
