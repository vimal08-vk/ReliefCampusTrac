package com.example.reliefcamp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class DistributionRequest {

    @NotNull(message = "Camp is required")
    private Long campId;

    @NotNull(message = "Family is required")
    private Long familyId;

    @NotNull(message = "Supply is required")
    private Long supplyId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String remarks;

    private LocalDate distributionDate;

    public DistributionRequest() {
    }

    public DistributionRequest(Long campId, Long familyId, Long supplyId, Integer quantity, String remarks) {
        this(campId, familyId, supplyId, quantity, remarks, LocalDate.now());
    }

    public DistributionRequest(Long campId, Long familyId, Long supplyId, Integer quantity, String remarks, LocalDate distributionDate) {
        this.campId = campId;
        this.familyId = familyId;
        this.supplyId = supplyId;
        this.quantity = quantity;
        this.remarks = remarks;
        this.distributionDate = distributionDate != null ? distributionDate : LocalDate.now();
    }

    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Long supplyId) {
        this.supplyId = supplyId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(LocalDate distributionDate) {
        this.distributionDate = distributionDate;
    }
}
