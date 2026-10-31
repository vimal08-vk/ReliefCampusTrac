package com.example.reliefcamp.service;

import com.example.reliefcamp.dto.InventoryItemDTO;
import com.example.reliefcamp.dto.SupplyRequest;
import com.example.reliefcamp.dto.SupplyResponse;
import com.example.reliefcamp.model.Supply;

import java.util.List;

public interface SupplyService {

    SupplyResponse createSupply(SupplyRequest request);

    List<SupplyResponse> getAllSupplies();

    List<SupplyResponse> getSuppliesByCamp(Long campId);

    SupplyResponse getSupplyById(Long id);

    SupplyResponse updateSupply(Long id, SupplyRequest request);

    void deleteSupply(Long id);

    List<InventoryItemDTO> getInventory();

    Supply getSupplyEntityById(Long id);
}
